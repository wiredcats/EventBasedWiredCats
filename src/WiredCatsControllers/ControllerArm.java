/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.SensorEvents.EventArmLimitReached;
import edu.wpi.first.wpilibj.AnalogChannel;
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
    private DigitalInput upperBoundSwitch;
    
    private double armAngle;
    
    Timer timer;
    
    public ControllerArm(int limit)
    { 
        super(limit); 
        armAngle = armEncoder.get();
        timer = new Timer();
        timer.start();
        
        armEncoder = new Encoder(8,9);
        //Get Digital Input place.
        upperBoundSwitch = new DigitalInput(10);
        
        System.out.println("[WiredCats] Arm Controller initialized.");
    }
    
    public void run()
    {
        //fireEvent(new EventArmAngleChanged(this, armAngle));
        armEncoder.start();
        while (true)
        {
            //System.out.println("Pot reading: " + arm.getValue());
                
            SmartDashboard.putNumber("POT reading (getValue): ", armEncoder.getDistance());
            SmartDashboard.putNumber("Arm Encoder: ", armEncoder.get());
            
            double currentArmAngle = armEncoder.getDistance();
            if (armAngle != currentArmAngle)
            {
                armAngle = currentArmAngle;
                fireEvent(new EventArmAngleChanged(this, armAngle, timer.get()));
            }
            
            if (upperBoundSwitch.get()) armEncoder.reset();
            
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
//    private get ArmAngle(double encoderValue) {}
    
}
