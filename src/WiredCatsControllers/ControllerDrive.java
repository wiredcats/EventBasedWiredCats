/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventPositionChanged;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author BruceCrane
 */
public class ControllerDrive extends WiredCatsController
{

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private Gyro gyro;
    
    private int leftEncoderDistance;
    private int rightEncoderDistance;
    
    private int lastLeftEncoderDistance;
    private int lastRightEncoderDistance;
    
    public double lastGyroValue;
    
    private double gyroValue;
    
    private Timer timer;
    
    public ControllerDrive(int limit)
    {
        super(limit);
        //TODO
        leftEncoder = new Encoder(7, 8);  // 7, 8
        rightEncoder = new Encoder(5, 6);  //5, 6
        gyro = new Gyro(2);
        leftEncoderDistance = 0;
        rightEncoderDistance = 0;
        lastLeftEncoderDistance = 0;
        lastRightEncoderDistance = 0;
        
        timer = new Timer();
        timer.start();
        
        lastGyroValue = 0;
        System.out.println("[WiredCats] Drive Controller Initialized.");
    }
    
    /**
     * Called by the EventLogger, WiredCats2415, or Autonomous mode,
     * It resets the encoder values to 0 ticks.
     */
    public void resetEncoders(){
        leftEncoder.reset();
        rightEncoder.reset();
    }
    
    public int getLeftTicks(){
        return leftEncoder.get();
    }
    
    public int getRightTicks(){
        return rightEncoder.get();
    }
    
    public double getGyro()
    {
        return gyro.getAngle();
    }

    public void run()
    {
        leftEncoder.start();
        rightEncoder.start();
        while (true)
        {
                
            leftEncoderDistance = leftEncoder.get();
            rightEncoderDistance = rightEncoder.get();
            gyroValue = gyro.getAngle();
            
            SmartDashboard.putNumber("leftEncoderDistance", leftEncoderDistance);
            SmartDashboard.putNumber("rightEncoderDistance", rightEncoderDistance);
            
            if (leftEncoderDistance != lastLeftEncoderDistance ||
                   rightEncoderDistance != lastRightEncoderDistance)
            {
                lastLeftEncoderDistance = leftEncoderDistance;
                lastRightEncoderDistance = rightEncoderDistance;
                lastGyroValue = gyroValue;
                fireEvent( new EventPositionChanged(this, leftEncoderDistance, rightEncoderDistance, timer.get()));
            }
            
            SmartDashboard.putNumber("gyro", gyroValue);
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    } 
}
