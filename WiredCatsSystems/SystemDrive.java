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

/**
 * The system that controls everything drive train.
 *
 * @author BruceCrane
 */
public class SystemDrive extends WiredCatsSystem {

    private Talon left1;
    private Talon left2;
    private Talon left3;
    private Talon right1;
    private Talon right2;
    private Talon right3;

    public SystemDrive() {
        super();

        left1 = new Talon(4);
        left2 = new Talon(5);
        left3 = new Talon(6);

        right1 = new Talon(1);
        right2 = new Talon(2);
        right3 = new Talon(3);

        System.out.println("Drive system initialized.");

    }
    
    public void doDisabled(WiredCatsEvent event) {
        left1.set(0.0);
        left2.set(0.0);
        left3.set(0.0);
        
        right1.set(0.0);
        right2.set(0.0);
        right3.set(0.0);
    };
    
    public void doAutonomous(WiredCatsEvent event) {
            
    };
    
    public void doTeleop(WiredCatsEvent event) {
        if (event instanceof EventLeftYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                left1.set(((EventLeftYAxisMoved) event).y);
                left2.set(((EventLeftYAxisMoved) event).y);
                left3.set(((EventLeftYAxisMoved) event).y);
            }
        } else if (event instanceof EventRightYAxisMoved) {
            if (((EventGamePad) event).isController1()) {
                right1.set(((EventRightYAxisMoved) event).y);
                right2.set(((EventRightYAxisMoved) event).y);
                right3.set(((EventRightYAxisMoved) event).y);
            }
        }
    }

    /**
     * This figures out all the things that are needed to consider when setting the victor value. 
     *
     * @param value
     * @return
     */
    private double setVictorValues(double value) {
        //TODO: Fill this out with a function according to driver preferences
        return value;
    }
}
