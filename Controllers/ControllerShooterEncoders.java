/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Events.EventOverDesiredSpeed;
import Events.EventUnderDesiredSpeed;
import Util2415.CSVReader;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Robot;
import edu.wpi.first.wpilibj.templates.SmartDashboardUpdater;
import java.util.Vector;

/**
 *
 * @author Robotics
 */
public class ControllerShooterEncoders extends RobotController
{
    
    private Robot robot;
    
    private Encoder encoder1;
    private Encoder encoder2;
    
    private double encoder1Rate;
    private double encoder2Rate;
    
    private double desiredEncoder1Speed;
    private double desiredEncoder2Speed;
    
    public ControllerShooterEncoders(int limit, Robot robot)
    {
        super(limit, robot);
        
        //TODO get coder ports, a channel and b channel.
        encoder1 = new Encoder(1,2);
        encoder2 = new Encoder(3,4);
                
        requestedValues = new String[2];
        requestedValues[0] = "DESIRED_ENCODER_1_SPEED";
        requestedValues[1] = "DESIRED_ENCODER_2_SPEED";
        
    }
    
    

    public void run() 
    {
        encoder1.start();
        encoder2.start();
        
        while (true)
        {
            encoder1Rate = encoder1.getRate();
            encoder2Rate = encoder2.getRate();
            
            desiredEncoder1Speed = SmartDashboardUpdater.csvreader.getValue("DESIRED_ENCODER_1_SPEED");
            desiredEncoder2Speed = SmartDashboardUpdater.csvreader.getValue("DESIRED_ENCODER_2_SPEED");
             
//            System.out.println("---------------");
            //SmartDashboard.putNumber("speed of motor", encoder1Rate);
//            System.out.println("Motor 1: " + encoder1Rate);
            //SmartDashboard.putNumber("speed of second motor", encoder2Rate);
//            System.out.println("Motor 2: " + encoder2Rate);
            
            if (encoder1Rate > desiredEncoder1Speed)
            {
                //fire a too fast event.
//                System.out.println("Firing event 1 over desired speed.");
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_1));
            }
            else if (encoder1Rate < desiredEncoder1Speed)
            {
//                System.out.println("Firing event 1 under desired speed.");
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_1));
            }
            
            if (encoder2Rate > desiredEncoder2Speed)
            {
//                System.out.println("Firing event 2 over desired speed.");
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_2));
            }
            else if (encoder2Rate < desiredEncoder2Speed)
            {
//                System.out.println("Firing event 2 under desired speed.");
                fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_2));
            }
        }
    }
}
