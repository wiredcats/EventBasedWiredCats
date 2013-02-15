/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.EventGamePad;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 * The system that controls everything drive train.
 *
 * @author BruceCrane
 */
public class SystemDrive extends WiredCatsSystem 
{

    private Talon left;
    private Talon right;
    
    private double driveDeadband;
    
    private double driveGain;

    private double leftDriveDesiredPosition;
    private double rightDriveDesiredPosition;
    
    private double actualLeftDrivePosition;
    private double actualRightDrivePosition;
    
    private double kp;
    private double ki;
    private double kd;
    
    private double leftIntegral = 0.0;
    private double rightIntegral = 0.0;
    private double lastLeftError = 0;
    private double lastRightError = 0;
    
    public SystemDrive() {
        super();

        left = new Talon(1);
        right = new Talon(2);        
        leftDriveDesiredPosition = 0;
        rightDriveDesiredPosition = 0;

        driveDeadband = WiredCats2415.textReader.getValue("driveDeadband");
        driveGain = WiredCats2415.textReader.getValue("driveGain");
        
        kp = WiredCats2415.textReader.getValue("propConstantDrive");
        SmartDashboard.putNumber("propConstantDrive", kp);
        ki = WiredCats2415.textReader.getValue("integralConstantDrive");
        SmartDashboard.putNumber("integralConstantDrive", ki);
        kd = WiredCats2415.textReader.getValue("derivativeConstantDrive");
        SmartDashboard.putNumber("derivativeConstatnDrive", kd);
        
        
        System.out.println("[WiredCats] Initialized System Drive");

    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
        left.set(0.0);
        right.set(0.0);
        leftIntegral = 0.0;
        rightIntegral = 0.0;
    }
    
    public void doAutonomous(WiredCatsEvent event) 
    {
        if (event instanceof CommandNewDesiredPosition)
        {
            leftDriveDesiredPosition = ((CommandNewDesiredPosition)event).leftEncoder;
            rightDriveDesiredPosition = ((CommandNewDesiredPosition)event).rightEncoder;    
        }
    }
    
    public void update()
    {
        //left side.
        double error = leftDriveDesiredPosition - actualLeftDrivePosition;
            
            leftIntegral += error;
            
            double derivative = lastLeftError - error;
            lastLeftError = error;
            
            kp = SmartDashboard.getNumber("propConstantDrive");
            ki = SmartDashboard.getNumber("integralConstantDrive");
            kd = SmartDashboard.getNumber("derivativeConstantDrive");
            
            double leftPower = kp*error + ki*leftIntegral + kd*derivative;
            
            
            if (error != 0)
            {
                left.set(leftPower);
            }
        //right side.
        error = rightDriveDesiredPosition - actualRightDrivePosition;
        
            rightIntegral += error;
            
            derivative = lastRightError - error;
            lastRightError = error;
            
            double rightPower = kp*error + ki*rightIntegral + kd*derivative;
            
            if (leftPower == 0 &&
                rightPower == 0 &&
                autonomous_state == AUTONOMOUS_ATTEMPTING) autonomous_state = AUTONOMOUS_COMPLETED;

    }
    
    public byte autonomous_AtDesiredNode()
    {
        if (autonomous_state == AUTONOMOUS_COMPLETED)
        {
            autonomous_state = AUTONOMOUS_WAITING;
            return AUTONOMOUS_COMPLETED;
        }
        return autonomous_state;
    }
    
    public void doTeleop(WiredCatsEvent event) 
    {
        if (event instanceof EventLeftYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                left.set(setVictorValues(((EventLeftYAxisMoved) event).y));
                System.out.println(setVictorValues(((EventLeftYAxisMoved) event).y));
            }
        } else if (event instanceof EventRightYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                right.set(setVictorValues(((EventRightYAxisMoved) event).y));
            }
        }
    }

    /**
     * This figures out all the things that are needed to consider when setting the victor value. 
     *
     * @param value
     * @return
     */
    private double setVictorValues(double value) 
    {
        
        double a = driveGain;
        double b = driveDeadband;
        
        if (value >= 0)
        {
            return b + (1-b)*(a*MathUtils.pow(value, 3) + (1-a)*value);
        }
        else
        {
            return -1*b + (1-b)*(a*MathUtils.pow(value, 3) + (1-a)*value);
        }
    }

}
