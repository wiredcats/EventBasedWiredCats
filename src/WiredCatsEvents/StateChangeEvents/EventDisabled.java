package WiredCatsEvents.StateChangeEvents;

import WiredCatsEvents.EventStateChange;

/**
 * Announcing that we are disabled.
 *
 * @author Robotics
 */

public class EventDisabled extends EventStateChange {

    public EventDisabled(Object source) {
        super(source);
    }
}
