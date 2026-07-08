package roboticarm;

public class LoggerTask extends Thread {

    private final MotorController motorController;

    public LoggerTask(MotorController motorController) {

        this.motorController = motorController;

        setName("LoggerTask");
        setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {

        motorController.loggerAccess();
    }
}
