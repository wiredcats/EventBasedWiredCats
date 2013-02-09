package WiredCatsControllers;

import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.WiredCatsEventListener;
import edu.wpi.first.wpilibj.templates.WiredCats2415;
import java.util.Vector;

/**
 * Base case of our controller design.
 * Controllers are "event-firers". They have a list of listeners and send events to them
 * 
 * @author Robotics
 */
public abstract class WiredCatsController implements Runnable {

    protected Vector listeners;
    protected WiredCats2415 robot; //pointer to the main robot class.

    public WiredCatsController(int limit, WiredCats2415 robot) {
        listeners = new Vector(limit);
        this.robot = robot;
    }
    
    public synchronized void addEventListener(WiredCatsEventListener l) { listeners.addElement(l); }
    public synchronized void removeEventListener(WiredCatsEventListener l) { listeners.removeElement(l);}

    protected synchronized void fireEvent(WiredCatsEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            ((WiredCatsEventListener) (listeners.elementAt(i))).eventReceived(e);
        }
    }
}
