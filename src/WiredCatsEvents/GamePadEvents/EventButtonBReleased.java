package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonBReleased extends EventGamePad {

    public EventButtonBReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
