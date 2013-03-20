package Util2415;

import java.util.Vector;

/**
 * Queue used to organize events in systems
 *
 * @author Robotics
 */
public class BlockingQueue {

    private Vector queue; //events are held here

    public BlockingQueue(int limit) {
        queue = new Vector(limit);
    }

    public synchronized void put(Object o) throws InterruptedException {
        queue.addElement(o);
    }

    /**
     * Takes the next in line of the Queue.
     */
    public synchronized Object take() throws InterruptedException {
        Object temp;

        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                
            }
        }

        temp = queue.firstElement();
        queue.removeElementAt(0);

        return temp;
    }
    
    public int getSize() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
