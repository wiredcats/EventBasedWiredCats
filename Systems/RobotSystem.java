/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Systems;

import Events.EventAutonomous;
import Events.EventDisabled;
import Events.EventEnabled;
import Events.EventStateChange;
import Events.EventTeleop;
import Events.RobotEvent;
import Events.RobotEventListener;
import Util2415.BlockingQueue;

/**
 *
 * @author Robotics
 */
public abstract class RobotSystem implements Runnable, RobotEventListener
{
    
    //should listen to Robot, derp.
    
    BlockingQueue events; //the local queue for blocking.

        
    public static final byte STATE_AUTONOMOUS = 0;
    public static final byte STATE_TELEOP = 1;
    
    protected boolean enabled = false;
    protected byte state = -1;
    
    public RobotSystem()
    {
        events = new BlockingQueue(5);
    }

    public abstract void eventReceived(RobotEvent re);
    
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
    
    //the values thsi system 
    protected String[] requestedValues;
    
    /**
     *  This is solely used by the SmartDashboardUpdater.
     *  Each system subscribes to the updater, and then
     *  when the updater is updating values, it asks for
     *  the values wanted of each System, since they are the
     *  only things that actually want 
     */
    public final String[] getUpdateValues()
    {
        return requestedValues;
    }
    
    
}
