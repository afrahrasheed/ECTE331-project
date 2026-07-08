package drone;

import java.util.ArrayList;
import java.util.List;


public class DroneController {

    private final DroneSensor sensorA;
    private final DroneSensor sensorB;
    private final DroneSensor sensorC;

    private final FileLogger logger;

    private int previousValidAltitude;
    private int consecutiveFailures;

   
    public DroneController(FileLogger logger) {

        this.sensorA = new DroneSensor("A");
        this.sensorB = new DroneSensor("B");
        this.sensorC = new DroneSensor("C");

        this.logger = logger;

        this.previousValidAltitude = 100;
        this.consecutiveFailures = 0;
    }

    
    public void processCycle()
            throws SystemReliabilityException {

        Integer readingA = readOneSensor(sensorA);
        Integer readingB = readOneSensor(sensorB);
        Integer readingC = readOneSensor(sensorC);

        System.out.println();
        System.out.println("Sensor Results:");
        System.out.println("A = " + displayReading(readingA));
        System.out.println("B = " + displayReading(readingB));
        System.out.println("C = " + displayReading(readingC));

        List<Integer> validReadings = new ArrayList<>();

        if (isValid(readingA)) {
            validReadings.add(readingA);
        }

        if (isValid(readingB)) {
            validReadings.add(readingB);
        }

        if (isValid(readingC)) {
            validReadings.add(readingC);
        }

        // Reliability failure: fewer than 2 valid sensors
        if (validReadings.size() < 2) {

            registerReliabilityFailure(
                    "Fewer than two valid sensor readings"
            );

            return;
        }

        Integer majorityValue =
                findMajority(readingA, readingB, readingC);

        if (majorityValue != null) {

            consecutiveFailures = 0;
            previousValidAltitude = majorityValue;

            System.out.println(
                    "Voting Decision: Majority altitude = "
                            + majorityValue + " m"
            );

            logger.log(
                    "Majority decision: altitude = "
                            + majorityValue + " m"
            );

            detectOutliers(
                    readingA,
                    readingB,
                    readingC,
                    majorityValue
            );

        } else {

            // No majority exists
            System.out.println(
                    "Voting Decision: No majority found"
            );

            logger.log(
                    "No majority found among valid sensors"
            );

            System.out.println(
                    "Fallback Decision: Using previous valid altitude = "
                            + previousValidAltitude + " m"
            );

            logger.log(
                    "Fallback decision: previous altitude = "
                            + previousValidAltitude + " m"
            );

            registerReliabilityFailure(
                    "No majority found"
            );
        }
    }

    /**
     * Reads one sensor and handles sensor failures.
     *
     * @param sensor sensor to read
     * @return altitude value, or null if complete failure occurs
     */
    private Integer readOneSensor(DroneSensor sensor) {

        try {

            int reading =
                    sensor.readSensor(90, 21);

            if (!isValid(reading)) {

                System.out.println(
                        "Sensor " + sensor.getSensorId()
                                + " CORRUPTED reading: "
                                + reading
                );

                logger.log(
                        "Corrupted reading from Sensor "
                                + sensor.getSensorId()
                                + ": " + reading
                );
            }

            return reading;

        } catch (SensorReadException e) {

            System.out.println(
                    "Sensor " + sensor.getSensorId()
                            + " FAILURE"
            );

            logger.log(
                    "Sensor failure: Sensor "
                            + sensor.getSensorId()
            );

            return null;
        }
    }

    
    private boolean isValid(Integer reading) {

        return reading != null
                && reading >= 0
                && reading <= 200;
    }

    
    private Integer findMajority(
            Integer a,
            Integer b,
            Integer c) {

        if (isValid(a)
                && isValid(b)
                && a.equals(b)) {

            return a;
        }

        if (isValid(a)
                && isValid(c)
                && a.equals(c)) {

            return a;
        }

        if (isValid(b)
                && isValid(c)
                && b.equals(c)) {

            return b;
        }

        return null;
    }

    
    private void detectOutliers(
            Integer a,
            Integer b,
            Integer c,
            int majorityValue) {

        if (isValid(a) && a != majorityValue) {

            System.out.println(
                    "Outlier detected: Sensor A"
            );

            logger.log(
                    "Outlier detected: Sensor A value = " + a
            );
        }

        if (isValid(b) && b != majorityValue) {

            System.out.println(
                    "Outlier detected: Sensor B"
            );

            logger.log(
                    "Outlier detected: Sensor B value = " + b
            );
        }

        if (isValid(c) && c != majorityValue) {

            System.out.println(
                    "Outlier detected: Sensor C"
            );

            logger.log(
                    "Outlier detected: Sensor C value = " + c
            );
        }
    }

   
    private void registerReliabilityFailure(String reason)
            throws SystemReliabilityException {

        consecutiveFailures++;

        System.out.println(
                "Reliability Failure: " + reason
        );

        System.out.println(
                "Consecutive failures = "
                        + consecutiveFailures
        );

        logger.log(
                "Reliability failure: "
                        + reason
                        + ". Consecutive failures = "
                        + consecutiveFailures
        );

        if (consecutiveFailures >= 2) {

            throw new SystemReliabilityException(
                    "Two consecutive reliability failures detected"
            );
        }
    }

   
    private String displayReading(Integer reading) {

        if (reading == null) {
            return "FAILED";
        }

        return reading + " m";
    }
}
