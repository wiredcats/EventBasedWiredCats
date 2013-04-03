/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.SensorEvents.EventArmLimitReached;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Bruce Crane
 */
public class ControllerArm extends WiredCatsController {
    //private DigitalInput ls;
    private Encoder armEncoder;
    
    private DigitalInput limitSwitch;
    
    private double armAngle;
    
    private boolean needsReset;
    
    Timer timer;
    
    public ControllerArm(int limit)
    { 
        super(limit); 
        armEncoder = new Encoder(9,8);
        armAngle = armEncoder.getDistance();
        timer = new Timer();
        timer.start();
        
        needsReset = true;
        
        limitSwitch = new DigitalInput(12);
        
        System.out.println("[WiredCats] Arm Controller initialized.");
    }
    
    public void run() 
    {
        //fireEvent(new EventArmAngleChanged(this, armAngle));
        armEncoder.start();
        
        
        while (true)
        {
            
            //System.out.println("Pot reading: " + arm.getValue());
            
            double currentArmAngle = armEncoder.getDistance()/100;
//            if (armAngle != currentArmAngle)
//            {
                //System.out.println("arm angle changed. " + currentArmAngle);
                armAngle = currentArmAngle;
                fireEvent(new EventArmAngleChanged(this, armAngle, timer.get()));
//            }
        
            if (!limitSwitch.get())
            {
                if(needsReset) {
                    armEncoder.reset();
                    needsReset = false;
                }
                fireEvent(new EventArmLimitReached(this));
            }
            
//            lastLimitSwitchState = limitSwitch.get();

            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
