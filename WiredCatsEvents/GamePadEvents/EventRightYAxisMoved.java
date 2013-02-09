package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * What it says on the tin. 
 * Except the tin is slightly shinier because we have y values that are stored
 *
 * @author Robotics
 */
public class EventRightYAxisMoved extends EventGamePad {

    public double y;

    public EventRightYAxisMoved(Object source, byte controllerID, double y) {
        super(source, controllerID);
        this.y = y;
    }
}
