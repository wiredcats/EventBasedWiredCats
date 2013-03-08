/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    Timer xTimer = new Timer();
    Timer yTimer = new Timer();
    Timer zTimer = new Timer();
    
    double xTime;
    double yTime;
    double zTime;
    
    private boolean autoShoot;
    
    private int frisbeesShot;
    
    private boolean is1BangOn;
    private boolean is2BangOn;
    
    private boolean isRightBumperDown;

    public SystemShooter() {
        super();
        wheel1 = new Victor(6);
        wheel2 = new Victor(5);

        cockOn = new Solenoid(1);
        cockOff = new Solenoid(2);
        fireOn = new Solenoid(5);
        fireOff = new Solenoid(6);
        gateUp = new Solenoid(3);
        gateDown = new Solenoid(4);

        autoShoot = false;
        
        frisbeesShot = 0;
        is1BangOn = true;
        is2BangOn = true;
        
        xTime = WiredCats2415.textReader.getValue("xTime");
        SmartDashboard.putNumber("xTime", xTime);
        yTime = WiredCats2415.textReader.getValue("yTime");
        SmartDashboard.putNumber("yTime", yTime);
        zTime = WiredCats2415.textReader.getValue("zTime");
        SmartDashboard.putNumber("zTime", zTime);
        
        System.out.println("[WiredCats] Initialized System Shooter");
    }
    
    public void doDisabled(WiredCatsEvent event) {
        cock(false);
        
        fire(false);
        
        gate(true);
        
        xTime = SmartDashboard.getNumber("xTime");
        yTime = SmartDashboard.getNumber("yTime");
        zTime = SmartDashboard.getNumber("zTime");
        
    }
    
    public void doAutonomousSpecific(WiredCatsEvent event) 
    {
        if (event instanceof CommandShoot)
        {
            //autoshoot = true;
        }
    }
    
    private void gamepadEventListener(EventGamePad eventgp) {
        if (eventgp instanceof EventRightTriggerPressed && eventgp.isController1() && isRightBumperDown) 
        {
            autoShoot = true;
            if (!(xTimer.get()>0 || yTimer.get() >0 || zTimer.get() >0))
            {
                cock(true);
                fire(false);
                gate(false);
                xTimer.start();
                //System.out.println("0");
            }
        } else if (eventgp instanceof EventRightTriggerReleased && eventgp.isController1()) {
            autoShoot = false;
        }
        else if (eventgp instanceof EventRightBumperPressed && eventgp.isController1())
        {
            isRightBumperDown = true;
        }
        else if (eventgp instanceof EventRightBumperReleased && eventgp.isController1())
        {
            isRightBumperDown = false;
        }
    }
    
    private void doAutoShoot() 
    {
            if (xTimer.get() > xTime && !is1BangOn && !is2BangOn)
            {
                cock(false);
                fire(true);
                gate(false);
                xTimer.stop();
                xTimer.reset();
                yTimer.start();
                frisbeesShot++; 
            }
            
            if (yTimer.get() > yTime)
            {
                cock(true);
                fire(false);
                gate(false);
                yTimer.stop();
                yTimer.reset();
                zTimer.start();
                //System.out.println("Y");
            }
            
            if (zTimer.get() > zTime)
            {
                cock(false);
                fire(false);
                zTimer.stop();
                zTimer.reset();
                //System.out.println("Z");
                if (autoShoot)
                {
                    xTimer.start();
                    gate(false);
                }
                else
                {
                    gate(true);
                }
            }
    }
    
    public void update()
    {
        doAutoShoot();
    }
    
    public void doEnabled(WiredCatsEvent event)
    {
            if(event instanceof EventOverDesiredSpeed) {
                if(((EventOverDesiredSpeed) event).isFirstWheel) {
                    wheel1.set(0.0);
                    is1BangOn = false;
//                    System.out.println("1 bang off");
                } else {
                    wheel2.set(0.0);     
//                    System.out.println("2 bang off");
                    is2BangOn = false;
                }
                
            }
            else if(event instanceof EventUnderDesiredSpeed) {
                if(((EventUnderDesiredSpeed) event).isFirstWheel) {
                        wheel1.set(-1.0);
//                        System.out.println("1 bang on");
                        is1BangOn = true;
                    } else{
                        wheel2.set(-1.0);
//                        System.out.println("2 bang on");
                        is2BangOn = true;
                    }
                }
    }
    
    public void doTeleopSpecific(WiredCatsEvent event) 
    {    
        //System.out.println(events.getSize());
        if(event instanceof EventGamePad) gamepadEventListener((EventGamePad) event);
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
    
    public int getFrisbeesShot()
    {
        return frisbeesShot;
    }
}
