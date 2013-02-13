package WiredCatsEvents.StateChangeEvents;

import WiredCatsEvents.EventStateChange;

/**
 * Announcing that we are in autonomous.
 * 
 * @author Robotics
 */

public class EventAutonomous extends EventStateChange {

    public EventAutonomous(Object source) {
        super(source);
    }
}
