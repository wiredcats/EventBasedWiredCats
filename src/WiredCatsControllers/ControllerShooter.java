package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.*;

import Util2415.TXTReader;
import edu.wpi.first.wpilibj.Encoder;
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
    
    private boolean is1Over;
    private boolean is2Over;

    public ControllerShooter(int limit) {
        super(limit);

        //TODO get coder ports, a channel and b channel.
        encoder2 = new Encoder(2, 1);
        encoder1 = new Encoder(3, 4);
        is1Over = false;
        is2Over = false;
        
        System.out.println("[WiredCats] Shooter Controller initialized.");
    }

    public void run() {
        encoder1.start();
        encoder2.start();
        
        //get values from file, then 
        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
        SmartDashboard.putNumber("desiredEncoderWheel1Speed", desiredEncoder1Speed);
        SmartDashboard.putNumber("desiredEncoderWheel2Speed", desiredEncoder2Speed);

        while (true) {
            encoder1Rate = encoder1.getRate() / 2; //convertig fromt ticks per second, to revolutions per minute.
            encoder2Rate = encoder2.getRate() / 2;
            
            SmartDashboard.putNumber("Wheel 1 Speed", encoder1Rate);
            SmartDashboard.putNumber("Wheel 2 Speed", encoder2Rate);
            desiredEncoder1Speed = SmartDashboard.getNumber("desiredEncoderWheel1Speed");
            desiredEncoder2Speed = SmartDashboard.getNumber("desiredEncoderWheel2Speed");
            
            //Bang-Bang control system
            if (encoder1Rate > desiredEncoder1Speed) {
//                System.out.println("over desired speed.");
                if (!is1Over) fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_1));
                is1Over = true;
            } else if (encoder1Rate < desiredEncoder1Speed) {
//              System.out.println("under desired speed.");
                if (is1Over) fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_1));
                is1Over = false;
            }

            if (encoder2Rate > desiredEncoder2Speed) {
                if (!is2Over) fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_2));
                is2Over = true;
            } else if (encoder2Rate < desiredEncoder2Speed) {
                if (is2Over) fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_2));
                is2Over = false;
            }
        }
    }
}
