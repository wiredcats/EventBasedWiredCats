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
    private Victor arm;
    
    private Solenoid cockOn;
    private Solenoid cockOff;
    private Solenoid fireOn;
    private Solenoid fireOff;
    private Solenoid gateUp;
    private Solenoid gateDown;
    
    Timer cockTimer = new Timer();
    Timer fireTimer = new Timer();
    Timer loopTimer = new Timer();
    
    private boolean autoShoot;
    
    private int frisbeesHeld;

    public SystemShooter() {
        super();
        wheel1 = new Victor(7);
        wheel2 = new Victor(8);
        //arm = new Victor();

        cockOn = new Solenoid(4);
        cockOff = new Solenoid(3);
        fireOn = new Solenoid(6);
        fireOff = new Solenoid(5);
        gateUp = new Solenoid(1);
        gateDown = new Solenoid(2);

        autoShoot = false;
        
        frisbeesHeld = 3;
        
        System.out.println("[WiredCats] Initialized System Shooter");
    }
    
    public void doDisabled(WiredCatsEvent event) {
        cockOn.set(false);
        cockOff.set(true);
        
        fireOn.set(true);
        fireOff.set(false);
        
        gateUp.set(true);
        gateDown.set(false);
        
        //arm.set(0.0);
    }
    
    public void doAutonomous(WiredCatsEvent event) 
    {
        
    }
    
    private void gamepadEventListener(EventGamePad eventgp) {
        if (eventgp instanceof EventRightBumperPressed && eventgp.isController1()) {
            autoShoot = true;
            
            fireOn.set(false);
            fireOff.set(true);
            cockOn.set(true);
            cockOff.set(false);
            gateDown.set(true);
            gateUp.set(false);
                
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
            fireOn.set(false);
            fireOff.set(true);
            cockOn.set(false);
            cockOff.set(true);
//                    gateUp.set(false);
//                    gateDown.set(true);
                    
            loopTimer.stop();
            loopTimer.reset();
            fireTimer.start();
            System.out.println("looped");
            return;
        }


        if (cockTimer.get() >= 0.1) {
            fireOff.set(false);
            fireOn.set(true);

            cockTimer.stop();
            cockTimer.reset();
            loopTimer.start();
            System.out.println("--fired");
            return;
        }

        if (fireTimer.get() >= 0.3) {
//          gateUp.set(false);
//          gateDown.set(true);
            fireOn.set(true);
            fireOff.set(false);
//          cockOn.set(true);
//          cockOff.set(false);

            fireTimer.stop();
            fireTimer.reset();
            cockTimer.start();
            System.out.println("-cocked");
            return;
        }
    }
    
    public void doTeleop(WiredCatsEvent event) {
        if(autoShoot) doAutoShoot();
        
        if(event instanceof EventGamePad) gamepadEventListener((EventGamePad) event);
        else {
            //System.out.println("GOT TO THIS PIONT");
            if(event instanceof EventOverDesiredSpeed) {
                if(((EventOverDesiredSpeed) event).encoderID == EventOverDesiredSpeed.ENCODER_1) {
                    wheel1.set(0.0);
                    System.out.println("bang off");
                } else wheel2.set(0.0);
            }
            else if(event instanceof EventUnderDesiredSpeed) {
                if(((EventUnderDesiredSpeed) event).encoderID == EventUnderDesiredSpeed.ENCODER_1) {
                        wheel1.set(-1.0);
                        System.out.println("bang on");
                    } else wheel2.set(-1.0);
                }
            else if (event instanceof EventFrisbeeTaken)
            {
                System.out.println("Frisbee taken in.");
                frisbeesHeld++;
            }
        }
    }
    private void cock(boolean b)
    {
        cockOff.set(!b);
        cockOn.set(b);
    }
    
    private void fire(boolean b)
    {
        fireOff.set(!b);
        fireOn.set(b);
    }
    
    private void gate(boolean b)
    {
        gateUp.set(b);
        gateDown.set(!b);
    }
}
