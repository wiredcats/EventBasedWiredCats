package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * Pushing down the left joystick is a button. This is it.
 * 
 * @author Robotics
 */

public class EventLeftPushDownPressed extends EventGamePad {

    public EventLeftPushDownPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
