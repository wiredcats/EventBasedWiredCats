package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * Pushing down the left joystick is a button. This is it.
 * 
 * @author Robotics
 */

public class EventLeftPushDownReleased extends EventGamePad {

    public EventLeftPushDownReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
