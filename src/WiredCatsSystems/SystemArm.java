/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import Util2415.Queue;
import WiredCatsControllers.ControllerShooter;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
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
import WiredCatsEvents.GamePadEvents.EventLeftBumperReleased;
import WiredCatsEvents.GamePadEvents.EventLeftTriggerPressed;
import WiredCatsEvents.GamePadEvents.EventLeftTriggerReleased;
import WiredCatsEvents.GamePadEvents.EventRightBumperPressed;
import WiredCatsEvents.GamePadEvents.EventRightTriggerPressed;
import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
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
    private double leftBumperPreset;
    private double leftTriggerPreset;
    private double fullCourtPreset;
    private double intakePreset;
    
    private Victor arm;
    
    private Queue positions;
    
    private double desiredArmAngle;
    private double actualArmAngle;
    
    private double kp;
    private double ki;
    private double kd;
    
    private double integral;
    private double lastError;
    private double lastTime;
    
    private boolean isIncrementing;
    
    private ControllerShooter cs;
    
    public SystemArm(ControllerShooter cs)
    {
        super();
        this.cs = cs;
        //TODO find out the victor ports.
        arm = new Victor(3);
        kp = WiredCats2415.textReader.getValue("propConstantArm");
        SmartDashboard.putNumber("Proportional ConstantArm", kp);
        ki = WiredCats2415.textReader.getValue("integralConstantArm");
        SmartDashboard.putNumber("Integral ConstantArm", ki);
        kd = WiredCats2415.textReader.getValue("derivativeConstantArm");
        SmartDashboard.putNumber("Derivative ConstantArm", kd);

        leftBumperPreset = WiredCats2415.textReader.getValue("leftBumperPreset");
        leftTriggerPreset = WiredCats2415.textReader.getValue("leftTriggerPreset");
        fullCourtPreset = WiredCats2415.textReader.getValue("fullCourtPreset");
        intakePreset = WiredCats2415.textReader.getValue("intakePreset");
        SmartDashboard.putNumber("leftBumperPreset", leftBumperPreset);
        SmartDashboard.putNumber("leftTriggerPreset", leftTriggerPreset);
        SmartDashboard.putNumber("fullCourtPreset", fullCourtPreset);
        SmartDashboard.putNumber("intakePreset", intakePreset);
        desiredArmAngle = leftTriggerPreset;
        SmartDashboard.putNumber("desiredAngle", desiredArmAngle);
        System.out.println("[WiredCats] Initialized System Arm.");

        lastTime = 0;
    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
       kp = SmartDashboard.getNumber("Proportional ConstantArm");
       ki = SmartDashboard.getNumber("Integral ConstantArm");
       kd = SmartDashboard.getNumber("Derivative ConstantArm");
       leftBumperPreset = SmartDashboard.getNumber("leftBumperPreset");
       leftTriggerPreset = SmartDashboard.getNumber("leftTriggerPreset");
       fullCourtPreset = SmartDashboard.getNumber("fullCourtPreset");
       intakePreset = SmartDashboard.getNumber("intakePreset");
    }

    public void doEnabled(WiredCatsEvent event)
    {
        if (event instanceof EventArmAngleChanged)
        {
            actualArmAngle = ((EventArmAngleChanged)event).getAngle()/10;
            double deltaTime = ((EventArmAngleChanged)event).time - lastTime;
            double error = desiredArmAngle - actualArmAngle;
            
            if (deltaTime > 1)
            {
                integral = 0;
            }
            else 
            {
               integral += error * deltaTime; 
            }
            double derivative = (error - lastError)/deltaTime;
            lastError = error;
            lastTime = ((EventArmAngleChanged)event).time;
            
            double power = kp*error + ki*integral + kd*derivative;
            
            if (power < 0.063 && power > -.063 && autonomous_state == AUTONOMOUS_ATTEMPTING)
                autonomous_state = AUTONOMOUS_COMPLETED;
            
            if (power > 1.0) 
            {
                arm.set(1.0);
            } 
            else if (power < -1.0)
            {
                arm.set(-1.0);
            }
            else
            {
                arm.set(power);
            }
        }
    }
    
    public void doAutonomousSpecific(WiredCatsEvent event) {
        if (event instanceof CommandNewArmAngle){
            desiredArmAngleChanged(((CommandNewArmAngle)event).angle);
        }
    }

    public void doTeleopSpecific(WiredCatsEvent event) 
    {
        //System.out.println("Queue size of System Arm: " + events.getSize());
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
    }
    
     private void handleGamePadEvents(EventGamePad event)
    {
        if (event instanceof EventButtonYPressed && !event.isController1())
        {
             desiredArmAngleChanged(desiredArmAngle - .2);
        }
        else if (event instanceof EventButtonBPressed && !event.isController1())
        {
            desiredArmAngleChanged(desiredArmAngle + .2);
        }
        if (event instanceof EventRightBumperPressed && !event.isController1())
        {
            desiredArmAngleChanged(leftBumperPreset);
            cs.pyramidShot();
        }
        else if (event instanceof EventLeftBumperReleased && !event.isController1())
        {
            desiredArmAngleChanged(leftTriggerPreset);
        }
        else if (event instanceof EventButtonXPressed && !event.isController1())
        {
            desiredArmAngleChanged(fullCourtPreset);
            cs.fullCourt();
        }
        else if (event instanceof EventButtonXReleased && !event.isController1())
        {
            desiredArmAngleChanged(leftTriggerPreset);
        }
        else if (event instanceof EventLeftTriggerPressed && event.isController1())
        {
            desiredArmAngleChanged(intakePreset);
        }
        else if (event instanceof EventLeftTriggerReleased && event.isController1())
        {
            desiredArmAngleChanged(leftTriggerPreset);
        }
    }
     
     private void desiredArmAngleChanged(double newAngle)
     {
         if (desiredArmAngle > newAngle){
                arm.set(-.2);
            } else {
                arm.set(.2);
            }
            desiredArmAngle = newAngle;
     }
     
    public double getArmAngle()
    {
        return actualArmAngle;
    }
}
