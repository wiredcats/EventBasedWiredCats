/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventFrisbeeTaken;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Bruce Crane
 */
public class ControllerIntake extends WiredCatsController
{

    private boolean isSwitchPressed;
    
    private DigitalInput ls;
    private AnalogChannel arm;
    
    private double armAngle;
    
    public ControllerIntake(int limit)
    { 
        super(limit); 
        ls = new DigitalInput(7);
        arm = new AnalogChannel(1);
        isSwitchPressed = false;
        armAngle = -1;
        
        System.out.println("[WiredCats] Intake Controller initialized.");
    }
    
    public void run() 
    {
        
        while (true)
        {
            SmartDashboard.putNumber("POT reading: ", arm.getVoltage());
            //get pot of absolutely up.
            //get pot of absolutely down.
            //154
            if (ls.get())
            {
                if (!isSwitchPressed) fireEvent(new EventFrisbeeTaken(this));
                isSwitchPressed = true;
            }
            else
            {
                isSwitchPressed = false;
            }
            
        }
    }
    
    public double getArm()
    {
        return arm.getVoltage();
    }
    
//    private get ArmAngle(double encoderValue) {}
    
}
