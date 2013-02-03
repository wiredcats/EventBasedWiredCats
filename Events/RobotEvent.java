/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author Robotics
 */
public abstract class RobotEvent 
{
    
    private Object source;
    
    public Object getObject()
    {
        return source;
    }
    
    public RobotEvent(Object source)
    {
        this.source = source;
    }
    
}
