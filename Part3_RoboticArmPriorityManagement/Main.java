package roboticarm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "REAL-TIME ROBOTIC ARM CONTROLLER"
        );

        System.out.println(
                "Priority Management Demonstration"
        );

        System.out.println();

        runDemonstrations();

        System.out.println();
        System.out.println(
                "PERFORMANCE EVALUATION"
        );

        evaluatePerformance(5);
    }

    private static void runDemonstrations() {

        for (PriorityMode mode : PriorityMode.values()) {

            System.out.println();
            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "DEMONSTRATION: " + mode
            );

            System.out.println(
                    "===================================="
            );

            PerformanceResult result =
                    ScenarioRunner.runScenario(
                            mode,
                            true
                    );

            System.out.printf(
                    "SafetyMonitor waiting time: %.2f ms%n",
                    result.getWaitingTimeMs()
            );

            System.out.printf(
                    "SafetyMonitor response time: %.2f ms%n",
                    result.getResponseTimeMs()
            );
        }
    }

    private static void evaluatePerformance(int runs) {

        double baselineWait = 0;
        double baselineResponse = 0;

        double inheritanceWait = 0;
        double inheritanceResponse = 0;

        double ceilingWait = 0;
        double ceilingResponse = 0;

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(
                                     "performance_results.csv"
                             )
                     )) {

            writer.println(
                    "Mode,Run,WaitingTimeMs,ResponseTimeMs"
            );

            for (int run = 1; run <= runs; run++) {

                PerformanceResult baseline =
                        ScenarioRunner.runScenario(
                                PriorityMode.BASELINE,
                                false
                        );

                PerformanceResult inheritance =
                        ScenarioRunner.runScenario(
                                PriorityMode.PRIORITY_INHERITANCE,
                                false
                        );

                PerformanceResult ceiling =
                        ScenarioRunner.runScenario(
                                PriorityMode.PRIORITY_CEILING,
                                false
                        );

                baselineWait +=
                        baseline.getWaitingTimeMs();

                baselineResponse +=
                        baseline.getResponseTimeMs();

                inheritanceWait +=
                        inheritance.getWaitingTimeMs();

                inheritanceResponse +=
                        inheritance.getResponseTimeMs();

                ceilingWait +=
                        ceiling.getWaitingTimeMs();

                ceilingResponse +=
                        ceiling.getResponseTimeMs();

                writer.printf(
                        "%s,%d,%.2f,%.2f%n",
                        baseline.getMode(),
                        run,
                        baseline.getWaitingTimeMs(),
                        baseline.getResponseTimeMs()
                );

                writer.printf(
                        "%s,%d,%.2f,%.2f%n",
                        inheritance.getMode(),
                        run,
                        inheritance.getWaitingTimeMs(),
                        inheritance.getResponseTimeMs()
                );

                writer.printf(
                        "%s,%d,%.2f,%.2f%n",
                        ceiling.getMode(),
                        run,
                        ceiling.getWaitingTimeMs(),
                        ceiling.getResponseTimeMs()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "CSV file error: "
                            + e.getMessage()
            );
        }

        double avgBaselineWait =
                baselineWait / runs;

        double avgBaselineResponse =
                baselineResponse / runs;

        double avgInheritanceWait =
                inheritanceWait / runs;

        double avgInheritanceResponse =
                inheritanceResponse / runs;

        double avgCeilingWait =
                ceilingWait / runs;

        double avgCeilingResponse =
                ceilingResponse / runs;

        System.out.println();
        System.out.println(
                "AVERAGE RESULTS OVER "
                        + runs
                        + " RUNS"
        );

        System.out.println();

        System.out.printf(
                "%-25s %-18s %-18s%n",
                "Mode",
                "Waiting Time",
                "Response Time"
        );

        System.out.printf(
                "%-25s %-18.2f %-18.2f%n",
                "Baseline",
                avgBaselineWait,
                avgBaselineResponse
        );

        System.out.printf(
                "%-25s %-18.2f %-18.2f%n",
                "Priority Inheritance",
                avgInheritanceWait,
                avgInheritanceResponse
        );

        System.out.printf(
                "%-25s %-18.2f %-18.2f%n",
                "Priority Ceiling",
                avgCeilingWait,
                avgCeilingResponse
        );

        System.out.println();
        System.out.println(
                "WAITING TIME CHART"
        );

        printBar(
                "Baseline",
                avgBaselineWait
        );

        printBar(
                "Inheritance",
                avgInheritanceWait
        );

        printBar(
                "Ceiling",
                avgCeilingWait
        );

        System.out.println();
        System.out.println(
                "Results saved to performance_results.csv"
        );
    }

    private static void printBar(
            String label,
            double value) {

        int bars =
                Math.max(
                        1,
                        (int) Math.round(value / 10.0)
                );

        System.out.printf(
                "%-12s | ",
                label
        );

        for (int i = 0; i < bars; i++) {
            System.out.print("#");
        }

        System.out.printf(
                " %.2f ms%n",
                value
        );
    }
}
