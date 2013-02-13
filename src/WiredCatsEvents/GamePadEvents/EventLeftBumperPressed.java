package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventLeftBumperPressed extends EventGamePad {

    public EventLeftBumperPressed(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
