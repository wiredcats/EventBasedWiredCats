package WiredCatsControllers;

import WiredCatsEvents.SensorEvents.*;

import Util2415.TXTReader;
import edu.wpi.first.wpilibj.Counter;
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
    
    private Counter encoder1;
    private Counter encoder2;
    
    public double encoder1Rate;
    public double encoder2Rate;

    private double desiredEncoder1Speed;
    private double desiredEncoder2Speed;
    
    private Timer timer;
            
    private WiredCats2415 robot;

    public ControllerShooter(int limit, WiredCats2415 robot) {
        super(limit);
        
        this.robot = robot;

        timer = new Timer();
        timer.start();
        
        //Wheel 1 is first wheel to touch frisbee
        encoder2 = new Counter(7); //competition: 9,8 / practice: 1,2
        encoder1 = new Counter(6); //competition: 6,7 / practice: 4,3
        
        desiredEncoder1Speed = WiredCats2415.textReader.getValue("desiredEncoder1Speed");
        desiredEncoder2Speed = WiredCats2415.textReader.getValue("desiredEncoder2Speed");
        
        System.out.println("[WiredCats] Shooter Controller initialized.");
    }

    public void run() {
        encoder1.start();
        encoder2.start();
        
        while (true) {
            
            //NOTE NOT ACTUALLY RATE
            //TODO: FIX THIS
              encoder1Rate = (encoder1.get() / (encoder1.getPeriod() * 120));
              encoder2Rate = (encoder2.get() / (encoder2.getPeriod() * 120));
              SmartDashboard.putNumber("Wheel 1 Speed", encoder1Rate);
              SmartDashboard.putNumber("Wheel 2 Speed", encoder2Rate);
            
              if (robot.isDisabled()) {
                     desiredEncoder1Speed = SmartDashboard.getNumber("desiredEncoder1Speed");
                     desiredEncoder2Speed = SmartDashboard.getNumber("desiredEncoder2Speed");
              }
              else {
                if (encoder1Rate > desiredEncoder1Speed){
                     fireEvent(new EventOverDesiredSpeed(this, true));
                } else if (encoder1Rate < desiredEncoder1Speed){
                     fireEvent(new EventUnderDesiredSpeed(this, true));
                }

                if (encoder2Rate > desiredEncoder2Speed){
                     fireEvent(new EventOverDesiredSpeed(this, false));
                } else if (encoder2Rate < desiredEncoder2Speed){
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
}
