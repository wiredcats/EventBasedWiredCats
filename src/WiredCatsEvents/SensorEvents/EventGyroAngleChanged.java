/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents.SensorEvents;

import WiredCatsEvents.WiredCatsEvent;

/**
 *
 * @author Robotics
 */
public class EventGyroAngleChanged extends WiredCatsEvent
{
    
    public double angle;
 
    public EventGyroAngleChanged(Object source, double angle)
    {
        super(source);
        this.angle = angle;
    }
    
}
