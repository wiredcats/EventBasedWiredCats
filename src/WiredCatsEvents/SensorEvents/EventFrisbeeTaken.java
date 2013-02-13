/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents.SensorEvents;

import WiredCatsEvents.WiredCatsEvent;

/**
 * In the event that a Frisbee has been successfully loaded
 * upon the robot.
 * @author Bruce Crane
 */
public class EventFrisbeeTaken extends WiredCatsEvent
{
    public EventFrisbeeTaken(Object source)
    {
        super(source);
    }
}
