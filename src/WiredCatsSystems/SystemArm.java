/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import Util2415.Queue;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.EventButtonAPressed;
import WiredCatsEvents.GamePadEvents.EventButtonAReleased;
import WiredCatsEvents.GamePadEvents.EventButtonBPressed;
import WiredCatsEvents.GamePadEvents.EventButtonBReleased;
import WiredCatsEvents.GamePadEvents.EventButtonXPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYReleased;
import WiredCatsEvents.GamePadEvents.EventDPadXAxisMoved;
import WiredCatsEvents.GamePadEvents.EventLeftBumperPressed;
import WiredCatsEvents.GamePadEvents.EventLeftTriggerPressed;
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

     private final double presetA;
    private final double presetB;
    private final double presetX;
    private final double presetY;
    private double leftBumperPreset;
    private double leftTriggerPreset;
    
    private Victor arm;
    
    private double desiredArmAngle;
    private double actualArmAngle;
    
    private double kp;
    private double ki;
    private double kd;
    
    private double integral;
    private double lastError;
    private double lastTime;
    
    private Timer incrementTimer;
    
    private boolean isIncrementing;
    
    public SystemArm()
    {
        super();
        //TODO find out the victor ports.
        arm = new Victor(3);
        kp = WiredCats2415.textReader.getValue("propConstantArm");
        SmartDashboard.putNumber("Proportional ConstantArm", kp);
        ki = WiredCats2415.textReader.getValue("integralConstantArm");
        SmartDashboard.putNumber("Integral ConstantArm", ki);
        kd = WiredCats2415.textReader.getValue("derivativeConstantArm");
        SmartDashboard.putNumber("Derivative ConstantArm", kd);
        desiredArmAngle = 49.0; //midrange on robot arm.
        SmartDashboard.putNumber("desiredAngle", desiredArmAngle);
        
        presetA = WiredCats2415.textReader.getValue("presetA");
        presetB = WiredCats2415.textReader.getValue("presetB");
        presetX = WiredCats2415.textReader.getValue("presetX");
        presetY = WiredCats2415.textReader.getValue("presetY");
        leftBumperPreset = WiredCats2415.textReader.getValue("leftBumperPreset");
        leftTriggerPreset = WiredCats2415.textReader.getValue("leftTriggerPreset");
        
        System.out.println("[WiredCats] Initialized System Arm.");
        
        incrementTimer = new Timer();
        lastTime = 0;
    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
       kp = SmartDashboard.getNumber("Proportional ConstantArm");
       ki = SmartDashboard.getNumber("Integral ConstantArm");
       kd = SmartDashboard.getNumber("Derivative ConstantArm");
    }

    public void doAutonomous(WiredCatsEvent event) 
    {
        if (event instanceof CommandNewArmAngle)
        {
            desiredArmAngle = ((CommandNewArmAngle)event).angle;
        }
        else if (event instanceof EventArmAngleChanged)
        {
            double error = desiredArmAngle - actualArmAngle;
            //double deltaTime = ((EventArmAngleChanged))
            
            integral += error;
            
            double derivative = lastError - error;
            lastError = error;
            
//            double kp = SmartDashboard.getNumber("Proportional ConstantArm");
//            double ki = SmartDashboard.getNumber("Integral ConstantArm");
//            double kd = SmartDashboard.getNumber("Derivative ConstantArm");
            
            double power = kp*error + ki*integral + kd*derivative;
            
            if (power == 0 && autonomous_state == AUTONOMOUS_ATTEMPTING) autonomous_state = AUTONOMOUS_COMPLETED;
            
            //System.out.println("Power: " + power);
            
            if (error != 0)
            {
                arm.set(power);
            }
        }
    }

    public void doTeleop(WiredCatsEvent event) 
    {
        //System.out.println("Queue size of System Arm: " + events.getSize());
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
        else if (event instanceof EventArmAngleChanged)
        {
           PIDControl((EventArmAngleChanged)event);
        }
    }
    
     private void handleGamePadEvents(EventGamePad event)
    {
        //System.out.println("I AM HERE>");
        
        if (event instanceof EventDPadXAxisMoved && !event.isController1())
        {
            double x = ((EventDPadXAxisMoved)event).x;
            if (x > 0.5)
            {
                incrementTimer.reset();
                incrementTimer.start();
                isIncrementing = false;
            }
            else if (x < -0.5)
            {
                incrementTimer.stop();
                incrementTimer.reset();
                isIncrementing = true;
            }
            else {
                incrementTimer.stop();
                incrementTimer.reset();
            } 
        }
        else if (event instanceof EventLeftBumperPressed)
        {
            desiredArmAngle = leftBumperPreset;
            System.out.println("left bumper pressed");
        }
        else if (event instanceof EventLeftTriggerPressed)
        {
            desiredArmAngle = leftTriggerPreset;
            System.out.println("left trigger pressed");
        }
    }
     
    private void PIDControl(EventArmAngleChanged event)
    {
         //System.out.println("event received that arm changed");
            actualArmAngle = event.getAngle()/10;
//            System.out.println("New Actual Angle: " + actualArmAngle);
            double deltaTime = event.time - lastTime;
            double error = desiredArmAngle - actualArmAngle;
            
            if (deltaTime > 3)
            {
                integral = 0;
            }
            else 
            {
               integral += error * deltaTime; 
            }
//            System.out.println("error: " + error);
              
            double derivative = (error - lastError)/deltaTime;
//            System.out.println("Derivative: " + derivative);
            lastError = error;
            lastTime = event.time;
            
//            double kp = SmartDashboard.getNumber("Proportional ConstantArm");
//            double ki = SmartDashboard.getNumber("Integral ConstantArm");
//            double kd = SmartDashboard.getNumber("Derivative ConstantArm");
            
//            System.out.println("Integral:" + integrals.sum());
            
            double power = kp*error + ki*integral + kd*derivative;
            
            if (power == 0 && autonomous_state == AUTONOMOUS_ATTEMPTING) autonomous_state = AUTONOMOUS_COMPLETED;
            
            //System.out.println("Arm Power: " + power);
         
            if (power > 1.0) 
            {
                arm.set(1.0);
//                System.out.println("power over 1");
            } 
            else if (power < -1.0)
            {
                arm.set(-1.0);
//                System.out.println("power under 1");
            }
            else
            {
                arm.set(power);
            }
    }
         
    public double getArmAngle()
    {
        return actualArmAngle;
    }

    public byte autonomous_AtDesiredNode() 
    {
        return -1;
    }
    
}
