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
public class EventUnderDesiredSpeed extends WiredCatsEvent
{
    
    public static final byte ENCODER_1 = 1;
    public static final byte ENCODER_2 = 2;
    
    public boolean isFirstWheel;
    
    public EventUnderDesiredSpeed(Object source, boolean isFirstWheel)
    {
        super(source);
        this.isFirstWheel = isFirstWheel;
    }
}
