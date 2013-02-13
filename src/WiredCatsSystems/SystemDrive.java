/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.WiredCatsEvent;
import Util2415.TXTReader;

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

    private Talon left1;
    private Talon left2;
    private Talon left3;
    private Talon right1;
    private Talon right2;
    private Talon right3;
    
    private double driveDeadband;
    
    private double driveGain;

    public SystemDrive() {
        super();

        left1 = new Talon(4);
        left2 = new Talon(5);
        left3 = new Talon(10);

        right1 = new Talon(1);
        right2 = new Talon(2);
        right3 = new Talon(3);
        driveDeadband = WiredCats2415.textReader.getValue("driveDeadband");
        driveGain = WiredCats2415.textReader.getValue("driveGain");
        SmartDashboard.putNumber("driveDeadband", driveDeadband);
        SmartDashboard.putNumber("driveGain", driveGain);
        
        System.out.println("[WiredCats] Initialized System Drive");

    }
    
    public void doDisabled(WiredCatsEvent event) {
        left1.set(0.0);
        left2.set(0.0);
        left3.set(0.0);
        
        right1.set(0.0);
        right2.set(0.0);
        right3.set(0.0);
    };
    
    public void doAutonomous(WiredCatsEvent event) 
    {
            
    };
    
    public void doTeleop(WiredCatsEvent event) 
    {
        driveDeadband = SmartDashboard.getNumber("driveDeadband");
        driveGain = SmartDashboard.getNumber("driveGain");
        
        
        if (event instanceof EventLeftYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                left1.set(setVictorValues(((EventLeftYAxisMoved) event).y));
                left2.set(setVictorValues(((EventLeftYAxisMoved) event).y));
                left3.set(setVictorValues(((EventLeftYAxisMoved) event).y));
                System.out.println(setVictorValues(((EventLeftYAxisMoved) event).y));
            }
        } else if (event instanceof EventRightYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                right1.set(setVictorValues(((EventRightYAxisMoved) event).y));
                right2.set(setVictorValues(((EventRightYAxisMoved) event).y));
                right3.set(setVictorValues(((EventRightYAxisMoved) event).y));
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
        //TODO: Fill this out with a function according to driver preferences
        
        double a = WiredCats2415.textReader.getValue("driveGain");
        double b = WiredCats2415.textReader.getValue("driveDeadband");
        
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
