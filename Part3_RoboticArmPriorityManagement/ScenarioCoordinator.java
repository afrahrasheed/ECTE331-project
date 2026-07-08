package roboticarm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

public class ScenarioCoordinator {

    public final CountDownLatch loggerAcquired =
            new CountDownLatch(1);

    public final CountDownLatch highAttempted =
            new CountDownLatch(1);

    public final CountDownLatch mediumFinished =
            new CountDownLatch(1);

    private final boolean verbose;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public ScenarioCoordinator(boolean verbose) {
        this.verbose = verbose;
    }

    public synchronized void log(String message) {

        if (verbose) {
            System.out.println(
                    "[" + LocalTime.now().format(formatter) + "] "
                            + message
            );
        }
    }
}
