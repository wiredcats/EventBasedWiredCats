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
public class EventLeftDriveEncoderChanged extends WiredCatsEvent
{
    public int encoderValue;
    
    public EventLeftDriveEncoderChanged(Object source, int encoderValue)
    {
        super(source);
        this.encoderValue = encoderValue;
    }
}
