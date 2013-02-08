/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents;

/**
 *
 * @author Robotics
 */
public class EventRightYAxis extends EventGamePad
{
    public double y;
    
    public EventRightYAxis(Object source, byte controllerID, double y)
    {
        super(source,controllerID);
        this.y = y;
    }
    
    
}
