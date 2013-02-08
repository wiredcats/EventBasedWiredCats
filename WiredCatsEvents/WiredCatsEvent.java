/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents;

/**
 *
 * @author Robotics
 */
public abstract class WiredCatsEvent 
{
    
    private Object source;
    
    public Object getObject()
    {
        return source;
    }
    
    public WiredCatsEvent(Object source)
    {
        this.source = source;
    }
    
}
