/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents.AutonomousCommands;

/**
 *
 * @author Robotics
 */
public class CommandNewArmAngle extends AutonomousCommand 
{
    
    public double angle;
    
    public CommandNewArmAngle(Object source, double angle)
    {
        super(source);
        this.angle = angle;
    }
}
