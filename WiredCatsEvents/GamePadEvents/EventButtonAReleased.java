package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin.
 *
 * @author Robotics
 */

public class EventButtonAReleased extends EventGamePad {

    public EventButtonAReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
