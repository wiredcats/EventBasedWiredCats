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
    
    private double encoder1Rate;
    private double encoder2Rate;
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
        encoder2 = new Encoder(2, 1);
        encoder1 = new Encoder(3, 4);
        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
        SmartDashboard.putNumber("desiredEncoder1Speed", desiredEncoder1Speed);
        SmartDashboard.putNumber("desiredEncoder2Speed", desiredEncoder2Speed);
        desiredRange = WiredCats2415.textReader.getValue("shooterWheelRange");
        
        System.out.println("[WiredCats] Shooter Controller initialized.");
    }

    public void run() {
        encoder1.start();
        encoder2.start();
        
        if (robot.isDisabled())
        {
            desiredEncoder1Speed = SmartDashboard.getNumber("desiredEncoder1Speed");
            desiredEncoder2Speed = SmartDashboard.getNumber("desiredEncoder2Speed");
        }
        
        //get values from file, then 
//        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
//        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
//        SmartDashboard.putNumber("desiredEncoderWheel1Speed", desiredEncoder1Speed);
//        SmartDashboard.putNumber("desiredEncoderWheel2Speed", desiredEncoder2Speed);

        while (true) {
              if (robot.isDisabled()) {
                     desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
                     desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
              }
              else {
                encoder1Rate = encoder1.getRate() / 2; //convertig fromt ticks per second, to revolutions per minute.
                encoder2Rate = encoder2.getRate() / 2;
//                System.out.println(encoder1Rate + " " + desiredEncoder1Speed);
//                System.out.println("2 "+ encoder2Rate + " " + desiredEncoder2Speed);
                if (encoder1Rate > desiredEncoder1Speed){// && !has1BeenOver) {
                     has1BeenOver = true;
//                     System.out.println("1st over desired speed");
                     fireEvent(new EventOverDesiredSpeed(this, true));
                } else if (encoder1Rate < desiredEncoder1Speed){// && has1BeenOver){
                     fireEvent(new EventUnderDesiredSpeed(this, true));
//                     System.out.println("1st under desired speed");
                     has1BeenOver = false;
                }

                if (encoder2Rate > desiredEncoder2Speed){// && !has2BeenOver) {
                     has2BeenOver = true;
                     fireEvent(new EventOverDesiredSpeed(this, false));
//                     System.out.println("2nd over desired speed");
                } else if (encoder2Rate < desiredEncoder2Speed){// && has2BeenOver) {
                     has2BeenOver = false;
//                     System.out.println("2nd over desired speed");
                     fireEvent(new EventUnderDesiredSpeed(this, false));
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
