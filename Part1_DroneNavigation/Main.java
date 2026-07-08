package drone;


public class Main {

  
    public static void main(String[] args) {

        FileLogger logger =
                new FileLogger("log.txt");

        DroneController controller =
                new DroneController(logger);

        logger.log(
                "Drone navigation system started"
        );

        try {

            for (int cycle = 1; cycle <= 20; cycle++) {

                System.out.println();
                System.out.println(
                        
                );

                System.out.println(
                        "Processing Cycle " + cycle
                );

                System.out.println(
                      
                );

                controller.processCycle();
            }

            logger.log(
                    "Drone navigation system completed normally"
            );

        } catch (SystemReliabilityException e) {

            System.out.println();
            System.out.println(
                
            );

            System.out.println(
                    "SAFE MODE ACTIVATED"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "System execution stopped"
            );

            System.out.println(
                  
            );

            logger.log(
                    "SAFE MODE activation: "
                            + e.getMessage()
            );
        }
    }
}
