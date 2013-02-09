package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonBackPressed extends EventGamePad {

    public EventButtonBackPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
