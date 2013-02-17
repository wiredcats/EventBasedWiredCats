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
    public double time;
    
    public EventArmAngleChanged(Object source, double angle, double time)
    {
        super(source);
        this.angle = angle;
        this.time = time;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
}
