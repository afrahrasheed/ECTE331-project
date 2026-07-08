package drone;

import java.util.Random;


public class DroneSensor {

    private final String sensorId;
    private final Random random;

    
    public DroneSensor(String sensorId) {
        this.sensorId = sensorId;
        this.random = new Random();
    }

   
    public String getSensorId() {
        return sensorId;
    }

    
    public int readSensor(int baselineValue, int range)
            throws SensorReadException {

        int chance = random.nextInt(100);

        if (chance < 15) {

            throw new SensorReadException(
                    "Sensor " + sensorId + " failed"
            );

        } else if (chance < 30) {

            // Generate corrupted reading outside valid [0, 200] range
            if (random.nextBoolean()) {
                return -1 - random.nextInt(100);
            } else {
                return 201 + random.nextInt(100);
            }

        } else {

            // Generate a valid altitude reading
            int reading = baselineValue + random.nextInt(range);

            // Guarantee that valid values remain inside 0-200
            if (reading > 200) {
                reading = 200;
            }

            if (reading < 0) {
                reading = 0;
            }

            return reading;
        }
    }
}
