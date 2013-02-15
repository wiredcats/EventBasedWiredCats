/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.EventLeftDriveEncoderChanged;
import WiredCatsEvents.SensorEvents.EventRightDriveEncoderChanged;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author BruceCrane
 */
public class ControllerDrive extends WiredCatsController
{

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    
    private int leftEncoderDistance;
    private int rightEncoderDistance;
    
    private int lastLeftEncoderDistance;
    private int lastRightEncoderDistance;
    
    public ControllerDrive(int limit)
    {
        super(limit);
        //TODO
        leftEncoder = new Encoder(9, 10);
        rightEncoder = new Encoder(11, 12);
        leftEncoderDistance = 0;
        rightEncoderDistance = 0;
        lastLeftEncoderDistance = 0;
        lastRightEncoderDistance = 0;
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

    public void run()
    {
        while (true)
        {
            leftEncoderDistance = leftEncoder.get();
            rightEncoderDistance = rightEncoder.get();
            
            
            if (leftEncoderDistance != lastLeftEncoderDistance)
            {
                lastLeftEncoderDistance = leftEncoderDistance;
                fireEvent( new EventLeftDriveEncoderChanged(this, leftEncoderDistance));
            }
            if (rightEncoderDistance != lastRightEncoderDistance)
            {
                lastRightEncoderDistance = rightEncoderDistance;
                fireEvent( new EventRightDriveEncoderChanged(this, rightEncoderDistance));
            }
            
        }
    }
    
}
