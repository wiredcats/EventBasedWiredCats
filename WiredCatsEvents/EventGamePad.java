/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents;

/**
 *
 * @author Robotics
 */
public class EventGamePad extends WiredCatsEvent
{
    public static final byte CONTROLLER_1 = 1;
    public static final byte CONTROLLER_2 = 2;
    
    private byte controllerID;
    
    public EventGamePad(Object source, byte controllerID)
    {
        super(source);
        this.controllerID = controllerID;
    }
    
    public boolean isController1()
    {
        return (controllerID == CONTROLLER_1? true : false);
    }
}
