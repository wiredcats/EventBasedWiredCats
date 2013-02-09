/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsSystems;

import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.SensorEvents.*;
import WiredCatsEvents.WiredCatsEvent;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

/**
 * The System that controls the shooter mechanism
 *
 * @author Robotics
 */
public class SystemShooter extends WiredCatsSystem {

    private Victor wheel1;
    private Victor wheel2;
    
    private Solenoid cockOn;
    private Solenoid cockOff;
    private Solenoid fireOn;
    private Solenoid fireOff;
    
    Timer cockTimer = new Timer();
    Timer fireTimer = new Timer();
    Timer loopTimer = new Timer();
    
    private boolean autoShoot;

    public SystemShooter() {
        super();
        wheel1 = new Victor(8);
        wheel2 = new Victor(7);

        cockOn = new Solenoid(4);
        cockOff = new Solenoid(3);
        fireOn = new Solenoid(6);
        fireOff = new Solenoid(5);

        autoShoot = false;
    }
    
    public void doDisabled(WiredCatsEvent event) {
        cockOn.set(true);
        cockOff.set(false);
        
        fireOn.set(false);
        fireOff.set(true);
    }
    
    public void doAutonomous(WiredCatsEvent event) {
        
    }
    
    private void gamepadEventListener(EventGamePad eventgp) {
        if (eventgp instanceof EventRightBumperPressed && eventgp.isController1()) {
            autoShoot = true;
            
            fireOn.set(true);
            fireOff.set(false);
            cockOn.set(false);
            cockOff.set(true);

            loopTimer.stop();
            loopTimer.reset();
            cockTimer.stop();
            cockTimer.reset();

            fireTimer.start();

        } else if (eventgp instanceof EventRightBumperReleased && eventgp.isController1()) {
            autoShoot = false;
        }
    }
    
    public void doAutoShoot() {
        if (loopTimer.get() >= 0.5) {
            fireOn.set(true);
            fireOff.set(false);
            cockOn.set(false);
            cockOff.set(true);

            loopTimer.stop();
            loopTimer.reset();
            fireTimer.start();
            System.out.println("looped");
            return;
        }


        if (cockTimer.get() >= 0.1) {
            cockOn.set(false);
            cockOff.set(true);

            cockTimer.stop();
            cockTimer.reset();
            loopTimer.start();
            System.out.println("-cocked");
            return;
        }

        if (fireTimer.get() >= 0.3) {
            fireOn.set(false);
            fireOff.set(true);
            cockOn.set(true);
            cockOff.set(false);

            fireTimer.stop();
            fireTimer.reset();
            cockTimer.start();
            System.out.println("--fired");
            return;
        }
    }
    
    public void doTeleop(WiredCatsEvent event) {
        if(autoShoot) doAutoShoot();
        
        if(event instanceof EventGamePad) gamepadEventListener((EventGamePad) event);
        else {
            if(event instanceof EventOverDesiredSpeed) {
                if(((EventOverDesiredSpeed) event).encoderID == EventOverDesiredSpeed.ENCODER_1) {
                    wheel1.set(0.0);
                } else wheel2.set(0.0);
            if(event instanceof EventUnderDesiredSpeed) {
                if(((EventUnderDesiredSpeed) event).encoderID == EventUnderDesiredSpeed.ENCODER_1) {
                        wheel1.set(1.0);
                    } else wheel2.set(1.0);
                }
            }
        }
    }
}
