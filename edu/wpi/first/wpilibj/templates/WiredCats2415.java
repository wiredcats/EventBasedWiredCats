/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import WiredCatsControllers.ControllerGamePad;
import WiredCatsControllers.ControllerShooterEncoders;
import WiredCatsEvents.EventAutonomous;
import WiredCatsEvents.EventTeleop;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.WiredCatsEventListener;
import WiredCatsSystems.SystemDrive;
import WiredCatsSystems.SystemShooter;
import Util2415.CSVReader;


import edu.wpi.first.wpilibj.SimpleRobot;
import java.util.Vector;

public class WiredCats2415 extends SimpleRobot
{
    
    Vector listeners = new Vector(5);
    
    //fires events to the systems to tell them state changes
    //such as AUTONOMOUS, or DISABLED
    
    
    ControllerGamePad controllerGamePad;
    ControllerShooterEncoders controllerShooterEncoders;
    
    
    SystemDrive systemDrive;
    SystemShooter systemShooter;
    
    //the robot class itself is a
    //controller, because the
    //systems have to know that 
    //the states have chagned,
    //and we could just be state based
    //and give them all a pointer to this,
    //but I like keeping consistent and 
    //using our event system.
    
    Thread gamepadThread;
    Thread driveThread;
    Thread sduThread;
    Thread shootEncoderThread;
    Thread shooterThread;
    
    public WiredCats2415()
    {
        initControllers();
        //initDrive();
        initShooter();
        
        //driveThread.start();
        gamepadThread.start();
        shooterThread.start();
        shootEncoderThread.start();
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() 
    {
        //fire autonomous event.
        fireEvent(new EventAutonomous(this));
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() 
    {
        //fires teleop event.
        fireEvent(new EventTeleop(this));
        
    }
    
    public void disabled()
    {

    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() 
    {
        //I don't think a test event is necessary, but I cant be certain; we'll have to 
        //test that.
    }
    
    private void initControllers()
    {
        //starts up the systems and controllers.
        controllerGamePad = new ControllerGamePad(5, this);
        controllerShooterEncoders = new ControllerShooterEncoders(5, this);
        
        gamepadThread = new Thread(controllerGamePad);
        shootEncoderThread = new Thread(controllerShooterEncoders);
    }
    
    private void initDrive()
    {
        //setting up systemDrive, adding all the things it listens to/ interacts with.
        systemDrive = new SystemDrive();
        controllerGamePad.addEventListener(systemDrive);
        this.addEventListener(systemDrive);
        //creates drive thread.
        driveThread = new Thread(systemDrive);
    }
    
    private void initShooter()
    {
        //setting up systemShooter
        systemShooter = new SystemShooter();
        controllerGamePad.addEventListener(systemShooter);
        controllerShooterEncoders.addEventListener(systemShooter);
        this.addEventListener(systemShooter);
        shooterThread = new Thread(systemShooter);
    }
    
    private synchronized void addEventListener(WiredCatsEventListener l)
    {
        listeners.addElement(l);
    }
    
    private synchronized void removeEventListener(WiredCatsEventListener l)
    {
        listeners.removeElement(l);
    }
    
    private synchronized void fireEvent(WiredCatsEvent e)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            ((WiredCatsEventListener)(listeners.elementAt(i))).eventReceived(e);
        }
    }
}
