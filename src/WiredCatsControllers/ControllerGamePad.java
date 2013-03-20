package WiredCatsControllers;

import WiredCatsEvents.GamePadEvents.EventButtonAPressed;
import WiredCatsEvents.GamePadEvents.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 * Basically a collection of variables that correspond to Joystick values. 
 * More of a C++ struct than a class in use.
 *
 * @author Robotics
 */
public class ControllerGamePad extends WiredCatsController implements Runnable {
    
    //Holds previous values of buttons / controllers in order to compare them with new ones
    GamePad oldPrimaryGP;
    GamePad oldSecondaryGP;
    
    //Holds new values of buttons / controllers in order to compare them with old
    GamePad newPrimaryGP;
    GamePad newSecondaryGP;
    
    Timer timer;

    public ControllerGamePad(int limit) {
        super(limit);
        
        oldPrimaryGP = new GamePad(new Joystick(1));
        newPrimaryGP = new GamePad(new Joystick(1));
        
        oldSecondaryGP = new GamePad(new Joystick(2));
        newSecondaryGP = new GamePad(new Joystick(2));
        
        timer = new Timer();
        timer.start();

        System.out.println("[WiredCats] GamePad Controller intialized");
    }

    public void run() {
        while (true) {
                checkController(oldPrimaryGP, newPrimaryGP, (byte) 1);
                checkController(oldSecondaryGP, newSecondaryGP, (byte) 2);
            try {                
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkController(GamePad oldGP, GamePad newGP, byte whichController) {
        newGP.updateValues();
        //was !=
        if (oldGP.leftY != newGP.leftY) fireEvent(new EventLeftYAxisMoved(this, whichController, newGP.leftY));
        if (oldGP.leftX != newGP.leftX) fireEvent(new EventLeftXAxisMoved(this, whichController, newGP.leftX));
        if (oldGP.rightY != newGP.rightY) fireEvent(new EventRightYAxisMoved(this, whichController, newGP.rightY));
        if (oldGP.rightX != newGP.rightX) fireEvent(new EventRightXAxisMoved(this, whichController, newGP.rightX));
        if (oldGP.dPadX != newGP.dPadX) fireEvent(new EventDPadXAxisMoved(this, whichController, newGP.dPadX));
        
        if (newGP.button_A != oldGP.button_A) {
            if (newGP.button_A) fireEvent(new EventButtonAPressed(this, whichController));
            else fireEvent(new EventButtonAReleased(this, whichController));
        }

        if (newGP.button_B != oldGP.button_B) {
            if (newGP.button_B) fireEvent(new EventButtonBPressed(this, whichController));
            else fireEvent(new EventButtonBReleased(this, whichController));
        }

        if (newGP.button_X != oldGP.button_X) {
            if (newGP.button_X) fireEvent(new EventButtonXPressed(this, whichController));
            else fireEvent(new EventButtonXReleased(this, whichController));
        }

        if (newGP.button_Y != oldGP.button_Y) {
            if (newGP.button_Y) fireEvent(new EventButtonYPressed(this, whichController));
            else fireEvent(new EventButtonYReleased(this, whichController));
        }
        
        if (newGP.button_Back != oldGP.button_Back) {
            if (newGP.button_Back) fireEvent(new EventButtonBackPressed(this, whichController));
            else fireEvent(new EventButtonBackReleased(this, whichController));
        }

        if (newGP.button_Start != oldGP.button_Start) {
            if (newGP.button_Start) fireEvent(new EventButtonStartPressed(this, whichController));
            else fireEvent(new EventButtonStartReleased(this, whichController));
        }
        
        if (newGP.right_Bumper != oldGP.right_Bumper) {
            if (newGP.right_Bumper) fireEvent(new EventRightBumperPressed(this, whichController));
            else fireEvent(new EventRightBumperReleased(this, whichController));
        }
        
        if (newGP.right_Trigger != oldGP.right_Trigger) {
            if (newGP.right_Trigger) fireEvent(new EventRightTriggerPressed(this, whichController));
            else fireEvent(new EventRightTriggerReleased(this, whichController));
        }

        if (newGP.right_PushDown != oldGP.right_PushDown) {
            if (newGP.right_PushDown) fireEvent(new EventRightPushDownPressed(this, whichController));
            else fireEvent(new EventRightPushDownReleased(this, whichController));
        }

        if (newGP.left_Bumper != oldGP.left_Bumper) {
            if (newGP.left_Bumper) fireEvent(new EventLeftBumperPressed(this, whichController));
            else fireEvent(new EventLeftBumperReleased(this, whichController));
        }
        
        if (newGP.left_Trigger != oldGP.left_Trigger) {
            if (newGP.left_Trigger) fireEvent(new EventLeftTriggerPressed(this, whichController));
            else fireEvent(new EventLeftTriggerReleased(this, whichController));
        }

        if (newGP.left_PushDown != oldGP.left_PushDown) {
            if (newGP.left_PushDown) fireEvent(new EventLeftPushDownPressed(this, whichController));
            else fireEvent(new EventLeftPushDownReleased(this, whichController));
        }
        
        oldGP.updateValues();
    }

    /**
     * This is an object that holds onto all the information of the controller,
     * allowing for us to check changes in thumbsticks/buttons.
     */
    private class GamePad {
        
        public Joystick js;

        public boolean button_A;
        public boolean button_B;
        public boolean button_X;
        public boolean button_Y;
        public boolean button_Back;
        public boolean button_Start;
        
        public boolean left_PushDown;
        public boolean left_Bumper;
        public boolean left_Trigger;
        
        public boolean right_PushDown;
        public boolean right_Bumper;
        public boolean right_Trigger;
        
        public double leftY;
        public double leftX;
        public double rightY;
        public double rightX;
        
        public double dPadX;
        
        public GamePad(Joystick js) {
            this.js = js;
            updateValues();
        }
        
        public final void updateValues() {
            button_A = js.getRawButton(1);
            button_B = js.getRawButton(2);
            button_X = js.getRawButton(3);
            button_Y = js.getRawButton(4);
            button_Back = js.getRawButton(7);
            button_Start = js.getRawButton(8);

            left_PushDown = js.getRawButton(9);
            left_Bumper = js.getRawButton(5);
            left_Trigger = (js.getRawAxis(3) > 0);

            right_PushDown = js.getRawButton(10);
            right_Bumper = js.getRawButton(6);
            right_Trigger = (js.getRawAxis(3) < 0);

            leftY = js.getRawAxis(2);
            leftX = js.getRawAxis(1);
            rightY = js.getRawAxis(5);
            rightX = js.getRawAxis(3);

            dPadX = js.getRawAxis(6);
        }
    }
}
