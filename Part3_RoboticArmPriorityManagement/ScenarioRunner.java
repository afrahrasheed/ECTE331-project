package roboticarm;

public class ScenarioRunner {

    public static PerformanceResult runScenario(
            PriorityMode mode,
            boolean verbose) {

        ScenarioCoordinator coordinator =
                new ScenarioCoordinator(verbose);

        MotorController motorController =
                new MotorController(mode, coordinator);

        LoggerTask loggerTask =
                new LoggerTask(motorController);

        MotionPlanner motionPlanner =
                new MotionPlanner(coordinator);

        SafetyMonitor safetyMonitor =
                new SafetyMonitor(motorController);

        coordinator.log(
                "Starting scenario: " + mode
        );

        loggerTask.start();

        try {

            coordinator.loggerAcquired.await();

            safetyMonitor.start();
            motionPlanner.start();

            loggerTask.join();
            safetyMonitor.join();
            motionPlanner.join();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        coordinator.log(
                "Scenario completed: " + mode
        );

        return new PerformanceResult(
                mode,
                safetyMonitor.getWaitingTimeMs(),
                safetyMonitor.getResponseTimeMs()
        );
    }
}
