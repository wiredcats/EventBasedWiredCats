package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * Pushing down the right joystick is a button. This is it.
 * 
 * @author Robotics
 */

public class EventRightPushDownReleased extends EventGamePad {

    public EventRightPushDownReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
