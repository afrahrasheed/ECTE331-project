package synchronisation;


public class Main {

    public static void main(String[] args) {

        final int ITERATIONS = 10000;

        SharedData sharedData =
                new SharedData();

        ThreadA threadA =
                new ThreadA(sharedData, ITERATIONS);

        ThreadB threadB =
                new ThreadB(sharedData, ITERATIONS);

        System.out.println(
                "Starting synchronisation test..."
        );

        System.out.println(
                "Number of iterations: "
                        + ITERATIONS
        );

        long startTime =
                System.currentTimeMillis();

        threadA.start();
        threadB.start();

        try {

            threadA.join();
            threadB.join();

        } catch (InterruptedException e) {

            System.out.println(
                    "Main thread interrupted"
            );

            Thread.currentThread().interrupt();
        }

        long endTime =
                System.currentTimeMillis();

        long expectedA1 = 125250;
        long expectedB1 = 31375;
        long expectedB2 = 145350;
        long expectedA2 = 190500;
        long expectedB3 = 270700;
        long expectedA3 = 350900;

        System.out.println();
        System.out.println(
                "===== FINAL RESULTS ====="
        );

        System.out.println(
                "A1 = " + sharedData.getA1()
                        + " | Expected = "
                        + expectedA1
        );

        System.out.println(
                "B1 = " + sharedData.getB1()
                        + " | Expected = "
                        + expectedB1
        );

        System.out.println(
                "B2 = " + sharedData.getB2()
                        + " | Expected = "
                        + expectedB2
        );

        System.out.println(
                "A2 = " + sharedData.getA2()
                        + " | Expected = "
                        + expectedA2
        );

        System.out.println(
                "B3 = " + sharedData.getB3()
                        + " | Expected = "
                        + expectedB3
        );

        System.out.println(
                "A3 = " + sharedData.getA3()
                        + " | Expected = "
                        + expectedA3
        );

        boolean correct =
                sharedData.getA1() == expectedA1
                && sharedData.getB1() == expectedB1
                && sharedData.getB2() == expectedB2
                && sharedData.getA2() == expectedA2
                && sharedData.getB3() == expectedB3
                && sharedData.getA3() == expectedA3;

        System.out.println();

        if (correct) {

            System.out.println(
                    "TEST PASSED"
            );

            System.out.println(
                    "All shared variables are correct."
            );

        } else {

            System.out.println(
                    "TEST FAILED"
            );

            System.out.println(
                    "One or more values are incorrect."
            );
        }

        System.out.println(
                "Execution time = "
                        + (endTime - startTime)
                        + " ms"
        );
    }
}
