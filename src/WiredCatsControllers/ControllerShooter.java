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
    
    private boolean is1InRange;
    private boolean is2InRange;
    
    private boolean is1Over;
    private boolean is2Over;
    
    private Timer timer;

    public ControllerShooter(int limit) {
        super(limit);

        timer = new Timer();
        timer.start();
        
        //TODO get coder ports, a channel and b channel.
        encoder2 = new Encoder(2, 1);
        encoder1 = new Encoder(3, 4);
        is1Over = true;
        is2Over = true;
        is1InRange = false;
        is2InRange = false;
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
        
        //get values from file, then 
//        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
//        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
//        SmartDashboard.putNumber("desiredEncoderWheel1Speed", desiredEncoder1Speed);
//        SmartDashboard.putNumber("desiredEncoderWheel2Speed", desiredEncoder2Speed);

        while (true) {
            
            

            
//            System.out.println(encoder1Rate);
//            System.out.println(desiredEncoder1Speed);
//            System.out.println(is1Over);
                SmartDashboard.putNumber("Wheel 1 Speed", encoder1Rate);
                SmartDashboard.putNumber("Wheel 2 Speed", encoder2Rate);
                desiredEncoder1Speed = SmartDashboard.getNumber("desiredEncoder1Speed");
                desiredEncoder2Speed = SmartDashboard.getNumber("desiredEncoder2Speed");
                
                encoder1Rate = encoder1.getRate() / 2; //convertig fromt ticks per second, to revolutions per minute.
                encoder2Rate = encoder2.getRate() / 2;
                
                timer.stop();
                timer.reset();
                timer.start();
                if (encoder1Rate > desiredEncoder1Speed){// && !is1Over) {
                //System.out.println("over desired speed.");
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_1));
            } else if (encoder1Rate < desiredEncoder1Speed){// && is1Over) {
                fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_1));
                //System.out.println("under desired speed.");
            }
//            if (encoder1Rate - desiredEncoder1Speed < desiredRange ||
//                    encoder1Rate - desiredEncoder1Speed > -1*desiredRange)
//            {
//                if (!is1InRange) fireEvent(new EventFirstShooterWheelWithinRange(this));
//                is1InRange = true;
//            }
//            if (encoder2Rate - desiredEncoder2Speed < desiredRange ||
//                    encoder2Rate - desiredEncoder2Speed > -1*desiredRange)
//            {
//                if (!is2InRange) fireEvent(new EventSecondShooterWheelWithinRange(this));
//                is1InRange = true;
//            }

            if (encoder2Rate > desiredEncoder2Speed) {
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_2));
              
            } else if (encoder2Rate < desiredEncoder2Speed) {
                fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_2));
              
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
            //Bang-Bang control system************************
    }
}
