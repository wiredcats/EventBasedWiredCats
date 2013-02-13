package WiredCatsEvents;

/**
 * Basic form for an Event State Change.
 * Event listeners don't need any extraneous information.
 * They just need to know what mode are they switching to / from
 * 
 * @author Robotics
 */
public class EventStateChange extends WiredCatsEvent {

    public EventStateChange(Object source) {
        super(source);
    }
}
