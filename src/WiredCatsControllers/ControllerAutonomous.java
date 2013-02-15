/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import Util2415.LogReader;
import Util2415.Node;
import WiredCatsEvents.AutonomousCommands.CommandIntake;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import WiredCatsEvents.AutonomousCommands.CommandShoot;
import WiredCatsEvents.AutonomousCommands.CommandStopIntake;
import WiredCatsSystems.SystemDrive;
import WiredCatsSystems.SystemIntake;
import WiredCatsSystems.SystemShooter;
import WiredCatsSystems.WiredCatsSystem;
import java.util.Vector;

/**
 *
 * @author Bruce Crane
 */
public class ControllerAutonomous extends WiredCatsController
{
    
    LogReader lr;
    Vector nodes;
    
    Node desiredState;
    boolean atDesiredNode;
    
    double frisbeesHeld;
    
    boolean enabled = false;
    
    SystemDrive sd;
    SystemShooter ss;
    SystemIntake si;
    
    boolean driveReady;
    boolean shooterReady;
    boolean intakeReady;
    
    public ControllerAutonomous(int limit, String strategyFileName)
    {
        super(limit);
        atDesiredNode = true;
                
        driveReady = false;
        shooterReady = false;
        intakeReady = false;
    }
    
    public void readLog(String s)
    {
        nodes = lr.readLog(s);
    }
    
    public void begin()
    {
        enabled = true;
    }
    
    public void end()
    {
        enabled = false;
    }

    public void run() 
    {
        while (true)
        {
            if (enabled)
            {
                if (!isEmpty() && atDesiredNode)
                {
                    desiredState = take();
                    atDesiredNode = false;
                    setNewDesiredNode();
                    driveReady = false;
                    shooterReady = false;
                    intakeReady = false;
                }
                
                if (sd.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED)
                {
                    driveReady = true;
                }
                if (ss.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED)
                {
                    shooterReady = true;
                }
                if (si.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED)
                {
                    intakeReady = true;
                }
                
                if (driveReady && shooterReady && intakeReady)
                {
                    atDesiredNode = true;
                }
            } 
        }
    }
    
    private boolean isEmpty()
    {
        return nodes.isEmpty();
    }
    
    private Node take()
    {
        Node temp = (Node)nodes.firstElement();
        nodes.removeElementAt(0);
        return temp;
    }
    
    private void setNewDesiredNode()
    {
        double nFrisbees = desiredState.frisbeesHeld;
        double nArmAngle = desiredState.armAngle;

        if (desiredState.isIntakeOn)
        {
            fireEvent(new CommandIntake(this));
        }
        else
        {
            fireEvent(new CommandStopIntake(this));
        }
        
        if (nFrisbees < frisbeesHeld)
        {
            fireEvent(new CommandShoot(this));
        }
        frisbeesHeld = nFrisbees;
        
        //TODO get the gyro values.
        fireEvent(new CommandNewDesiredPosition(this, desiredState.leftTicks, desiredState.rightTicks, 0));
        fireEvent(new CommandNewArmAngle(this, desiredState.armAngle));
        
    }
    
}
    