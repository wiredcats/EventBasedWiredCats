/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.EventButtonAPressed;
import WiredCatsEvents.GamePadEvents.EventButtonAReleased;
import WiredCatsEvents.GamePadEvents.EventButtonBPressed;
import WiredCatsEvents.GamePadEvents.EventButtonXPressed;
import WiredCatsEvents.GamePadEvents.EventButtonXReleased;
import WiredCatsEvents.GamePadEvents.EventButtonYPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYReleased;
import WiredCatsEvents.GamePadEvents.EventLeftBumperPressed;
import WiredCatsEvents.GamePadEvents.EventRightBumperPressed;
import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 *
 * @author Robotics
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
    
    public SystemIntake()
    {
        super();
        //TODO find out the victor ports.
        arm = new Victor(10);
        intakeMotor = new Victor(8);
        System.out.println("[WiredCats] Initialized system Intake");
        propConstant = WiredCats2415.textReader.getValue("propConstant");
        SmartDashboard.putNumber("Proportional Constant", propConstant);
        integralConstant = WiredCats2415.textReader.getValue("integralConstant");
        SmartDashboard.putNumber("Integral Constant", integralConstant);
        derivativeConstant = WiredCats2415.textReader.getValue("derivativeConstant");
        SmartDashboard.putNumber("Derivative Constant", derivativeConstant);
        desiredArmAngle = 4.0; //midrange on robot arm.
        SmartDashboard.putNumber("desiredAngle", desiredArmAngle);
        
        presetA = WiredCats2415.textReader.getValue("presetA");
        presetB = WiredCats2415.textReader.getValue("presetB");
        presetX = WiredCats2415.textReader.getValue("presetX");
        presetY = WiredCats2415.textReader.getValue("presetY");
    }
    
    public void doDisabled(WiredCatsEvent event) 
    {
        arm.set(0.0);
        intakeMotor.set(0.0);
    }

    public void doAutonomous(WiredCatsEvent event) 
    {
        
    }

    public void doTeleop(WiredCatsEvent event) 
    {  
        propConstant = SmartDashboard.getNumber("propConstant");
        integralConstant = SmartDashboard.getNumber("integralConstant");
        derivativeConstant = SmartDashboard.getNumber("derivativeConstant");
        
        SmartDashboard.putNumber("actualArmAngle", actualArmAngle);
        desiredArmAngle = SmartDashboard.getNumber("desiredArmAngle");
        
        
        if (event instanceof EventGamePad) handleGamePadEvents((EventGamePad)event);
        else if (event instanceof EventArmAngleChanged)
        {
            //PID loop.
            
        }
        
    }
    
    private void handleGamePadEvents(EventGamePad event)
    {
        if (event instanceof EventButtonAPressed && event.isController1())
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
        else if (event instanceof EventRightBumperPressed && event.isController1())
        {
            if (desiredArmAngle > 3.65) desiredArmAngle -= .05;
        }
        else if (event instanceof EventLeftBumperPressed && event.isController1())
        {
            if (desiredArmAngle < 4.55) desiredArmAngle += .05;
        }
            
    }
    
}
