package drone;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileLogger {

    private final String fileName;
    private final DateTimeFormatter formatter;

    
    public FileLogger(String fileName) {
        this.fileName = fileName;
        this.formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    
    public void log(String message) {

        String timestamp =
                LocalDateTime.now().format(formatter);

        try (FileWriter fileWriter =
                     new FileWriter(fileName, true);
             PrintWriter printWriter =
                     new PrintWriter(fileWriter)) {

            printWriter.println(
                    "[" + timestamp + "] " + message
            );

        } catch (IOException e) {

            System.err.println(
                    "Logging error: " + e.getMessage()
            );
        }
    }
}
