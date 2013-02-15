/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util2415;

/**
 *
 * @author Bruce Crane
 */
public class Node 
{
    
    public double leftTicks;
    public double rightTicks;
    public double frisbeesHeld;
    public double armAngle;
    public boolean isIntakeOn;
    
    public Node(double leftTicks, double rightTicks, double frisbeesHeld, double armAngle, double isIntakeOn)
    {
        this.leftTicks = leftTicks;
        this.rightTicks = rightTicks;
        this.frisbeesHeld = frisbeesHeld;
        this.armAngle = armAngle;
        if (isIntakeOn != 0.0) this.isIntakeOn = true;
        else this.isIntakeOn = false;
        
    }
    
    
    
}
