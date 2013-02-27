/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.AutonomousCommands.CommandIntake;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.AutonomousCommands.CommandStopIntake;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.EventButtonAPressed;
import WiredCatsEvents.GamePadEvents.EventButtonAReleased;
import WiredCatsEvents.GamePadEvents.EventButtonBPressed;
import WiredCatsEvents.GamePadEvents.EventButtonBReleased;
import WiredCatsEvents.GamePadEvents.EventButtonXPressed;
import WiredCatsEvents.GamePadEvents.EventButtonXReleased;
import WiredCatsEvents.GamePadEvents.EventButtonYPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYReleased;
import WiredCatsEvents.GamePadEvents.EventDPadXAxisMoved;
import WiredCatsEvents.GamePadEvents.EventLeftBumperPressed;
import WiredCatsEvents.GamePadEvents.EventRightBumperPressed;
import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 *
 * @author Bruce Crane
 */
public class SystemIntake extends WiredCatsSystem
{
    private Victor intakeMotor;
    
    private Timer incrementTimer;
    
    private boolean isIncrementing;
    
    public SystemIntake()
    {
        super();
        //TODO find out the victor ports.
        intakeMotor = new Victor(4);
        incrementTimer = new Timer();
        System.out.println("[WiredCats] Initialized System Intake.");
        
    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
        intakeMotor.set(0.0);
        
    }
    
    public void doEnabled(WiredCatsEvent event)
    {
        
    }

    public void doAutonomousSpecific(WiredCatsEvent event) 
    {
        if (event instanceof CommandIntake)
        {
            intakeMotor.set(-1*0.75);
        }
        else if (event instanceof CommandStopIntake)
        {
            intakeMotor.set(0.0);
        }
        
    }

    public void doTeleopSpecific(WiredCatsEvent event) 
    {  
        
//        propConstant = SmartDashboard.getNumber("propConstant");
//        integralConstant = SmartDashboard.getNumber("integralConstant");
//        derivativeConstant = SmartDashboard.getNumber("derivativeConstant");
        //desiredArmAngle = SmartDashboard.getNumber("desiredArmAngle");
        
        
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
        
    }
    
    private void handleGamePadEvents(EventGamePad event)
    {
        if (event instanceof EventButtonAPressed && !event.isController1())
        {
            intakeMotor.set(-1*1.0);
        }
        else if (event instanceof EventButtonAReleased && !event.isController1())
        {
            intakeMotor.set(0.0);
        }
        else if (event instanceof EventButtonBPressed && !event.isController1())
        {
            intakeMotor.set(1.0);
        }
        else if (event instanceof EventButtonBReleased && !event.isController1())
        {
            intakeMotor.set(0.0);
        }
       
    }

    
    public double isIntakeOn()
    {
        if (intakeMotor.get() != 0.0)
        {
            return 1;
        }
        else 
        {
            return 0;
        }
    }
    
}
