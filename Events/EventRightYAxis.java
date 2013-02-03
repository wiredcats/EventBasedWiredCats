/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author Robotics
 */
public class EventRightYAxis extends EventGamePad
{
    public int y;
    
    public EventRightYAxis(Object source, byte controllerID, int y)
    {
        super(source,controllerID);
        this.y = y;
    }
    
    
}
