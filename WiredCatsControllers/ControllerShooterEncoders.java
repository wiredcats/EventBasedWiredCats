package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.*;

import Util2415.TXTReader;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 * Fires events based on encoders used in the shooter.
 *
 * @author Robotics
 */

public class ControllerShooterEncoders extends WiredCatsController {

    private WiredCats2415 robot;
    private Encoder encoder1;
    private Encoder encoder2;
    
    private double encoder1Rate;
    private double encoder2Rate;
    private double desiredEncoder1Speed;
    private double desiredEncoder2Speed;

    public ControllerShooterEncoders(int limit, WiredCats2415 robot) {
        super(limit, robot);

        //TODO get coder ports, a channel and b channel.
        encoder1 = new Encoder(1, 2);
        encoder2 = new Encoder(4, 3);
    }

    public void run() {
        encoder1.start();
        encoder2.start();

        while (true) {
            encoder1Rate = encoder1.getRate() / 2; //convertig fromt ticks per second, to revolutions per minute.
            encoder2Rate = encoder2.getRate() / 2;

            desiredEncoder1Speed = 5000;
            desiredEncoder2Speed = 7000;
            
            SmartDashboard.putNumber("Wheel 1 Speed", encoder1Rate);
            SmartDashboard.putNumber("Wheel 2 Speed", encoder2Rate);
            
            //Bang-Bang control system
            if (encoder1Rate > desiredEncoder1Speed) {
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_1));
            } else if (encoder1Rate < desiredEncoder1Speed) {
                fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_1));
            }

            if (encoder2Rate > desiredEncoder2Speed) {
                fireEvent(new EventOverDesiredSpeed(this, EventOverDesiredSpeed.ENCODER_2));
            } else if (encoder2Rate < desiredEncoder2Speed) {
                fireEvent(new EventUnderDesiredSpeed(this, EventUnderDesiredSpeed.ENCODER_2));
            }
        }
    }
}
