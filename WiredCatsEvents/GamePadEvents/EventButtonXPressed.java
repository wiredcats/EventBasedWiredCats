package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventButtonXPressed extends EventGamePad {

    public EventButtonXPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
