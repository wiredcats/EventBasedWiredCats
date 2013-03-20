package WiredCatsSystems;

import WiredCatsEvents.AutonomousCommands.CommandShoot;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.EventStateChange;
import WiredCatsEvents.GamePadEvents.*;
import WiredCatsEvents.SensorEvents.*;
import WiredCatsEvents.WiredCatsEvent;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

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
    private Solenoid gateUp;
    private Solenoid gateDown;
    
    Timer fireTimer = new Timer();
    Timer cockTimer = new Timer();
    Timer interCockTimer = new Timer();
    Timer interFireTimer = new Timer();
    
    double fireTime;
    double cockTime;
    double interCockTime;
    double interFireTime;
    
    private boolean autoShoot;
    
    private int frisbeesShot;

    public SystemShooter() {
        super();
        wheel1 = new Victor(5); // Both bots: 5
        wheel2 = new Victor(6); // Both bots: 6

        cockOn = new Solenoid(4); //competition: 4 / practice: 1
        cockOff = new Solenoid(3); //competition: 3 / practice: 2
        fireOn = new Solenoid(6); // competition: 6 / practice: 5
        fireOff = new Solenoid(5); // competition: 5 / practice 6
        gateUp = new Solenoid(1); // competition: 1 / practice 3
        gateDown = new Solenoid(2); // competition: 2 / practice 4

        autoShoot = false;
        
        frisbeesShot = 0;
        
        fireTime = WiredCats2415.textReader.getValue("fireTime");
        cockTime = WiredCats2415.textReader.getValue("cockTime");
        interCockTime = WiredCats2415.textReader.getValue("interCockTime");
        interFireTime = WiredCats2415.textReader.getValue("interFireTime");
        
        System.out.println("[WiredCats] Initialized System Shooter");
    }
    
    public void doDisabled(WiredCatsEvent event) {
        cock(false);
        fire(false);
        gate(true);
        
        fireTime = WiredCats2415.textReader.getValue("fireTime");
        cockTime = WiredCats2415.textReader.getValue("cockTime");
        interCockTime = WiredCats2415.textReader.getValue("interCockTime");
        interFireTime = WiredCats2415.textReader.getValue("interFireTime");
        
        autoShoot = false;
    }

    public void doEnabled(WiredCatsEvent event) {
        //If speed over desired, turn off motor
        if (event instanceof EventOverDesiredSpeed) {
            if (((EventOverDesiredSpeed) event).isFirstWheel) {
                wheel1.set(0.0);
            } else {
                wheel2.set(0.0);
            }
        //If speed under desired, motor is full power
        } else if (event instanceof EventUnderDesiredSpeed) {
            if (((EventUnderDesiredSpeed) event).isFirstWheel) {
                wheel1.set(1.0);
            } else {
                wheel2.set(1.0);
            }
        }
    }
    
    public void doAutonomousSpecific(WiredCatsEvent event) {
        if (event instanceof CommandShoot) {  //Assumes that only one shot per node
            autonomous_state = AUTONOMOUS_ATTEMPTING;
            cock(true);
            fire(false);
            gate(false);
            fireTimer.reset();
            fireTimer.start();
        }
    }
    
    private void gamepadEventListener(EventGamePad eventgp) {
        //Right trigger is actual fire
        //Right bumper is safety
        if (eventgp instanceof EventRightTriggerPressed && eventgp.isController1()) {
            autoShoot = true;
            
            if (!(fireTimer.get() > 0 
                    || cockTimer.get() > 0 
                    || interCockTimer.get() > 0
                    || interFireTimer.get() > 0)) { //If none of the timers are going, reset back to the beginning
                cock(true);
                fire(false);
                gate(true);
                fireTimer.start();
            } 
        } else if (eventgp instanceof EventRightTriggerReleased && eventgp.isController1()) {
            autoShoot = false;
        } else if (eventgp instanceof EventLeftTriggerPressed && eventgp.isController1())
        {
            cock(true);
        }
    }
    
    private void doAutoShoot() {
        //First time firing / loop to beginning
        if (fireTimer.get() > fireTime) { //If we're not getting up to speed, we should be relatively at speed
            cock(false);
            fire(false);
            gate(false);
            fireTimer.stop();
            fireTimer.reset();
            interCockTimer.start();
            frisbeesShot++;
        }
        
        if (interCockTimer.get() >= interCockTime)
        {
            cock(false);
            fire(true);
            gate(false);
            interCockTimer.stop();
            interCockTimer.reset();
            cockTimer.start();
        }

        //Cocking is finished, so reset back to original position
        if (cockTimer.get() > cockTime) {
            cock(true);
            fire(false);
            gate(false);
            cockTimer.stop();
            cockTimer.reset();
            interFireTimer.start();
        }

        //Reset everything back to resting state
        if (interFireTimer.get() > interFireTime) {
            cock(false);
            fire(false);
            interFireTimer.stop();
            interFireTimer.reset();
            
            if (autoShoot) { //Begin the cycle again
                fireTimer.start();
                gate(false);
            } else { //Otherwise, put the gate up
                gate(true);
                if (autonomous_state == AUTONOMOUS_ATTEMPTING) {
                    autonomous_state = AUTONOMOUS_COMPLETED; //Tells autonomous that we are done
                }
            }
        }
    }
    
    public void update() {
        doAutoShoot();
    }
    
    public void doTeleopSpecific(WiredCatsEvent event) {    
        if(event instanceof EventGamePad) gamepadEventListener((EventGamePad) event);
    }
    private void cock(boolean b) {
        cockOff.set(!b);
        cockOn.set(b);
    }
    
    private void fire(boolean b) {
        fireOff.set(!b);
        fireOn.set(b);
    }
    
    private void gate(boolean b) {
        gateUp.set(b);
        gateDown.set(!b);
    }
    
    public int getFrisbeesShot() { return frisbeesShot; }
}
