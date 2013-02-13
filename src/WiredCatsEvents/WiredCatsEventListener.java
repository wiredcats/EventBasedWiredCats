package WiredCatsEvents;

/**
 * Interface for event listeners. 
 * Requires event listeners to be able to deal with different events
 *
 * @author Robotics
 */
public interface WiredCatsEventListener {
    
    public void eventReceived(WiredCatsEvent re);
    
}
