/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventArmAngleChanged;
import WiredCatsEvents.SensorEvents.EventFrisbeeTaken;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Bruce Crane
 */
public class ControllerArm extends WiredCatsController
{

    
    //private DigitalInput ls;
    private AnalogChannel arm;
    
    private double armAngle;
    
    Timer timer;
    
    public ControllerArm(int limit)
    { 
        super(limit); 
        //ls = new DigitalInput(7);
        arm = new AnalogChannel(1);
        armAngle = arm.getValue();
        timer = new Timer();
        timer.start();
        
        System.out.println("[WiredCats] Arm Controller initialized.");
    }
    
    public void run() 
    {
        //fireEvent(new EventArmAngleChanged(this, armAngle));
        while (true)
        {
            //System.out.println("Pot reading: " + arm.getValue());
                
            SmartDashboard.putNumber("POT reading (getValue): ", arm.getValue());
            //System.out.println("Arm Value: " + arm.getValue());
                //System.out.println(arm.getValue());
            //SmartDashboard.putNumber("POT reading (getVoltage): ", arm.getVoltage());
            //get pot of absolutely up.
            //get pot of absolutely down.
            //154
            
            double currentArmAngle = arm.getValue();
            if (armAngle != currentArmAngle)
            {
                //System.out.println("arm angle changed. " + currentArmAngle);
                armAngle = currentArmAngle;
                fireEvent(new EventArmAngleChanged(this, armAngle, timer.get()));
            }

            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
//    private get ArmAngle(double encoderValue) {}
    
}
