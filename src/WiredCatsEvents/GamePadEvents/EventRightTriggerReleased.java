package WiredCatsEvents.GamePadEvents;

import WiredCatsEvents.EventGamePad;

/**
 * Triggers are buttons, but slightly tricky.
 * NOTE! The left and right trigger are actually both on the same axis.
 * Left trigger pressed down = [-1,0) while right trigger is (0,1]
 * If the axis value = 0, then neither one is pressed down
 * 
 * This means that 
 * 1) we can read of an axis value off of it
 * 2) You can not press both the left and right trigger at the same time.
 * 
 * We usually do not want to read an axis value, but just be aware of this.
 * Inform your drivers
 * 
 * @author Robotics
 */

public class EventRightTriggerReleased extends EventGamePad {

    public EventRightTriggerReleased(Object source, byte controllerID) {
        super(source, controllerID);
    }
}
