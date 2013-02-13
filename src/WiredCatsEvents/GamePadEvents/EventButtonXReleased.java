package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonXReleased extends EventGamePad {

    public EventButtonXReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
