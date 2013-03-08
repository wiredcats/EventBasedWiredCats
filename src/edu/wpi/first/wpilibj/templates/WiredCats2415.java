/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

/**
 * Import all relevant libraries
 * Note that we are using the asterisk key for personal libraries 
 * since we do not want to constantly add new systems
 */

import Util2415.LogReader;
import Util2415.WiredCatsLogger;
import WiredCatsControllers.*;
import WiredCatsEvents.WiredCatsEventListener;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.StateChangeEvents.*;
import WiredCatsSystems.*;

//Specific utilities
import Util2415.TXTReader;
import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Vector; 


/**
 * Main Robot Class.
 * This is based on the SimpleRobot template and starts all of the other systems.
 * Also acts as a WiredCatsController as it fires state change events such as AUTONOMOUS or DISABLED
 * 
 * @author Bruce Crane
 */

public class WiredCats2415 extends SimpleRobot 
{

    public static TXTReader textReader;
    public WiredCatsLogger logWriter;
    
    private Vector listeners = new Vector(5);
    private Vector threads = new Vector(5);
    
    private ControllerGamePad controllerGamePad;
    private ControllerShooter controllerShooter;
    private ControllerArm controllerArm;
    private ControllerDrive controllerDrive;
    private ControllerAutonomous controllerAutonomous;
    
    private SystemDrive systemDrive;
    private SystemShooter systemShooter;
    private SystemIntake systemIntake;
    private SystemArm systemArm;
    
    private Compressor compressor;
    
    static
    {
        textReader = new TXTReader();
        textReader.getFromFile("CheesyConfig.txt");
    }
    
    public WiredCats2415() {
        
        //compressor = new Compressor(9, 10);
        
        
        initControllers();
        initDrive();
        initShooter();
        initIntake();
        initArm();
//        initAutonomous();
//        initLogger();
        
        

        
        for (int i = 0; i < threads.size(); i++) {
            ((Thread) (threads.elementAt(i))).start();
        }
    }

    /**
    * The following functions work by sending an event to all other listeners
    * This tells them what mode the robot is in now
    */
    
    public void disabled() 
    { 
        fireEvent(new EventDisabled(this)); 
//        if (logWriter.isOpen()) logWriter.close();
        textReader.getFromFile("CheesyConfig.txt");
//        controllerAutonomous.stop();
        super.getWatchdog().feed();
    }
    public void autonomous() 
    { 
        System.out.println("AUTONOMOUS.");
        //TODO make 100 dollars.
        controllerDrive.resetEncoders();
        fireEvent(new EventAutonomous(this)); 
        
        LogReader lr = new LogReader();
        Vector nodes = lr.readLog("PlayBook/testAutonomous.txt");
        for (int i = 0; i < nodes.size(); i++)
        {
            System.out.println(nodes.elementAt(i));
        }
        System.out.println("length: " + nodes.size());

//Node(double leftTicks, double rightTicks, int frisbeesShot, double armAngle, double isIntakeOn)
        
//        compressor.start();
//        controllerAutonomous.begin();
        //systemDrive.eventReceived(new CommandNewDesiredPosition(this, 100, 100, 0));
        //super.getWatchdog().feed();
    }
    public void operatorControl() 
    { 
        fireEvent(new EventTeleop(this)); 
//        if (!logWriter.isOpen()) logWriter.newLog();
//        controllerAutonomous.stop();
//        compressor.start();
//        super.getWatchdog().feed();
    }


    /*
     * This function starts up controllers and adds them to threads
     */
    private void initControllers() {
        controllerDrive = new ControllerDrive(5);
        controllerGamePad = new ControllerGamePad(5);
        controllerShooter = new ControllerShooter(5, this);
        controllerArm = new ControllerArm(5);
        


        threads.addElement(new Thread(controllerGamePad));
        threads.addElement(new Thread(controllerShooter));
        threads.addElement(new Thread(controllerArm));
        threads.addElement(new Thread(controllerDrive));
        threads.addElement(new Thread(controllerAutonomous));
        
        //SmartDashboard.putString("Logger File Name Base", "CHANGEME");
    }
    
    /*
     * The following functions set up systems 
     * by defining what they listen to or interact with
     */
    
    private void initSystem(WiredCatsSystem w){
        this.addEventListener(w);
        controllerGamePad.addEventListener(w);
        threads.addElement(new Thread(w));
    }

    private void initDrive() {
        systemDrive = new SystemDrive();
        controllerDrive.addEventListener(systemDrive);
        initSystem(systemDrive);
    }

    private void initShooter() {
        systemShooter = new SystemShooter();
        controllerShooter.addEventListener(systemShooter);
        initSystem(systemShooter);
    }
    
    private void initIntake(){
        systemIntake = new SystemIntake();
        initSystem(systemIntake);
    }
    
    private void initArm()
    {
        systemArm = new SystemArm(controllerShooter);
        controllerArm.addEventListener(systemArm);
        initSystem(systemArm);
    }
    
    private void initLogger()
    {
        logWriter = new WiredCatsLogger();
        threads.addElement(new Thread(logWriter));
        logWriter.addSystems(systemDrive, systemIntake, systemShooter, systemArm);
        logWriter.addControllers(controllerArm, controllerShooter, controllerDrive);
    }
    
    private void initAutonomous()
    {
        controllerAutonomous = new ControllerAutonomous(5);
        controllerAutonomous.addSystems(systemDrive, systemIntake, systemShooter, systemArm);
    }

    /**
     * Since this class also acts like a controller, 
     * we need to be able to add Listeners and fire events.
     * Thus, we repeat some functionality from the WiredCatsController class
     */
    private synchronized void addEventListener(WiredCatsEventListener l) { listeners.addElement(l); }
    private synchronized void removeEventListener(WiredCatsEventListener l) { listeners.removeElement(l); }

    private synchronized void fireEvent(WiredCatsEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((WiredCatsEventListener) (listeners.elementAt(i))).eventReceived(e);
        }
    }
}
