package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.*;

import Util2415.TXTReader;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 * Fires events based on encoders used in the shooter.
 *
 * @author Bruce Crane
 */

public class ControllerShooter extends WiredCatsController {
    
    private Encoder encoder1;
    private Encoder encoder2;
    
    public double encoder1Rate;
    public double encoder2Rate;
    private double desiredEncoder1SpeedPyramid;
    private double desiredEncoder2SpeedPyramid;
    private double desiredEncoder1SpeedCourt;
    private double desiredEncoder2SpeedCourt;
    
    private double desiredEncoder1Speed;
    private double desiredEncoder2Speed;
    
    private double desiredRange;
    
    private boolean has1BeenOver;
    private boolean has2BeenOver;
    
    private Timer timer;
            
    private WiredCats2415 robot;

    public ControllerShooter(int limit, WiredCats2415 robot) {
        super(limit);
        
        this.robot = robot;

        timer = new Timer();
        timer.start();
        
        has1BeenOver = false;
        has2BeenOver = false;
        
        //TODO get coder ports, a channel and b channel.
        encoder2 = new Encoder(1, 2); //1, 2,
        encoder1 = new Encoder(4, 3); //3, 4
        desiredEncoder1SpeedPyramid = WiredCats2415.textReader.getValue("desiredEncoder1SpeedPyramid");
        desiredEncoder2SpeedPyramid = WiredCats2415.textReader.getValue("desiredEncoder2SpeedPyramid");
        SmartDashboard.putNumber("desiredEncoder1SpeedPyramid", desiredEncoder1SpeedPyramid);
        SmartDashboard.putNumber("desiredEncoder2SpeedPyramid", desiredEncoder2SpeedPyramid);
        desiredEncoder1SpeedCourt = WiredCats2415.textReader.getValue("desiredEncoder1SpeedCourt");
        desiredEncoder2SpeedCourt = WiredCats2415.textReader.getValue("desiredEncoder2SpeedCourt");
        SmartDashboard.putNumber("desiredEncoder1SpeedCourt", desiredEncoder1SpeedCourt);
        SmartDashboard.putNumber("desiredEncoder2SpeedCourt", desiredEncoder2SpeedCourt);
        desiredRange = WiredCats2415.textReader.getValue("shooterWheelRange");
        
        desiredEncoder1Speed = desiredEncoder1SpeedPyramid;
        desiredEncoder2Speed = desiredEncoder2SpeedPyramid;
        
        System.out.println("[WiredCats] Shooter Controller initialized.");
    }

    public void run() {
        encoder1.start();
        encoder2.start();
        
        if (robot.isDisabled())
        {
            desiredEncoder1SpeedPyramid = SmartDashboard.getNumber("desiredEncoder1SpeedPyramid");
            desiredEncoder2SpeedPyramid = SmartDashboard.getNumber("desiredEncoder2SpeedPyramid");
            desiredEncoder1SpeedCourt = SmartDashboard.getNumber("desiredEncoder1SpeedCourt");
            desiredEncoder2SpeedCourt = SmartDashboard.getNumber("desiredEncoder2SpeedCourt");
        }
        
        //get values from file, then 
//        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
//        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
//        SmartDashboard.putNumber("desiredEncoderWheel1Speed", desiredEncoder1Speed);
//        SmartDashboard.putNumber("desiredEncoderWheel2Speed", desiredEncoder2Speed);

        while (true) {
            
              encoder1Rate = encoder1.getRate() / 2; //convertig fromt ticks per second, to revolutions per minute.
              encoder2Rate = encoder2.getRate() / 2;
              SmartDashboard.putNumber("Wheel 1 Speed", encoder1Rate);
              SmartDashboard.putNumber("Wheel 2 Speed", encoder2Rate);
            
              if (robot.isDisabled()) {
                     desiredEncoder1SpeedPyramid = SmartDashboard.getNumber("desiredEncoder1SpeedPyramid");
                     desiredEncoder2SpeedPyramid = SmartDashboard.getNumber("desiredEncoder2SpeedPyramid");
                     desiredEncoder1SpeedCourt = SmartDashboard.getNumber("desiredEncoder1SpeedCourt");
                     desiredEncoder2SpeedCourt = SmartDashboard.getNumber("desiredEncoder2SpeedCourt");
              }
              else {
//                System.out.println(encoder1Rate + " " + desiredEncoder1Speed);
//                System.out.println("2 "+ encoder2Rate + " " + desiredEncoder2Speed);
                if (encoder1Rate > desiredEncoder1Speed){// && !has1BeenOver) {
                     has1BeenOver = true;
                     fireEvent(new EventOverDesiredSpeed(this, true));
                } else if (encoder1Rate < desiredEncoder1Speed){// && has1BeenOver){
                     fireEvent(new EventUnderDesiredSpeed(this, true));
                     has1BeenOver = false;
                }

                if (encoder2Rate > desiredEncoder2Speed){// && !has2BeenOver) {
                     has2BeenOver = true;
                     fireEvent(new EventOverDesiredSpeed(this, false));
                } else if (encoder2Rate < desiredEncoder2Speed){// && has2BeenOver) {
                     has2BeenOver = false;
                     fireEvent(new EventUnderDesiredSpeed(this, false));
                }
                try {
                    Thread.sleep(9);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void fullCourt()
    {
        desiredEncoder1Speed = desiredEncoder1SpeedCourt;
        desiredEncoder2Speed = desiredEncoder2SpeedCourt;
        SmartDashboard.putNumber("desiredEncoder1Speed", desiredEncoder1Speed);
        SmartDashboard.putNumber("desiredEncoder2Speed", desiredEncoder2Speed);
    }
    
    public void pyramidShot()
    {
        desiredEncoder1Speed = desiredEncoder1SpeedPyramid;
        desiredEncoder2Speed = desiredEncoder2SpeedPyramid;
        SmartDashboard.putNumber("desiredEncoder1Speed", desiredEncoder1Speed);
        SmartDashboard.putNumber("desiredEncoder2Speed", desiredEncoder2Speed);
    }
}
