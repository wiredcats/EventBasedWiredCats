///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package Util2415;
//
//import WiredCatsControllers.ControllerDrive;
//import WiredCatsControllers.ControllerArm;
//import WiredCatsControllers.ControllerShooter;
//import WiredCatsEvents.EventGamePad;
//import WiredCatsEvents.EventStateChange;
//import WiredCatsEvents.WiredCatsEvent;
//import WiredCatsSystems.SystemArm;
//import WiredCatsSystems.SystemDrive;
//import WiredCatsSystems.SystemDrive;
//import WiredCatsSystems.SystemIntake;
//import WiredCatsSystems.SystemIntake;
//import WiredCatsSystems.SystemShooter;
//import WiredCatsSystems.SystemShooter;
//import WiredCatsSystems.WiredCatsSystem;
//import com.sun.squawk.microedition.io.FileConnection;
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.templates.WiredCats2415;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Vector;
//import javax.microedition.io.Connector;
//
///**
// *
// * @author BruceCrane
// */
//public class WiredCatsLogger implements Runnable
//{
//    
//    WiredCats2415 wiredcats2415;
//    
//    DataOutputStream theFile;
//    FileConnection fc;
//    
//    private ControllerArm ci;
//    private ControllerShooter cs;
//    private ControllerDrive cd;
//    
//    private SystemDrive sd;
//    private SystemIntake si;
//    private SystemShooter ss;
//    private SystemArm sa;
//    
//    private Timer logTimer;
//    private Timer robotTimer;
//    
//    private String fileName;
//    private int counter;
//    
//    private Vector controllers;
//    
//    public WiredCatsLogger()
//    {
//        logTimer = new Timer();
//        robotTimer = new Timer();
//        theFile = null;
//        fc = null;
//        fileName = null;
//        counter = 0;
//        System.out.println("[WiredCats] Logging System Initialized.");
//    }
//    
//    public void addControllers(ControllerArm ci, ControllerShooter cs, ControllerDrive cd)
//    {
//        this.ci = ci;
//        this.cs = cs;
//        this.cd = cd;
//    }
//    
//    public void addSystems(SystemDrive sd, SystemIntake si, SystemShooter ss, SystemArm sa)
//    {
//        this.sd = sd;
//        this.si = si;
//        this.ss = ss;
//        this.sa = sa;
//    }
//    
//    public void newLog()
//    {
//        robotTimer.reset();
//        robotTimer.start();
//        fileName = SmartDashboard.getString("Logger File Name Base");
//        counter++;
//
//        try
//        {
//            System.out.println("[WiredCats] Creating new Log: " + fileName + counter + ".txt");
//            fc = (FileConnection)Connector.open("file:///WiredCatsLogs/" + fileName + counter + ".txt", Connector.WRITE);    
//            fc.create();
//            theFile = fc.openDataOutputStream();
//        }
//        catch (IOException ioe)
//        {
//            ioe.printStackTrace();
//        }
//        
//        logTimer.reset();
//        logTimer.start();
//
//    }
//    
//    public boolean isOpen()
//    {
//        return theFile != null;
//    }
//    
//    /**
//     * Takes the string of data, and writes it
//     * in a way that the LogReader can then access
//     * later.
//     * @param s 
//     */
//    public void log()
//    {
//        String eventWriteUp = "";
//        eventWriteUp += cd.getLeftTicks() + " ";
//        eventWriteUp += cd.getRightTicks() + " ";
//        //eventWriteUp += cd.lastGyroValue + " ";
//        eventWriteUp += ss.getFrisbeesShot() + " ";
//        eventWriteUp += sa.getArmAngle() + " ";
//        eventWriteUp += si.isIntakeOn() + '\n';
//        
//        writeString(eventWriteUp);
//    }
//    
//    private void writeString(String s)
//    {
//        char[] thing = s.toCharArray();
//        try {
//        for (int i = 0; i < thing.length; i++) theFile.write(thing[i]);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            System.out.println("[WiredCats: Error] Attempted to write to log and failed.");
//        }
//    }
//    
//    /**
//     * Closes the file, called when we want to stop
//     * logging.
//     */
//    public void close()
//    {
//        //System.out.println("C")
//        robotTimer.stop();
//        robotTimer.reset();
//        System.out.println("[WiredCats] Closing Log.");
//        
//        try
//        {
//          theFile.close();  
//        } catch (IOException ioe) { ioe.printStackTrace(); }
//        
//        theFile = null;
//        fc = null;
//        logTimer.stop();
//        logTimer.reset();
//    }
//
//    public void run() 
//    {
//        SmartDashboard.putString("Logger File Name Base", "CHANGEME");
//        while (true) 
//        {
//            if (logTimer.get() > 0.1)
//            {                
//                logTimer.stop();
//                logTimer.reset();
//                log();
//                logTimer.start();
//            }
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//}
