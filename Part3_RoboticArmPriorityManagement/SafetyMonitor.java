package roboticarm;

public class SafetyMonitor extends Thread {

    private final MotorController motorController;

    private double waitingTimeMs;
    private double responseTimeMs;

    public SafetyMonitor(
            MotorController motorController) {

        this.motorController = motorController;

        setName("SafetyMonitor");
        setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {

        long packed =
                motorController.safetyAccess();

        waitingTimeMs =
                MotorController.unpackWaitingTime(packed);

        responseTimeMs =
                MotorController.unpackResponseTime(packed);
    }

    public double getWaitingTimeMs() {
        return waitingTimeMs;
    }

    public double getResponseTimeMs() {
        return responseTimeMs;
    }
}
