package WiredCatsSystems;

import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.GamePadEvents.EventButtonAPressed;
import WiredCatsEvents.GamePadEvents.EventButtonXPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYPressed;
import WiredCatsEvents.GamePadEvents.EventButtonYReleased;
import WiredCatsEvents.WiredCatsEvent;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Robotics
 */
public class SystemHang extends WiredCatsSystem
{

    private Solenoid hangOff;
    private Solenoid hangOn;
    
    private boolean isUp;
    
    public SystemHang()
    {
        super();
        hangOn = new Solenoid(7);
        hangOff = new Solenoid(8);
        isUp = false;
    }
    
    public void doDisabled(WiredCatsEvent event) {

    }

    public void doAutonomousSpecific(WiredCatsEvent event) {
    }

    public void doTeleopSpecific(WiredCatsEvent event) 
    {
        if (event instanceof EventGamePad)  handleGamePadEvents(((EventGamePad)event));
    }

    public void doEnabled(WiredCatsEvent event) {
        
    }
    
    private void handleGamePadEvents(EventGamePad event) {
        if (event instanceof EventButtonXPressed && event.isController1()) {
            if (isUp) {
               hangOn.set(false);
               hangOff.set(true); 
               isUp = false;
            } else {
               hangOn.set(true);
               hangOff.set(false);
               isUp = true;
            }   
        }
    }
    
}
