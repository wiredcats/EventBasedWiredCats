/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents;

/**
 *
 * @author Robotics
 */
public class EventLeftYAxis extends EventGamePad
{
    public double y; //it's a percentage, -100% to 100%
    
    public EventLeftYAxis(Object source, byte controllerID, double y)
    {
        super(source, controllerID);
        this.y = y;
    }
}
