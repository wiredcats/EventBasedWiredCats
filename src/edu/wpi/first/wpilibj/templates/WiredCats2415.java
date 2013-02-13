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

import Util2415.WiredCatsEventLogger;
import WiredCatsControllers.*;
import WiredCatsEvents.WiredCatsEventListener;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.StateChangeEvents.*;
import WiredCatsSystems.*;

//Specific utilities
import Util2415.TXTReader;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Vector; 


/**
 * Main Robot Class.
 * This is based on the SimpleRobot template and starts all of the other systems.
 * Also acts as a WiredCatsController as it fires state change events such as AUTNOMOUS or DISABLED
 * 
 * @author Bruce Crane
 */

public class WiredCats2415 extends SimpleRobot 
{

    public static TXTReader textReader;
    public static WiredCatsEventLogger logWriter;
    
    private Vector listeners = new Vector(5);
    private Vector threads = new Vector(5);
    
    private ControllerGamePad controllerGamePad;
    private ControllerShooter controllerShooter;
    private ControllerIntake controllerIntake;
    private ControllerDrive controllerDrive;
    
    private SystemDrive systemDrive;
    private SystemShooter systemShooter;
    private SystemIntake systemIntake;
    
    static
    {
        textReader = new TXTReader();
        textReader.getFromFile("CheesyConfig.txt");
        logWriter = new WiredCatsEventLogger();
//        logWriter.newLog();
    }
    
    public WiredCats2415() {
        initControllers();
//        initDrive();
        //initShooter();
        //initIntake();
        
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
        if (logWriter.isOpen()) logWriter.close();
    }
    public void autonomous() 
    { 
        //TODO make 100 dollars.
        fireEvent(new EventAutonomous(this)); 
    }
    public void operatorControl() 
    { 
        fireEvent(new EventTeleop(this)); 
        if (!logWriter.isOpen()) logWriter.newLog();
    }


    /*
     * This function starts up controllers and adds them to threads
     */
    private void initControllers() {
        controllerGamePad = new ControllerGamePad(5);
        controllerShooter = new ControllerShooter(5);
        controllerIntake = new ControllerIntake(5);
        controllerDrive = new ControllerDrive(5);

        threads.addElement(new Thread(controllerGamePad));
        threads.addElement(new Thread(controllerShooter));
        threads.addElement(new Thread(controllerIntake));
        
        logWriter.addControllers(controllerIntake, controllerShooter, controllerDrive);
        SmartDashboard.putString("Logger File Name Base", "CHANGEME");
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
        initSystem(systemDrive);
    }

    private void initShooter() {
        //setting up systemShooter
        systemShooter = new SystemShooter();
        controllerShooter.addEventListener(systemShooter);
        initSystem(systemShooter);
    }
    
    private void initIntake()
    {
        systemIntake = new SystemIntake();
        controllerIntake.addEventListener(systemIntake);
        initSystem(systemIntake);
    }

    /**
     * Since this class also acts like a controller, 
     * we need to be able to add Listeners and fire events.
     * Thus, we repeat some functionality from the WiredCatsController class
     */
    private synchronized void addEventListener(WiredCatsEventListener l) { listeners.addElement(l); }
    private synchronized void removeEventListener(WiredCatsEventListener l) { listeners.removeElement(l); }

    private synchronized void fireEvent(WiredCatsEvent e) 
    {
        for (int i = 0; i < listeners.size(); i++) {
            ((WiredCatsEventListener) (listeners.elementAt(i))).eventReceived(e);
        }
    }
}
