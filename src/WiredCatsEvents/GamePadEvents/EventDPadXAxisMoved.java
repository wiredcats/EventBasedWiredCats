package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * Except the tin is slightly shinier because we have x values that are stored
 * 
 * @author Robotics
 */

public class EventDPadXAxisMoved extends EventGamePad {

    public double x; //it's a percentage, -100% to 100%

    public EventDPadXAxisMoved(Object source, byte controllerID, double x) {
        super(source, controllerID);
        this.x = x;
    }
}
