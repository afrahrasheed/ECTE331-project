package roboticarm;

import java.util.concurrent.TimeUnit;

public class MotorController {

    private final PriorityMode mode;
    private final ScenarioCoordinator coordinator;

    private volatile Thread owner;
    private volatile int ownerOriginalPriority;

    public MotorController(
            PriorityMode mode,
            ScenarioCoordinator coordinator) {

        this.mode = mode;
        this.coordinator = coordinator;
    }

    public void loggerAccess() {

        synchronized (this) {

            Thread current = Thread.currentThread();

            owner = current;
            ownerOriginalPriority = current.getPriority();

            if (mode == PriorityMode.PRIORITY_CEILING) {

                current.setPriority(Thread.MAX_PRIORITY);

                coordinator.log(
                        "Priority ceiling applied to LoggerTask: "
                                + Thread.MAX_PRIORITY
                );
            }

            coordinator.log(
                    "LoggerTask acquired MotorController"
            );

            coordinator.loggerAcquired.countDown();

            try {

                coordinator.highAttempted.await();

                if (mode == PriorityMode.BASELINE) {

                    coordinator.log(
                            "LoggerTask delayed while MotionPlanner executes"
                    );

                    coordinator.mediumFinished.await();
                }

                if (mode == PriorityMode.PRIORITY_INHERITANCE) {

                    coordinator.log(
                            "LoggerTask continues with inherited priority "
                                    + current.getPriority()
                    );
                }

                if (mode == PriorityMode.PRIORITY_CEILING) {

                    coordinator.log(
                            "LoggerTask continues at ceiling priority"
                    );
                }

                busyWork(60);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }

            if (current.getPriority() != ownerOriginalPriority) {

                current.setPriority(ownerOriginalPriority);

                coordinator.log(
                        "LoggerTask priority restored to "
                                + ownerOriginalPriority
                );
            }

            coordinator.log(
                    "LoggerTask released MotorController"
            );

            owner = null;
        }
    }

    public long safetyAccess() {

        coordinator.log(
                "SafetyMonitor attempting to access MotorController"
        );

        if (mode == PriorityMode.PRIORITY_INHERITANCE) {
            applyPriorityInheritance(Thread.currentThread());
        }

        coordinator.highAttempted.countDown();

        long start = System.nanoTime();
        long acquired;

        synchronized (this) {

            acquired = System.nanoTime();

            coordinator.log(
                    "SafetyMonitor acquired MotorController"
            );

            busyWork(30);

            coordinator.log(
                    "SafetyMonitor completed emergency operation"
            );
        }

        long end = System.nanoTime();

        long waitingTime =
                TimeUnit.NANOSECONDS.toMillis(acquired - start);

        long responseTime =
                TimeUnit.NANOSECONDS.toMillis(end - start);

        return packTimes(waitingTime, responseTime);
    }

    private void applyPriorityInheritance(Thread highPriorityThread) {

        Thread currentOwner = owner;

        if (currentOwner != null
                && currentOwner.getPriority()
                < highPriorityThread.getPriority()) {

            int oldPriority =
                    currentOwner.getPriority();

            currentOwner.setPriority(
                    highPriorityThread.getPriority()
            );

            coordinator.log(
                    "Priority inheritance applied: LoggerTask priority "
                            + oldPriority
                            + " -> "
                            + currentOwner.getPriority()
            );
        }
    }

    private long packTimes(long waitingTime, long responseTime) {

        return (waitingTime << 32)
                | (responseTime & 0xffffffffL);
    }

    public static long unpackWaitingTime(long packed) {
        return packed >>> 32;
    }

    public static long unpackResponseTime(long packed) {
        return packed & 0xffffffffL;
    }

    private void busyWork(long durationMs) {

        long end =
                System.nanoTime()
                        + TimeUnit.MILLISECONDS.toNanos(durationMs);

        double value = 0;

        while (System.nanoTime() < end) {
            value += Math.sqrt(value + 1.0);
        }

        if (value < 0) {
            System.out.println(value);
        }
    }
}
