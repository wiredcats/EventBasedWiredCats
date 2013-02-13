package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin.
 *
 * @author Robotics
 */

public class EventRightBumperReleased extends EventGamePad {

    public EventRightBumperReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
