package WiredCatsSystems;

import WiredCatsEvents.StateChangeEvents.*;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.WiredCatsEventListener;

import Util2415.BlockingQueue;
import WiredCatsEvents.AutonomousCommands.AutonomousCommand;

/**
 * The RobotSystem is an abstract class that is used to create the Systems of
 * the robot. The RobotSystem listens to events sent to it by subclasses of the
 * RobotController.
 *
 * @author Bruce Crane
 */
public abstract class WiredCatsSystem implements Runnable, WiredCatsEventListener {

    BlockingQueue events; //the local queue for blocking
    public static final byte STATE_DISABLED = 0;
    public static final byte STATE_AUTONOMOUS = 1;
    public static final byte STATE_TELEOP = 2;
    protected byte state = -1;
    
    public static final byte AUTONOMOUS_WAITING = 0;
    public static final byte AUTONOMOUS_ATTEMPTING = 0;
    public static final byte AUTONOMOUS_COMPLETED = 0;
    protected byte autonomous_state = -1;
    

    public WiredCatsSystem() {
        events = new BlockingQueue(5);
    }
    
    /**
     * This run method is currently calibrated for a blocking queue structure
     * Please rewrite this if you want to use a different style of event handling
     */
    public void run() {
        while (true) {
            try {
                if (!events.isEmpty()) {

                    WiredCatsEvent event = (WiredCatsEvent) (events.take());

                    if (event instanceof EventStateChange) {
                        eventStateChangeReceived((EventStateChange) event);
                        }
                    
                        if(state == STATE_DISABLED) doDisabled(event);
                        else
                        {
                           doEnabled(event);
                           if(state == STATE_AUTONOMOUS) doAutonomousSpecific(event);
                           else if(state == STATE_TELEOP) doTeleopSpecific(event);  
                        }
                                      
                    }
                update();
                Thread.sleep(1);        
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public abstract void doDisabled(WiredCatsEvent event);
    public abstract void doAutonomousSpecific(WiredCatsEvent event);
    public abstract void doTeleopSpecific(WiredCatsEvent event);
    public abstract void doEnabled(WiredCatsEvent event);
    
    public final byte autonomous_AtDesiredNode()
    {
        if (autonomous_state == AUTONOMOUS_COMPLETED)
        {
            autonomous_state = AUTONOMOUS_WAITING;
            return AUTONOMOUS_COMPLETED;
        }
        return autonomous_state;
    }

    public void update() {}
    
    /**
     * An abstract method that is called by any RobotController that this system
     * is subscribed to. 
     * It takes in a RobotEvent and determines how to act
     * (single thread, multi-thread, blocking queue) 
     * 
     * Default mode written here is blocking queue. Can be overwritten for other methods if desired
     *
     * @param re the robot event being handled.
     */
    public void eventReceived(WiredCatsEvent re) {
        try {
            events.put((Object) re);
        } catch (InterruptedException iE) {
            
        } catch (NullPointerException npE) {
            
        }
    }

    /**
     * A method that is called in the eventReceived(RobotEvent re) method, it
     * handles the case that the event inherits from the EventStateChange class.
     * This event is final because there is no reason anyone should be changing
     * it since it is the same for each System.
     *
     * YOU MUST CALL THIS METHOD IN RUN METHOD!!!
     *
     * @param esc
     */
    protected final void eventStateChangeReceived(EventStateChange esc) {
        if (esc instanceof EventDisabled) {
            state = STATE_DISABLED;
            return;
        }

        if (esc instanceof EventAutonomous) {
            state = STATE_AUTONOMOUS;
            return;
        } 
        
        if (esc instanceof EventTeleop) {
            state = STATE_TELEOP;
            return;
        }
    }
}
