/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.SensorEvents.EventPositionChanged;

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

    private double leftDesiredEncoderDistance;
    private double rightDesiredEncoderDistance;
    
    private double leftEncoderDistance;
    private double rightEncoderDistance;
    
    private double gyroAngle;
    
    private double leftPower;
    private double rightPower;
    
    private double lastTime;
    
    private double leftIntegral;
    private double rightIntegral;
    
    private double lastLeftError;
    private double lastRightError;
    
    private double kp;
    private double ki;
    private double kd;
    
    public SystemDrive() {
        super();

        left = new Talon(2);
        right = new Talon(7); //was 1.        
        leftDesiredEncoderDistance = 0;
        rightDesiredEncoderDistance = 0;

        driveDeadband = WiredCats2415.textReader.getValue("driveDeadband");
        driveGain = WiredCats2415.textReader.getValue("driveGain");
        
        gyroAngle = 0;
        lastTime = 0;
        
        leftIntegral = 0;
        rightIntegral = 0;
        lastLeftError = 0;
        lastRightError = 0;
        
        SmartDashboard.putNumber("driveDeadband", driveDeadband);
        SmartDashboard.putNumber("driveGain", driveGain);
        
        kp = WiredCats2415.textReader.getValue("propConstantDrive");
        SmartDashboard.putNumber("propConstantDrive", kp);
        ki = WiredCats2415.textReader.getValue("integralConstantDrive");
        SmartDashboard.putNumber("integralConstantDrive", ki);
        kd = WiredCats2415.textReader.getValue("derivativeConstantDrive");
        SmartDashboard.putNumber("derivativeConstantDrive", kd);
        
        
        System.out.println("[WiredCats] Initialized System Drive");

    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
        kp = SmartDashboard.getNumber("propConstantDrive");
        ki = SmartDashboard.getNumber("integralConstantDrive");
        kd = SmartDashboard.getNumber("derivativeConstantDrive");
        driveDeadband = SmartDashboard.getNumber("driveDeadband");
        driveGain = SmartDashboard.getNumber("driveGain");
        
        left.set(0.0);
        right.set(0.0);
        leftIntegral = 0.0;
        rightIntegral = 0.0;
    }
    
    public void doEnabled(WiredCatsEvent event) {};
    
    public void doAutonomousSpecific(WiredCatsEvent event) 
    {
        if (event instanceof CommandNewDesiredPosition)
        {
            System.out.println("NEW DESIRED POSITION");
              
            changeDesiredPosition(((CommandNewDesiredPosition)event).leftEncoder,
                    ((CommandNewDesiredPosition)event).rightEncoder);
            
        }
        else if (event instanceof EventPositionChanged)
        {
            gyroAngle = ((EventPositionChanged)event).time;
            leftEncoderDistance = ((EventPositionChanged)event).leftEncoder;
            rightEncoderDistance = ((EventPositionChanged)event).rightEncoder;
            
            //compensate for error.
            double deltaTime = ((EventPositionChanged)event).time - lastTime;
            double leftError = leftDesiredEncoderDistance - leftEncoderDistance;
            double rightError = rightDesiredEncoderDistance - rightEncoderDistance;
            
            if (deltaTime > 1)
            {
                leftIntegral = 0;
                rightIntegral = 0;
            }
            else 
            {
               leftIntegral += leftError * deltaTime;
               rightIntegral += rightError * deltaTime;
            }
            
            double leftDerivative = (leftError - lastLeftError)/deltaTime;
            double rightDerivative = (rightError - lastRightError)/deltaTime;
            
            lastLeftError = leftError;
            lastRightError = rightError;
            
            lastTime = ((EventPositionChanged)event).time;
            
            double leftPower = kp*leftError + ki*leftIntegral + kd*leftDerivative;
            double rightPower = kp*rightError + ki*rightIntegral + kd*rightDerivative;
            
            if ( (leftPower < 0.1 && leftPower > -.1) &&
                    (rightPower <0.1 && rightPower > -.1) &&
                    autonomous_state == AUTONOMOUS_ATTEMPTING)
                autonomous_state = AUTONOMOUS_COMPLETED;
            
            left.set(setPower(leftPower));
            //right.set(setPower(rightPower));
        }
    }
    
    public void doTeleopSpecific(WiredCatsEvent event) 
    {
        //System.out.println(events.getSize());
        
        if (event instanceof EventLeftYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                left.set(setVictorValues(((EventLeftYAxisMoved) event).y));
//                System.out.println("left: " + setVictorValues(((EventLeftYAxisMoved) event).y));
            }
        } else if (event instanceof EventRightYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                right.set(-1*setVictorValues(((EventRightYAxisMoved)event).y));
//                System.out.println("right: " + setVictorValues(((EventRightYAxisMoved) event).y));
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
            if (value < b) return 0;
            
            return b + (1-b)*(a*MathUtils.pow(value, 1) + (1-a)*value);
        }
        else
        {
            if (value > -1*b) return 0;
            
            return -1*b + (1-b)*(a*MathUtils.pow(value, 1) + (1-a)*value);
        }
    }

    
    private double setPower(double d)
    {
            if (d > 1.0) 
            {
                return 1.0;
            } 
            else if (d < -1.0)
            {
                return -1.0;
            }
            else
            {
                return d;
            }
    }
    
    private void changeDesiredPosition(double newLeftValue, double newRightValue)
    {
        if (leftDesiredEncoderDistance > newLeftValue) left.set(0.1);
        else left.set(-.1);
        if (rightDesiredEncoderDistance > newRightValue) right.set(0.1);
        else right.set(-.1);
        
        leftDesiredEncoderDistance = newLeftValue;          
        rightDesiredEncoderDistance = newRightValue;  
    }
}
