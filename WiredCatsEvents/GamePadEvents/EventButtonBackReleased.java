package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonBackReleased extends EventGamePad {

    public EventButtonBackReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
