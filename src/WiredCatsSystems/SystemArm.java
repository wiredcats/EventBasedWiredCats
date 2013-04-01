/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsControllers.ControllerShooter;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.SensorEvents.EventArmLimitReached;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 *
 * @author Robotics
 */
public class SystemArm extends WiredCatsSystem 
{
    private double backPyramidPreset;
    private double frontPyramidPreset;
    private double intakePreset;
    private double hoverPreset;
    private double upperBoundHardStop;
    
    private Victor arm;
    
    private double desiredArmAngle;
    private double actualArmAngle;
    
    private double kp;
    private double ki;
    private double kd;
    
    private double integral;
    private double lastError;
    
    private ControllerShooter shooter;
    
    private boolean isManualControl;
    
    private boolean armHasBeenReset;
    
    public SystemArm(ControllerShooter cs) {
        super();
        this.shooter = cs;
        
        arm = new Victor(3); // Both bots: 3
        kp = WiredCats2415.textReader.getValue("propConstantArm");
        ki = WiredCats2415.textReader.getValue("integralConstantArm");
        kd = WiredCats2415.textReader.getValue("derivativeConstantArm");

        intakePreset = WiredCats2415.textReader.getValue("intakePreset");
        backPyramidPreset = WiredCats2415.textReader.getValue("backPyramidPreset");
        frontPyramidPreset = WiredCats2415.textReader.getValue("frontPyramidPreset");
        hoverPreset = WiredCats2415.textReader.getValue("hoverPreset");
        upperBoundHardStop = WiredCats2415.textReader.getValue("upperBoundHardStop");

        desiredArmAngle = 0;
        
        isManualControl = false;
        
        armHasBeenReset = false;
        
        System.out.println("[WiredCats] Initialized System Arm.");
    }
    
    public void doDisabled(WiredCatsEvent event) {
       kp = WiredCats2415.textReader.getValue("propConstantArm");
       ki = WiredCats2415.textReader.getValue("integralConstantArm");
       kd = WiredCats2415.textReader.getValue("derivativeConstantArm");
       
       intakePreset = WiredCats2415.textReader.getValue("intakePreset");
        backPyramidPreset = WiredCats2415.textReader.getValue("backPyramidPreset");
        frontPyramidPreset = WiredCats2415.textReader.getValue("frontPyramidPreset");
        hoverPreset = WiredCats2415.textReader.getValue("hoverPreset");
        upperBoundHardStop = WiredCats2415.textReader.getValue("upperBoundHardStop");

    }

    public void doEnabled(WiredCatsEvent event) {
        if (event instanceof EventArmLimitReached)
        {
            this.armHasBeenReset = true;
            desiredArmAngleChanged(0.5);
            if (arm.get() < 0.0) arm.set(0.0);
        }
        else if (event instanceof EventArmAngleChanged && armHasBeenReset) {
            actualArmAngle = ((EventArmAngleChanged)event).getAngle();
            if (isManualControl) return;
            double error = desiredArmAngle - actualArmAngle;
            
            integral += error; 
            
            double derivative = (error - lastError);
            lastError = error;
            
            double power = kp*error + ki*integral + kd*derivative;
            
            //If within deadband of 0.3, autonomous is done
            if (error < .08 && error > -.08 
                    && autonomous_state == AUTONOMOUS_ATTEMPTING)
            {
                autonomous_state = AUTONOMOUS_COMPLETED;
            }
            
//            System.out.println("power: " + power);
//            System.out.println("error: " + error);
//            System.out.println("");
            
            if (power > 1.0) power = 0.7;
            else if (power < -1.0) power = -1.0;
            arm.set(power);
        }
    }
    
    public void doAutonomousSpecific(WiredCatsEvent event) {
        if (event instanceof CommandNewArmAngle){
            desiredArmAngleChanged(((CommandNewArmAngle)event).angle);
            autonomous_state = AUTONOMOUS_ATTEMPTING;
        }
    }

    public void doTeleopSpecific(WiredCatsEvent event) 
    {
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
    }
    
    public void update(){
        if (!armHasBeenReset) {
            arm.set(-0.3);
        }
    }
    
     private void handleGamePadEvents(EventGamePad event) {
         //If Y or B pressed, do incremental control
         if (event instanceof EventButtonYPressed && !event.isController1()) {
             arm.set(-0.5);
             isManualControl = true;
         } else if (event instanceof EventButtonBPressed && !event.isController1()) {
             arm.set(0.35);
             isManualControl = true;
         }
        else if ((event instanceof EventButtonYReleased ||
                event instanceof EventButtonBReleased) && !event.isController1()) {
            desiredArmAngleChanged(actualArmAngle);
            isManualControl = false;
            arm.set(0.0);
        }
        
        //If right bumper pressed, do back pyramid shot
        else if (event instanceof EventRightBumperPressed && !event.isController1()) {
            desiredArmAngleChanged(backPyramidPreset);
        }
        
        //If right trigger pressed, do front pyramid shot
        else if (event instanceof EventRightTriggerPressed && !event.isController1()) {
            desiredArmAngleChanged(frontPyramidPreset);
        }
         
         //If X is pressed, go to hover position
         else if (event instanceof EventButtonXPressed && !event.isController1()) {
             desiredArmAngleChanged(hoverPreset);
         } 
         
         // If primary left trigger is pressed, go down to intake.
         // Else go to hover
         else if (event instanceof EventLeftTriggerPressed && event.isController1()) {
             desiredArmAngleChanged(intakePreset);
         } else if (event instanceof EventLeftTriggerReleased && event.isController1()) {
             desiredArmAngleChanged(hoverPreset);
         }
    }
     
     /**
      * Provides kickstart for PID Loop
      * (Otherwise AngleChanged Event will not start)
      */
     private void desiredArmAngleChanged(double newAngle)
     {
         if (desiredArmAngle > newAngle) arm.set(-.2);
         else arm.set(.2);
         
         integral = 0;
         
         desiredArmAngle = newAngle;
     }
     
    public double getArmAngle() { return actualArmAngle; }
}
