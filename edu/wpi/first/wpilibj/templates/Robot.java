/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import Controllers.ControllerGamePad;
import Events.EventAutonomous;
import Events.EventTeleop;
import Events.RobotEvent;
import Events.RobotEventListener;
import Systems.SystemDrive;
import Util2415.CSVReader;


import edu.wpi.first.wpilibj.SimpleRobot;
import java.util.Vector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SimpleRobot
{
    
    //fires events to the systems to tell them state changes
    //such as AUTONOMOUS, or DISABLED
    
    
    ControllerGamePad controllerGamePad;
    
    
    SystemDrive systemDrive;
    
    SmartDashboardUpdater smartdashboardupdater;
    
    //the robot class itself is a
    //controller, because the
    //systems have to know that 
    //the states have chagned,
    //and we could just be state based
    //and give them all a pointer to this,
    //but I like keeping consistent and 
    //using our event system.
    
    Thread gamepadthread;
    Thread drivethread;
    Thread sduThread;
    
    public Robot()
    {
        //starts up the systems and controllers.
        controllerGamePad = new ControllerGamePad(5, this);
        gamepadthread = new Thread(controllerGamePad);
        
        //creates the smart Dashboard. All file reading is done through it,
        //so this is where the filename is going to be put.
        smartdashboardupdater = new SmartDashboardUpdater("CheesyConfig.txt");
        
        //setting up systemDrive, adding all the things it listens to/ interacts with.
        systemDrive = new SystemDrive();
        controllerGamePad.addEventListener(systemDrive);
        this.addEventListener(systemDrive);
        //systems have to subscribe to the smartdashboardupdater
        //to make sure it updates their values.
        smartdashboardupdater.addSubscribedSystem(systemDrive);
        //creates drive thread.
        drivethread = new Thread(systemDrive);
        
        
        //creates the SmartDashboard thread.
        sduThread = new Thread(smartdashboardupdater);
        
        
        //CSVReader.getFromFile("CheesyConfig.txt");
//        System.out.println(CSVReader.getValue("TESTING_VALUE"));
        
        //starts the threads.
        drivethread.start();
        gamepadthread.start();
        sduThread.start();
        
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
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() 
    {
        //I don't think a test event is necessary, but I cant be certain; we'll have to 
        //test that.
    }
    
    Vector listeners = new Vector(5);
    
    private synchronized void addEventListener(RobotEventListener l)
    {
        listeners.addElement(l);
    }
    
    private synchronized void removeEventListener(RobotEventListener l)
    {
        listeners.removeElement(l);
    }
    
    private synchronized void fireEvent(RobotEvent e)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            ((RobotEventListener)(listeners.elementAt(i))).eventReceived(e);
        }
    }
}
