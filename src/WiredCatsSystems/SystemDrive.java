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
public class SystemDrive extends WiredCatsSystem {
    private Talon leftTalon;
    private Talon rightTalon;
    
    private double driveDeadband;
    
    private double driveGain;

    private double leftDesiredEncoderDistance;
    private double rightDesiredEncoderDistance;
    
    private double leftEncoderDistance;
    private double rightEncoderDistance;
    
    private double leftIntegral;
    private double rightIntegral;
    
    private double lastLeftError;
    private double lastRightError;
    
    private boolean movingForward;
    
    private double kp;
    private double ki;
    private double kd;
    
    public SystemDrive() {
        super();

        leftTalon = new Talon(2); // Both bots: 2
        rightTalon = new Talon(1); //Both bots: 1
        leftDesiredEncoderDistance = 0;
        rightDesiredEncoderDistance = 0;

        driveDeadband = WiredCats2415.textReader.getValue("driveDeadband");
        driveGain = WiredCats2415.textReader.getValue("driveGain");
        
        lastLeftError = 0;
        lastRightError = 0;
        
        kp = WiredCats2415.textReader.getValue("propConstantDrive");
        ki = WiredCats2415.textReader.getValue("integralConstantDrive");
        kd = WiredCats2415.textReader.getValue("derivativeConstantDrive");
           
        System.out.println("[WiredCats] Initialized System Drive");
    }
    
    public void doDisabled(WiredCatsEvent event) {
        kp = WiredCats2415.textReader.getValue("propConstantDrive");
        ki = WiredCats2415.textReader.getValue("integralConstantDrive");
        kd = WiredCats2415.textReader.getValue("derivativeConstantDrive");
        driveDeadband = WiredCats2415.textReader.getValue("driveDeadband");
        driveGain = WiredCats2415.textReader.getValue("driveGain");
        
        leftTalon.set(0.0);
        rightTalon.set(0.0);
        leftIntegral = 0.0;
        rightIntegral = 0.0;
        
        lastLeftError = 0;
        lastRightError = 0;
    }
    
    public void doEnabled(WiredCatsEvent event) {};
    
    public void doAutonomousSpecific(WiredCatsEvent event) {
        if (event instanceof CommandNewDesiredPosition) {
              
            changeDesiredPosition(((CommandNewDesiredPosition)event).leftEncoder,
                    ((CommandNewDesiredPosition)event).rightEncoder);
        }
        else if (event instanceof EventPositionChanged) {
            leftEncoderDistance = ((EventPositionChanged)event).leftEncoder;
            rightEncoderDistance = ((EventPositionChanged)event).rightEncoder;
            
            //If we get to the setpoint, stop moving


            //compensate for error.
            double leftError = leftDesiredEncoderDistance - leftEncoderDistance;
            double rightError = rightDesiredEncoderDistance - rightEncoderDistance;
            
            //Integral
            leftIntegral += leftError;
            rightIntegral += rightError;
            
            //Derivative
            double leftDerivative = (leftError - lastLeftError);
            double rightDerivative = (rightError - lastRightError);
            
            lastLeftError = leftError;
            lastRightError = rightError;
            
            double leftPower = kp*leftError + ki*leftIntegral + kd*leftDerivative;
            double rightPower = kp*rightError + ki*rightIntegral + kd*rightDerivative;

            if (movingForward) {
                if (leftError < 0 || rightError < 0) 
                {
                    leftTalon.set(0);
                    rightTalon.set(0);
                    if (autonomous_state == AUTONOMOUS_ATTEMPTING)
                    {
                        autonomous_state = AUTONOMOUS_COMPLETED;
                        System.out.println("The bot is moving forwards, and we have made it to our destination");
                    }
                    return;
                }
            } else {
                if (leftError > 0 || rightError > 0)
                {
                    leftTalon.set(0);
                    rightTalon.set(0);
                    if (autonomous_state == AUTONOMOUS_ATTEMPTING)
                    {
                        autonomous_state = AUTONOMOUS_COMPLETED;
                        System.out.println("The bot is moving backwards, and we have made it to our destination");
                    }
                    return;
                }
            }

            System.out.println("leftPower: " + leftPower);
            System.out.println("rightPower: " + rightPower);
            System.out.println("leftError: " + leftError);
            System.out.println("rightError: " + rightError);
            System.out.println("");
            
            leftPower = capMaxValue(leftPower, 0.80); 
            rightPower = capMaxValue(rightPower, 0.80);
            
            leftTalon.set(-1*leftPower);
            rightTalon.set(rightPower);
        }
    }
    
    public void doTeleopSpecific(WiredCatsEvent event) {
        if (event instanceof EventLeftYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                leftTalon.set(linearizeTalon(((EventLeftYAxisMoved) event).y));
            }
        } else if (event instanceof EventRightYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                rightTalon.set(-1*linearizeTalon(((EventRightYAxisMoved)event).y));
            }
        }
    }

    private double linearizeTalon(double value)  {
        double a = driveGain;
        double b = driveDeadband;
        
        if (value >= 0) {
            if (value < b) return 0;
            return b + (1-b)*(a*MathUtils.pow(value, 1) + (1-a)*value);
        }
        else {
            if (value > -1*b) return 0;       
            return -1*b + (1-b)*(a*MathUtils.pow(value, 1) + (1-a)*value);
        }
    }
    
    private double capMaxValue(double d, double limit) {
        if (d > limit) {
            return limit;
        } else if (d < -limit) {
            return -limit;
        } else {
            return d;
        }
    }
    
    /**
     * Provides kickstart for PID Loops to start
     * Otherwise, bot will not move and no "EventPositionChanged" event will be emitted
     * (Messy solution)
     */
    
    private void changeDesiredPosition(double newLeftValue, double newRightValue) {
        if (leftDesiredEncoderDistance < newLeftValue + .05 &&
                leftDesiredEncoderDistance > newLeftValue - .05
                && rightDesiredEncoderDistance < newRightValue + .05
                && rightDesiredEncoderDistance > newRightValue - .05) {
            autonomous_state = AUTONOMOUS_COMPLETED;
            System.out.println("We were already at our destination.");
            return;
        } else {
            autonomous_state = AUTONOMOUS_ATTEMPTING;
        }
        
        leftIntegral = 0;
        rightIntegral = 0;
        
        if (leftDesiredEncoderDistance > newLeftValue) leftTalon.set(0.2);
        else if (leftDesiredEncoderDistance < newLeftValue) leftTalon.set(-0.2);
        else leftTalon.set(0.0);
        
        System.out.println("we were not already at our destination.");
        
        if (rightDesiredEncoderDistance > newRightValue) rightTalon.set(-0.2);
        else if (rightDesiredEncoderDistance < newRightValue) rightTalon.set(0.2);
        else rightTalon.set(0.0);
        
        leftDesiredEncoderDistance = newLeftValue;          
        rightDesiredEncoderDistance = newRightValue;  
        
        if (leftDesiredEncoderDistance < leftEncoderDistance) movingForward = false;
        else movingForward = true;
    }
    
    private int integralDrive;
    private int encoderError;
    private int desiredEncoderValue;
    
    private void gyroEncoderPID(int encoderValue, double gyro){
        
        int encoderError = desiredEncoderValue - encoderValue;
        
        integralDrive += encoderError;
        
        
    }
}
