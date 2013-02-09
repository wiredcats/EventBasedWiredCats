package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonAPressed extends EventGamePad {

    public EventButtonAPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
