package drone;

import java.io.IOException;


public class SensorReadException extends IOException {

    private static final long serialVersionUID = 1L;

    
    public SensorReadException(String message) {
        super(message);
    }
}
