package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * 
 * @author Robotics
 */

public class EventLeftBumperReleased extends EventGamePad {

    public EventLeftBumperReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
