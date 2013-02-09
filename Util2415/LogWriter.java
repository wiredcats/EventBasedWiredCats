package Util2415;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.OutputConnection;

/**
 * The write-companion to our Scanner.
 * The actual meat of the EventLogger.
 * 
 * @author Robotics
 */
public class LogWriter {

    DataOutputStream theFile;
    FileConnection fc;

    public LogWriter(String s) {
        try {
            fc = (FileConnection) Connector.open("file:///" + s, Connector.WRITE);
            fc.create();
            theFile = fc.openDataOutputStream();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    /**
     * Takes the string of data, and writes it in a way that the LogReader can
     * then access later.
     *
     * @param s
     */
    public void logEvent(String s) {
        try {
            theFile.writeChars("yay" + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Closes the file, called when we want to stop logging.
     */
    public void close() {
        try {
            theFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
