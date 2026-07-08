package roboticarm;

public class PerformanceResult {

    private final PriorityMode mode;
    private final double waitingTimeMs;
    private final double responseTimeMs;

    public PerformanceResult(
            PriorityMode mode,
            double waitingTimeMs,
            double responseTimeMs) {

        this.mode = mode;
        this.waitingTimeMs = waitingTimeMs;
        this.responseTimeMs = responseTimeMs;
    }

    public PriorityMode getMode() {
        return mode;
    }

    public double getWaitingTimeMs() {
        return waitingTimeMs;
    }

    public double getResponseTimeMs() {
        return responseTimeMs;
    }
}
