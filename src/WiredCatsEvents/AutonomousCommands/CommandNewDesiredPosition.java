/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsEvents.AutonomousCommands;

/**
 *
 * @author Robotics
 */
public class CommandNewDesiredPosition extends AutonomousCommand
{
    public double leftEncoder;
    public double rightEncoder;
    public double angle;
    
    
    public CommandNewDesiredPosition(Object source, double leftEncoder, double rightEncoder, double angle)
    {
        super(source);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.angle = angle;
    }
}
