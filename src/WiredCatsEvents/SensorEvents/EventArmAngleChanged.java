/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents.SensorEvents;

import WiredCatsEvents.WiredCatsEvent;

/**
 *
 * @author Bruce Crane
 */
public class EventArmAngleChanged extends WiredCatsEvent
{
    
    private double angle;
    
    public EventArmAngleChanged(Object source, double angle)
    {
        super(source);
        this.angle = angle;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
}
