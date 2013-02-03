/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Systems;

import Events.EventAutonomous;
import Events.EventDisabled;
import Events.EventEnabled;
import Events.EventGamePad;
import Events.EventLeftYAxis;
import Events.EventRightYAxis;
import Events.EventStateChange;
import Events.EventTeleop;
import Events.RobotEvent;
import Util2415.CSVReader;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.templates.SmartDashboardUpdater;

/**
 * The system that controls everything
 * drive train.
 * @author BruceCrane
 */
public class SystemDrive extends RobotSystem
{
    //victors and such.
    Victor left1;
    Victor left2;
    Victor left3;
    
    Victor right1;
    Victor right2;
    Victor right3;
    
    public SystemDrive()
    {
        super();
        
    left1 = new Victor(7);
    left2 = new Victor(6);
    left3 = new Victor(5);
    
    right1 = new Victor(8);
    right2 = new Victor(9);
    right3 = new Victor(10);
    
    //dictates what values it wants;
    
    requestedValues = new String[2];
    requestedValues[0] = "TESTING_VALUE";
    requestedValues[1] = "VALUE_2";
    
    System.out.println("Drive system initialized.");
        
    }
    
    public void run()
    {
        //this is the function that is run.
        
        while (true)
        {
            try {
                if (!events.isEmpty())
                {
                    
                    System.out.println(SmartDashboardUpdater.csvreader.getValue("TESTING_VALUE"));
                
                RobotEvent event = (RobotEvent)(events.take());
                
                if (event instanceof EventStateChange)
                {
                    //then check specific events.
                    super.eventStateChangeReceived((EventStateChange)event);                   
                }
                
                if (state == STATE_TELEOP && enabled)
                {
                  //read controller input.
                    if (event instanceof EventLeftYAxis)
                    {
                      if (((EventGamePad)event).isController1())
                        {
                         //set victors to value.
                         System.out.println("setting left victors to: " + ((EventLeftYAxis)event).y + "%");
                         left1.set(((EventLeftYAxis)event).y/100);
                         left2.set(((EventLeftYAxis)event).y/100);
                         left3.set(((EventLeftYAxis)event).y/100);
                        }
                    }
                    else if (event instanceof EventRightYAxis)
                    {
                       if (((EventGamePad)event).isController1())
                      {
                          //set victors to value.
                          System.out.println("setting right victors to: " + ((EventRightYAxis)event).y + "%");
                          right1.set(-1 * ((EventRightYAxis)event).y/100);
                          right2.set(-1 * ((EventRightYAxis)event).y/100);
                          right3.set(-1 * ((EventRightYAxis)event).y/100);
                        }
                    }
                }
                //reads event
                
                
                }
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    
    public void eventReceived(RobotEvent re) 
    {
        try
        {
            events.put((Object)re);
        }
        catch (InterruptedException iE) {}
        catch (NullPointerException npE) {}
    }

}
