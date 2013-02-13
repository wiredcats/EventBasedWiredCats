package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * Pushing down the right joystick is a button. This is it.
 * 
 * @author Robotics
 */

public class EventRightPushDownPressed extends EventGamePad {

    public EventRightPushDownPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
