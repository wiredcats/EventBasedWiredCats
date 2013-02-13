package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonStartPressed extends EventGamePad {

    public EventButtonStartPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
