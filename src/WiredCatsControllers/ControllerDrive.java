/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author BruceCrane
 */
public class ControllerDrive extends WiredCatsController
{

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    
    public ControllerDrive(int limit)
    {
        super(limit);
        //TODO
        leftEncoder = new Encoder(9, 10);
        rightEncoder = new Encoder(11, 12);
        System.out.println("[WiredCats] Drive Controller Initialized.");
    }
    
    public void run() 
    {
        //TODO eventually a PID loop for autonomous?
        //it really does nothing except for hold values,
        //so I guess just let the run method run out.
    }
    
    
    /**
     * Called by the EventLogger, WiredCats2415, or Autonomous mode,
     * It resets the encoder values to 0 ticks.
     */
    public void resetEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
    }
    
    public int getLeftTicks()
    {
        return leftEncoder.get();
    }
    
    public int getRightTicks()
    {
        return rightEncoder.get();
    }
    
}
