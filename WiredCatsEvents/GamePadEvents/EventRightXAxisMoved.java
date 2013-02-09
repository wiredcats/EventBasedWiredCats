package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * Except the tin is slightly shinier because we have x values that are stored
 * 
 * @author Robotics
 */

public class EventRightXAxisMoved extends EventGamePad {

    public double x; 

    public EventRightXAxisMoved(Object source, byte controllerID, double x) {
        super(source, controllerID);
        this.x = x;
    }
}
