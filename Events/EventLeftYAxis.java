/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author Robotics
 */
public class EventLeftYAxis extends EventGamePad
{
    public int y; //it's a percentage, -100% to 100%
    
    public EventLeftYAxis(Object source, byte controllerID, int y)
    {
        super(source, controllerID);
        this.y = y;
    }
}
