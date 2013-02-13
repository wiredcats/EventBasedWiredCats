package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonBPressed extends EventGamePad {

    public EventButtonBPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
