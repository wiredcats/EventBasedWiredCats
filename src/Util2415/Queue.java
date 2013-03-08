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
public class Queue 
{
    private Vector queue;
    private int maxLength;
    
    
    public Queue(int maxLength)
    {
        this.maxLength = maxLength;
        queue = new Vector();
    }

    public void put(double d)
    {
        if (queue.size() == maxLength)
        {
            take();
        }
        queue.addElement(new OurDouble(d));
    }

    /**
     * Takes the next in line of the Queue.
     */
    public Object take()
    {
        OurDouble temp;

        temp = (OurDouble)queue.firstElement();
        queue.removeElementAt(0);

        return temp;
    }
    
    public double sum()
    {
        double sum = 0;
        
        for (int i = 0; i < queue.size(); i++)
        {
            sum += ((OurDouble)queue.elementAt(i)).d;
        }
        
        return sum;
    }
    
    public void clear()
    {
        queue.removeAllElements();
    }

    public boolean isEmpty() 
    {
        return queue.isEmpty();
    }
    
    private class OurDouble {
        public double d;

        public OurDouble(double d1) {
            this.d = d1;
        }
    }
}
