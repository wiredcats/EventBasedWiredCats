/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Events.RobotEvent;
import Events.RobotEventListener;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.templates.Robot;
import java.util.Vector;

/**
 *
 * @author Robotics
 */
public abstract class RobotController implements Runnable
{
   
    protected Vector listeners;
    
    protected Robot robot; //pointer to the main robot class.
    
    public RobotController(int limit, Robot robot)
    {
        listeners = new Vector(limit);
        this.robot = robot;
    }
    
    public synchronized void addEventListener(RobotEventListener l)
    {
        listeners.addElement(l);
    }
    
    public synchronized void removeEventListener(RobotEventListener l)
    {
        listeners.removeElement(l);
    }
    
    protected synchronized void fireEvent(RobotEvent e)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            ((RobotEventListener)(listeners.elementAt(i))).eventReceived(e);
        }
    }
    
}
