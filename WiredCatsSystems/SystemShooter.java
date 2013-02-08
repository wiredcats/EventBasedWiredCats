/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventButtonAPressed;
import WiredCatsEvents.EventButtonAReleased;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.EventOverDesiredSpeed;
import WiredCatsEvents.EventRightBumperPressed;
import WiredCatsEvents.EventRightBumperReleased;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.EventUnderDesiredSpeed;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Robotics
 */
public class SystemShooter extends WiredCatsSystem
{

    private Victor talon1; //the victors that this shoots with.
    private Victor talon2;
    private Solenoid cockOn;
    private Solenoid cockOff;
    
    private Solenoid fireOn;
    private Solenoid fireOff;
    
    //guard solenoids.
    
    Timer cockTimer = new Timer();
    Timer fireTimer = new Timer();
    Timer loopTimer = new Timer();
    
    private boolean autoShoot;
    
    public SystemShooter()
    {
        super();
        talon1 = new Victor(8);
        talon2 = new Victor(7);
        cockOn = new Solenoid(4);
        cockOff = new Solenoid(3);
        fireOn = new Solenoid(6);
        fireOff = new Solenoid(5);
        autoShoot = false;
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
    
    private void gamepadEventListener(EventGamePad eventgp)
    {
        if (eventgp instanceof EventRightBumperPressed && eventgp.isController1())
        {
            System.out.println("turn on autoshoot");
            autoShoot = true;
               fireOn.set(true);
               fireOff.set(false);
               cockOn.set(false);
               cockOff.set(true);
               
               loopTimer.stop();
               loopTimer.reset();
               cockTimer.stop();
               cockTimer.reset();
               
               fireTimer.start();
            
        }
        else if (eventgp instanceof EventRightBumperReleased && eventgp.isController1())
        {
            System.out.println("turn off autoshoot");
            autoShoot = false;
            
            
        }
    }
    

    public void run() 
    {
        cockOn.set(true);
        cockOff.set(false);
        fireOn.set(false);
        fireOff.set(true);
        
        while (true)
        {
           
           if (autoShoot && enabled)
           {
                
               if(loopTimer.get() >= 0.5) 
                {
                    fireOn.set(true);
                    fireOff.set(false);
                    cockOn.set(false);
                    cockOff.set(true);
                    
                    loopTimer.stop();
                    loopTimer.reset();
                    fireTimer.start();
                    System.out.println("looped");
                }
                

                if (cockTimer.get() >= 0.1) 
                {
                    cockOn.set(false);
                    cockOff.set(true);
                    
                    cockTimer.stop();
                    cockTimer.reset();
                    loopTimer.start();
                    System.out.println("-cocked");
                }
                
                if (fireTimer.get() >= 0.3) 
                {
                    fireOn.set(false);
                    fireOff.set(true);
                    cockOn.set(true);
                    cockOff.set(false);

                    fireTimer.stop();
                    fireTimer.reset();
                    cockTimer.start();
                    
                    
                    
                    System.out.println("--fired");
                }

           }
            
            
           if (!events.isEmpty())
           {
                try 
                {
                    WiredCatsEvent event = (WiredCatsEvent)events.take();
                    
                    
                    if (event instanceof EventStateChange)
                    {
                        
                        super.eventStateChangeReceived((EventStateChange)event);
                    }
                    else if (!enabled || state != WiredCatsSystem.STATE_TELEOP)
                    {
                       //then we shouldn't be doing anything.
                        continue;
                    }
                    else if (event instanceof EventGamePad)
                    {
                        gamepadEventListener((EventGamePad)event);
                    }
                    else if (event instanceof EventOverDesiredSpeed)
                    {
//                        System.out.println(">>>OVer teh desired speed");
                        if (((EventOverDesiredSpeed)event).encoderID == EventOverDesiredSpeed.ENCODER_1)
                        {

                            talon1.set(0.0);
                        }
                        else //then it's got to be the other talon.
                        {
                            talon2.set(0.0);
                        } 
                    }
                    else if (event instanceof EventUnderDesiredSpeed)
                    {
//                       System.out.println(">>>under the desired speed");
                       if (((EventUnderDesiredSpeed)event).encoderID == EventUnderDesiredSpeed.ENCODER_1)
                        {
                            //System.out.println("BANG 2");
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
