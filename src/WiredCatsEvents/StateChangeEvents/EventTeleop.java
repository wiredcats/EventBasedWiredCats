package WiredCatsEvents.StateChangeEvents;

import WiredCatsEvents.EventStateChange;

/**
 * Announcing that we are in Teleop.
 * 
 * @author Robotics
 */

public class EventTeleop extends EventStateChange {

    public EventTeleop(Object source) {
        super(source);
    }
}
