/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util2415;

import WiredCatsControllers.ControllerDrive;
import WiredCatsControllers.ControllerIntake;
import WiredCatsControllers.ControllerShooter;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.WiredCatsEvent;
import com.sun.squawk.microedition.io.FileConnection;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 *
 * @author BruceCrane
 */
public class WiredCatsEventLogger 
{
    
    DataOutputStream theFile;
    FileConnection fc;
    
    private ControllerIntake ci;
    private ControllerShooter cs;
    private ControllerDrive cd;
    
    private Timer timer;
    
    private String fileName;
    private int counter;
    
    private Vector controllers;
    
    public WiredCatsEventLogger()
    {
        timer = new Timer();
        theFile = null;
        fc = null;
        fileName = null;
        counter = 0;
    }
    
    public void addControllers(ControllerIntake ci, ControllerShooter cs, ControllerDrive cd)
    {
        this.ci = ci;
        this.cs = cs;
        this.cd = cd;
    }
    
    public void newLog()
    {
        timer.reset();
        timer.start();
        fileName = SmartDashboard.getString("Logger File Name Base");
        counter++;

        try
        {
            System.out.println("[WiredCats] Creating new Log: " + fileName + counter + ".txt");
            fc = (FileConnection)Connector.open("file:///WiredCatsLogs/" + fileName + counter + ".txt", Connector.WRITE);    
            fc.create();
            theFile = fc.openDataOutputStream();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
    
    public boolean isOpen()
    {
        return theFile != null;
    }
    
    /**
     * Takes the string of data, and writes it
     * in a way that the LogReader can then access
     * later.
     * @param s 
     */
    public synchronized void logEvent(WiredCatsEvent e)
    {
        if (!(e instanceof EventGamePad)) return;
        String eventWriteUp = "";
        eventWriteUp += e.toString() + " " + timer.get();
        
        writeString(eventWriteUp + "\n");
    }
    
    private void writeString(String s)
    {
        char[] thing = s.toCharArray();
        try {
        for (int i = 0; i < thing.length; i++) theFile.write(thing[i]);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("[WiredCats: Error] Attempted to write to log and failed.");
        }
    }
    
    /**
     * Closes the file, called when we want to stop
     * logging.
     */
    public void close()
    {
        //System.out.println("C")
        timer.stop();
        System.out.println("[WiredCats] Closing Log.");
        
        try
        {
          theFile.close();  
        } catch (IOException ioe) { ioe.printStackTrace(); }
        
        theFile = null;
        fc = null;
    }
    
}
