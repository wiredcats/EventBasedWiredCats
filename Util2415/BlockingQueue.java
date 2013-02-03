/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util2415;

import java.util.Vector;

/**
 *
 * @author Robotics
 */
public class BlockingQueue 
{
    
    
    private Vector queue;
    
    public BlockingQueue(int limit)
    {
        queue = new Vector(limit);
        //events are sent here.
    }
    
    public synchronized void put(Object o) throws InterruptedException
    {
        queue.addElement(o);
    }
    
    /**
     * Takes the next in line
     * of the Queue.
     */
    public synchronized Object take() throws InterruptedException
    {
       Object temp; 
       
       while (queue.isEmpty())
       {
           try
           {
               wait();
           } catch (InterruptedException e) {}
       }
       
       temp = queue.firstElement();
       queue.removeElementAt(0);
        
        return temp; 
    }
    
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }
    
}
