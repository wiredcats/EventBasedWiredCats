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

    private final double presetA;
    private final double presetB;
    private final double presetX;
    private final double presetY;
    
    private Victor arm;
    private Victor intakeMotor;
    
    private double desiredArmAngle;
    private double actualArmAngle;
    
    private double propConstant;
    private double integralConstant;
    private double derivativeConstant;
    
    private double integral;
    private double lastError;
    
    private Timer incrementTimer;
    
    private boolean isIncrementing;
    
    public SystemIntake()
    {
        super();
        //TODO find out the victor ports.
        arm = new Victor(10);
        intakeMotor = new Victor(8);
        System.out.println("[WiredCats] Initialized system Intake");
        propConstant = WiredCats2415.textReader.getValue("propConstantArm");
        SmartDashboard.putNumber("Proportional ConstantArm", propConstant);
        integralConstant = WiredCats2415.textReader.getValue("integralConstantArm");
        SmartDashboard.putNumber("Integral ConstantArm", integralConstant);
        derivativeConstant = WiredCats2415.textReader.getValue("derivativeConstantArm");
        SmartDashboard.putNumber("Derivative ConstantArm", derivativeConstant);
        desiredArmAngle = 150; //midrange on robot arm.
        SmartDashboard.putNumber("desiredAngle", desiredArmAngle);
        
        presetA = WiredCats2415.textReader.getValue("presetA");
        presetB = WiredCats2415.textReader.getValue("presetB");
        presetX = WiredCats2415.textReader.getValue("presetX");
        presetY = WiredCats2415.textReader.getValue("presetY");
        
        incrementTimer = new Timer();
    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
        arm.set(0.0);
        intakeMotor.set(0.0);
        
        integral = 0.0;
        
    }

    public void doAutonomous(WiredCatsEvent event) 
    {
        if (event instanceof CommandIntake)
        {
            intakeMotor.set(0.4);
        }
        else if (event instanceof CommandStopIntake)
        {
            intakeMotor.set(0.0);
        }
        else if (event instanceof CommandNewArmAngle)
        {
            desiredArmAngle = ((CommandNewArmAngle)event).angle;
        }
    }
    
    public void update() 
    {
         //PID LOOP
            
            double error = desiredArmAngle - actualArmAngle;
            
            integral += error;
            
            double derivative = lastError - error;
            lastError = error;
            
            double kp = SmartDashboard.getNumber("propConstant");
            double ki = SmartDashboard.getNumber("integralConstant");
            double kd = SmartDashboard.getNumber("derivativeConstant");
            
            double power = kp*error + ki*integral + kd*derivative;
            
            if (power == 0 && autonomous_state == AUTONOMOUS_ATTEMPTING) autonomous_state = AUTONOMOUS_COMPLETED;
            
            if (error != 0)
            {
                arm.set(power);
            }
    }
    
    public byte autonomous_AtDesiredNode()
    {
        if (autonomous_state == AUTONOMOUS_COMPLETED)
        {
            autonomous_state = AUTONOMOUS_WAITING;
            return AUTONOMOUS_COMPLETED;
        }
        else return autonomous_state;
    }

    public void doTeleop(WiredCatsEvent event) 
    {  
        if (incrementTimer.get() > .5)
        {
            incrementTimer.reset();
            incrementTimer.start();
            if (isIncrementing){
                desiredArmAngle++;
            } else {
                desiredArmAngle--; 
            }
                    
        }
        
        propConstant = SmartDashboard.getNumber("propConstant");
        integralConstant = SmartDashboard.getNumber("integralConstant");
        derivativeConstant = SmartDashboard.getNumber("derivativeConstant");
        
        SmartDashboard.putNumber("actualArmAngle", actualArmAngle);
        desiredArmAngle = SmartDashboard.getNumber("desiredArmAngle");
        
        
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
        else if (event instanceof EventArmAngleChanged)
        {
            actualArmAngle = ((EventArmAngleChanged)event).getAngle();
 
           
            
//            if (power > 0)
//            {
//                arm.set(power);
//            }
//            else
//            {
//                arm.set(0.1);
//            }
            
        }
        
    }
    
    private void handleGamePadEvents(EventGamePad event)
    {
        if (event instanceof EventButtonAPressed && event.isController1())
        {
            intakeMotor.set(.4);
        }
        else if (event instanceof EventButtonAPressed && event.isController1())
        {
            intakeMotor.set(0.0);
        }
        else if (event instanceof EventButtonAPressed && !event.isController1())
        {
            desiredArmAngle = presetA;
        }
        else if (event instanceof EventButtonBPressed && event.isController1())
        {
            desiredArmAngle = presetB;
        }
        else if (event instanceof EventButtonYPressed && event.isController1())
        {
            desiredArmAngle = presetY;
        }
        else if (event instanceof EventButtonXPressed && event.isController1())
        {
            desiredArmAngle = presetX;
        }
        else if (event instanceof EventDPadXAxisMoved && event.isController1())
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
            
    }
    
    public double getArmAngle()
    {
        return actualArmAngle;
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
