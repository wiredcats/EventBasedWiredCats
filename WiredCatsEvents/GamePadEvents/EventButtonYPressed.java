package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonYPressed extends EventGamePad {

    public EventButtonYPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
