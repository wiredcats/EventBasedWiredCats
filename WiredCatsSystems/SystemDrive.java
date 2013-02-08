/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventAutonomous;
import WiredCatsEvents.EventDisabled;
import WiredCatsEvents.EventEnabled;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.EventLeftYAxis;
import WiredCatsEvents.EventRightYAxis;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.EventTeleop;
import WiredCatsEvents.WiredCatsEvent;
import Util2415.CSVReader;
import edu.wpi.first.wpilibj.Victor;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Talon;

/**
 * The system that controls everything
 * drive train.
 * @author BruceCrane
 */
public class SystemDrive extends WiredCatsSystem
{
    //victors and such.
    private Talon left1;
    private Talon left2;
    private Talon left3;
    
    private Talon right1;
    private Talon right2;
    private Talon right3;
    
    public SystemDrive()
    {
        super();
        
    left1 = new Talon(4);
    left2 = new Talon(5);
    left3 = new Talon(6);
    
    right1 = new Talon(1);
    right2 = new Talon(2);
    right3 = new Talon(3);
    
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
                
                WiredCatsEvent event = (WiredCatsEvent)(events.take());
                
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
//                         System.out.println("setting left victors to: " + -1*((EventLeftYAxis)event).y + "%");
//                         left1.set(setVictorValues(-1*((EventLeftYAxis)event).y));
//                         left2.set(setVictorValues(-1*((EventLeftYAxis)event).y));
//                         left3.set(setVictorValues(-1*((EventLeftYAxis)event).y));
//                            System.out.println("!!!!!!!!!!!!!!!");
                           left1.set( ((EventLeftYAxis)event).y);
                           left2.set( ((EventLeftYAxis)event).y);
                           left3.set( ((EventLeftYAxis)event).y);
                           System.out.println( ((EventLeftYAxis)event).y);
                        }
                    }
                    else if (event instanceof EventRightYAxis)
                    {
                       if (((EventGamePad)event).isController1())
                      {
//                          //set victors to value.
//                          System.out.println("setting right victors to: " + setVictorValues(((EventRightYAxis)event).y) + "%");
//                          right1.set(-1 * setVictorValues(((EventRightYAxis)event).y));
//                          right2.set(-1 * setVictorValues(((EventRightYAxis)event).y));
//                          right3.set(-1 * setVictorValues(((EventRightYAxis)event).y));
                          right1.set( ((EventRightYAxis)event).y);
                          right2.set( ((EventRightYAxis)event).y);
                          right3.set( ((EventRightYAxis)event).y);
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
    
    
    public void eventReceived(WiredCatsEvent re) 
    {
        try
        {
            events.put((Object)re);
        }
        catch (InterruptedException iE) {}
        catch (NullPointerException npE) {}
    }
    
    
    /**
     * this figures out all the things that are needed
     * to consider when setting the victor value. This functions
     * domain is 1-100, since it should be a percentage,
     * and the percentage shouldn't go over 100, because victors
     * can't do that. (Or Talons, because we're using those now). 
     * @param value
     * @return 
     */
    private double setVictorValues(int value)
    {
        double MOTOR_DEADBAND = .08;
        
        double newValue = (value - MOTOR_DEADBAND)/(1-MOTOR_DEADBAND) + MOTOR_DEADBAND;
        
        double controlExponent = 3.0; 
        
        double outputValue = MathUtils.pow(newValue, controlExponent);
        
        return outputValue;
    }

}
