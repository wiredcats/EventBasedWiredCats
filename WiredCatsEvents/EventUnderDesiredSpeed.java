/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents;

/**
 *
 * @author Robotics
 */
public class EventUnderDesiredSpeed extends WiredCatsEvent
{
    
    public static final byte ENCODER_1 = 1;
    public static final byte ENCODER_2 = 2;
    
    public byte encoderID;
    
    public EventUnderDesiredSpeed(Object source, byte encoderID)
    {
        super(source);
        this.encoderID = encoderID;
    }
}
