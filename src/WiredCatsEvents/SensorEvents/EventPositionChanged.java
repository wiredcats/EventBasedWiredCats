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
public class EventPositionChanged extends WiredCatsEvent
{
    public double leftEncoder;
    public double rightEncoder;
    public double time;
    
    public EventPositionChanged(Object source, double leftEncoder, double rightEncoder, double time)
    {
        super(source);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.time = time;
    }
}
