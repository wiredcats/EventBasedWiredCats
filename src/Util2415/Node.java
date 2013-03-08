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
    public int frisbeesShot;
    public double armAngle;
    public boolean isIntakeOn;
    
    public Node(double leftTicks, double rightTicks, int frisbeesShot, double armAngle, double isIntakeOn)
    {
        this.leftTicks = leftTicks;
        this.rightTicks = rightTicks;
        this.frisbeesShot = frisbeesShot;
        this.armAngle = armAngle;
        if (isIntakeOn != 0.0) this.isIntakeOn = true;
        else this.isIntakeOn = false;
        
    }
    
    
    public String toString()
    {
        return leftTicks + " " + rightTicks + " " + frisbeesShot + " " + armAngle + " " + isIntakeOn;
    }
    
    
}
