package roboticarm;

import java.util.concurrent.TimeUnit;

public class MotionPlanner extends Thread {

    private final ScenarioCoordinator coordinator;

    public MotionPlanner(
            ScenarioCoordinator coordinator) {

        this.coordinator = coordinator;

        setName("MotionPlanner");
        setPriority(Thread.NORM_PRIORITY);
    }

    @Override
    public void run() {

        try {

            coordinator.highAttempted.await();

            coordinator.log(
                    "MotionPlanner started medium-priority execution"
            );

            busyWork(250);

            coordinator.log(
                    "MotionPlanner finished execution"
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        } finally {

            coordinator.mediumFinished.countDown();
        }
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
