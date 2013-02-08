/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventAutonomous;
import WiredCatsEvents.EventDisabled;
import WiredCatsEvents.EventEnabled;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.EventTeleop;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.WiredCatsEventListener;
import Util2415.BlockingQueue;

/**
 * The RobotSystem is an abstract class that is used
 * to create the Systems of the robot. The RobotSystem
 * listens to events sent to it by subclasses of the RobotController.
 * @author Bruce Crane
 */
public abstract class WiredCatsSystem implements Runnable, WiredCatsEventListener
{
    
    //should listen to Robot, derp.
    
    BlockingQueue events; //the local queue for blocking.

        
    public static final byte STATE_AUTONOMOUS = 0;
    public static final byte STATE_TELEOP = 1;
    
    protected boolean enabled = false;
    protected byte state = -1;
    
    public WiredCatsSystem()
    {
        events = new BlockingQueue(5);
    }
    
    /**
     * An abstract method that is called by any RobotController that this
     * system is subscribed to. It takes in a RobotEvent
     * and determines how to act. There are several different
     * ways to respond to events (single threaded, blocking queue,
     * multi-threaded) but throughout the 2013 code we found
     * that the blocking queue was the best.
     * @param re the robot event being handled.
     */
    public abstract void eventReceived(WiredCatsEvent re);
    
    /**
     * A method that is called in the eventReceived(RobotEvent re) method,
     * it handles the case that the event inherits from the EventStateChange class.
     * This event is final because there is no reason anyone should be changing it
     * since it is the same for each System. You must call this method
     * in the eventReceived(RobotEvent re) method, or handle it yourself 
     * (which I don't recommend since this is already made).
     * @param esc 
     */
    protected final void eventStateChangeReceived(EventStateChange esc)
    {
                    if (esc instanceof EventAutonomous)
                    {
                        state = STATE_AUTONOMOUS;
                    }
                    else if (esc instanceof EventTeleop)
                    {
                        state = STATE_TELEOP;
                    }
                    
                    if (esc instanceof EventDisabled)
                    {
                        enabled = false;
                    }
                    else if (esc instanceof EventEnabled)
                    {
                        enabled = true;
                    }
    }
    
    
}
