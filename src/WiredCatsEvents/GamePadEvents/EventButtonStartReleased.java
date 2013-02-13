package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonStartReleased extends EventGamePad {

    public EventButtonStartReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
