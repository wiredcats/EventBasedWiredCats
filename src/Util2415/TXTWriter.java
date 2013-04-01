/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util2415;

import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;

/**
 *
 * @author Robotics
 */
public class TXTWriter {
    DataOutputStream theFile;
    FileConnection fc;
    
    public TXTWriter() {
 
    }
    
    public void update() {
        Enumeration keys = WiredCats2415.textReader.getKeys();
        
        try {
            System.out.println("[WiredCats] Updating WiredCatsConfig.txt");
            fc = (FileConnection)Connector.open("file:///WiredCatsConfig.txt", Connector.WRITE);    
            fc.create();
            theFile = fc.openDataOutputStream();
            
            String key = (String)keys.nextElement();
            writeString(key + ", " + WiredCats2415.textReader.getValue(key));
            while (keys.hasMoreElements()) {
                writeString("\n");
                key = (String)keys.nextElement();
                WiredCats2415.textReader.setValue(key, SmartDashboard.getNumber(key));
                writeString(key + ", " + WiredCats2415.textReader.getValue(key));
            }
            writeString("#\n");
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("[WiredCats] Values Updated.");
    }
    
    private void writeString(String s) {
        char[] thing = s.toCharArray();
        try {
            for (int i = 0; i < thing.length; i++) {
                theFile.write(thing[i]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[WiredCats: Error] Attempted to write to log and failed.");
        }
    }
    
}
