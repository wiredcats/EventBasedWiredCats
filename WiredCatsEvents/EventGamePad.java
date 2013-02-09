package WiredCatsEvents;

/**
 * Base case for a Gamepad Event. 
 * Contains both source and which controller the button press was from
 *
 * @author Robotics
 */
public class EventGamePad extends WiredCatsEvent {

    public static final byte PRIMARY_CONTROLLER = 1;
    public static final byte SECONDARY_CONTROLLER = 2;
    
    private byte controllerID;

    public EventGamePad(Object source, byte controllerID) {
        super(source);
        this.controllerID = controllerID;
    }

    public boolean isController1() {
        return (controllerID == PRIMARY_CONTROLLER ? true : false);
    }
}
