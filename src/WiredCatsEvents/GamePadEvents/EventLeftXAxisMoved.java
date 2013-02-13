package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * Except the tin is slightly shinier because we have x values that are stored
 * 
 * @author Robotics
 */

public class EventLeftXAxisMoved extends EventGamePad {

    public double x;

    public EventLeftXAxisMoved(Object source, byte controllerID, double x) {
        super(source, controllerID);
        this.x = x;
    }
}
