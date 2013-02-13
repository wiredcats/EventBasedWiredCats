package WiredCatsEvents;

/**
 * Base case for an Event.
 * At its most fundamental, fires an event which keeps track of who fired it in the first place
 * 
 * @author Robotics
 */

public abstract class WiredCatsEvent {

    private Object source;

    public WiredCatsEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
    
    public String toString()
    {
        return this.getClass().getName();
    }
}
