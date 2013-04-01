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

import Util2415.TXTWriter;
import Util2415.AutonomousReader;
//import Util2415.WiredCatsLogger;
import WiredCatsControllers.*;
import WiredCatsEvents.WiredCatsEventListener;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.StateChangeEvents.*;
import WiredCatsSystems.*;

//Specific utilities
import Util2415.TXTReader;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SimpleRobot;
import java.util.Vector; 


/**
 * Main Robot Class.
 * This is based on the SimpleRobot template and starts all of the other systems.
 * Also acts as a WiredCatsController as it fires state change events such as AUTONOMOUS or DISABLED
 * 
 * @author Bruce Crane
 */

public class WiredCats2415 extends SimpleRobot {
    public static final TXTReader textReader;
    static {
        textReader = new TXTReader();
        textReader.getFromFile("WiredCatsConfig.txt");
    }
    
//    public WiredCatsLogger logWriter;
    
    public TXTWriter textWriter;
    
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
    private SystemHang systemHang;
    
    private Compressor compressor;
    
    public WiredCats2415() {
        textWriter = new TXTWriter();
        
        compressor = new Compressor(5, 8);
        
        textReader.pushToSmartDashboard();
        
        initControllers();
        initDrive();
        initShooter();
        initIntake();
        initArm();
        initAutonomous();
        initHang();
//        initLogger();
        
        for (int i = 0; i < threads.size(); i++) {
            ((Thread) (threads.elementAt(i))).start();
        }
    }

    /**
    * The following functions work by sending an event to all other listeners
    * This tells them what mode the robot is in now
    */
    
    public void disabled() { 
        fireEvent(new EventDisabled(this)); 
        controllerAutonomous.stop();
        textWriter.update();
        super.getWatchdog().feed();
    }
    
    public void autonomous() { 
        System.out.println(">>AUTONOMOUS<<");
        //TODO make 100 dollars.
        controllerDrive.resetEncoders();
        fireEvent(new EventAutonomous(this)); 
        
        AutonomousReader autoReader = new AutonomousReader();
        try
        {
              Vector nodes = autoReader.readLog("autonomous.txt"); //IMPORTANT!!! UPLOAD THIS FILE TO ROBOT 
                   
        
              for (int i = 0; i < nodes.size(); i++) {
                 System.out.println(nodes.elementAt(i));
              }
        
             System.out.println("length: " + nodes.size());
             controllerAutonomous.setNodes(nodes);
             controllerAutonomous.begin();
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
            System.out.println("Autonomous file format wrong.");
            controllerAutonomous.stop();
        }

    }
    
    public void operatorControl() { 
        compressor.start();
        fireEvent(new EventTeleop(this)); 
        controllerAutonomous.stop();
        super.getWatchdog().feed();
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
        controllerArm.addEventListener(systemShooter);
        initSystem(systemShooter);
    }
    
    private void initIntake(){
        systemIntake = new SystemIntake();
        initSystem(systemIntake);
    }
    
    private void initArm() {
        systemArm = new SystemArm(controllerShooter);
        controllerArm.addEventListener(systemArm);
        initSystem(systemArm);
    }

    private void initHang() {
        systemHang = new SystemHang();
        initSystem(systemHang);
    }
    
//    private void initLogger()
//    {
//        logWriter = new WiredCatsLogger();
//        threads.addElement(new Thread(logWriter));
//        logWriter.addSystems(systemDrive, systemIntake, systemShooter, systemArm);
//        logWriter.addControllers(controllerArm, controllerShooter, controllerDrive);
//    }
    
    private void initAutonomous() {
        controllerAutonomous = new ControllerAutonomous(5);
        controllerAutonomous.addSystems(systemDrive, systemIntake, systemShooter, systemArm);
        threads.addElement(new Thread(controllerAutonomous));
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
