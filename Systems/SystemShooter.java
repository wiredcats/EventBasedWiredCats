/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Systems;

import Events.EventButtonAPressed;
import Events.EventButtonAReleased;
import Events.EventGamePad;
import Events.EventOverDesiredSpeed;
import Events.EventRightBumperPressed;
import Events.EventRightBumperReleased;
import Events.EventStateChange;
import Events.EventUnderDesiredSpeed;
import Events.RobotEvent;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Robotics
 */
public class SystemShooter extends RobotSystem
{

    private Talon talon1; //the victors that this shoots with.
    private Talon talon2;
    
    boolean shootMotorOn; //if the motors for the shooter is on.
    
    public SystemShooter()
    {
        super();
        talon1 = new Talon(8);
        talon2 = new Talon(9);
        
        shootMotorOn = false;
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
    
    private void gamepadEventListener(EventGamePad eventgp)
    {
        if (!shootMotorOn && eventgp instanceof EventButtonAPressed && eventgp.isController1())
        {
            //then we start up motor.
            shootMotorOn = true;
            System.out.println("turn on motors");
        }
        else if (shootMotorOn && eventgp instanceof EventButtonAReleased && eventgp.isController1())
        {
            shootMotorOn = false;
            System.out.println("turn off motors");
        }
       /* else if (shootMotorOn && eventgp instanceof Ev)
        {
             //only should occur when the motor is on, and the gates
            //of the pistons are down.
            //so, FIRE!! (and remember, it's naturally extended
            //so contract when you want to fire, and vice versa.
            
        }
        else if (eventgp instanceof EventButtonAReleased)
        {
            //so, FIRE!!
            
        }*/
    }
    

    public void run() 
    {
        while (true)
        {
           if (!events.isEmpty())
           {
                try 
                {
                    RobotEvent event = (RobotEvent)events.take();
                    
                    
                    if (event instanceof EventStateChange)
                    {
                        
                        super.eventStateChangeReceived((EventStateChange)event);
                    }
                    else if (!enabled || state != RobotSystem.STATE_TELEOP)
                    {
                       //then we shouldn't be doing anything.
                        continue;
                    }
                    else if (event instanceof EventGamePad)
                    {
                        gamepadEventListener((EventGamePad)event);
                    }
                    else if (shootMotorOn && event instanceof EventOverDesiredSpeed)
                    {
//                        System.out.println(">>>OVer teh desired speed");
                        if (((EventOverDesiredSpeed)event).encoderID == EventOverDesiredSpeed.ENCODER_1)
                        {
                            
                            talon1.set(0.0);
                        }
                        else
                        {
                            talon2.set(0.0);
                        } 
                    }
                    else if (event instanceof EventUnderDesiredSpeed)
                    {
//                       System.out.println(">>>under the desired speed");
                       if (((EventOverDesiredSpeed)event).encoderID == EventOverDesiredSpeed.ENCODER_1)
                        {
                           
                            talon1.set(1.0);
                        }
                       else  
                        {
                            talon2.set(1.0);
                        } 
                    }
                    
                    //by this point, we are done event handling.
                    //so now all that is done is responding to the actions that
                    //have occured.
                    
                } 
                catch (InterruptedException ex) 
                {
                    ex.printStackTrace();
                }       
           }
        }
    }
    
}
