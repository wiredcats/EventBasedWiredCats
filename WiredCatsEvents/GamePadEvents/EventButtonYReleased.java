package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonYReleased extends EventGamePad {

    public EventButtonYReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
