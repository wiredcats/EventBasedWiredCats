package WiredCatsSystems;

import WiredCatsEvents.AutonomousCommands.CommandIntakeOn;
import WiredCatsEvents.AutonomousCommands.CommandIntakeOff;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Bruce Crane
 */
public class SystemIntake extends WiredCatsSystem
{
    private Victor intakeMotor;
    
    public SystemIntake() {
        super();
        intakeMotor = new Victor(4); // Both bots: 4
        System.out.println("[WiredCats] Initialized System Intake.");
    }
    
    public void doDisabled(WiredCatsEvent event) {
        intakeMotor.set(0.0);
    }
    
    public void doEnabled(WiredCatsEvent event) {
        
    }

    public void doAutonomousSpecific(WiredCatsEvent event) {
        if (event instanceof CommandIntakeOn) {
            intakeMotor.set(1.0);
        }
        else if (event instanceof CommandIntakeOff) {
            intakeMotor.set(0.0);
        }
    }

    public void doTeleopSpecific(WiredCatsEvent event) { 
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
    }
    
    private void handleGamePadEvents(EventGamePad event) {
        //Intake normally
        if (event instanceof EventLeftTriggerPressed && event.isController1()) {
            intakeMotor.set(1.0);
        }
        else if (event instanceof EventLeftTriggerReleased && event.isController1()) {
            intakeMotor.set(0.0);
        }
        
        //Intake while shooting
        else if (event instanceof EventRightTriggerPressed && event.isController1()) {
            intakeMotor.set(1.0);
        }
        else if (event instanceof EventRightTriggerReleased && event.isController1()) {
            intakeMotor.set(0.0);
        }
        
        //Backdrive
        else if (event instanceof EventButtonYPressed && event.isController1()) {
            intakeMotor.set(-1.0);
        }
        else if (event instanceof EventButtonYReleased && event.isController1()) {
            intakeMotor.set(0.0);
        }
    }
}
