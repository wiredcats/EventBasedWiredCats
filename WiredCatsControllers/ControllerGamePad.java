/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import WiredCatsEvents.EventButtonAPressed;
import WiredCatsEvents.EventButtonAReleased;
import WiredCatsEvents.EventDisabled;
import WiredCatsEvents.EventEnabled;
import WiredCatsEvents.EventGamePad;
import WiredCatsEvents.EventLeftYAxis;
import WiredCatsEvents.EventRightBumperPressed;
import WiredCatsEvents.EventRightBumperReleased;
import WiredCatsEvents.EventRightYAxis;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.WiredCats2415;

/**
 *
 * @author Robotics
 */
public class ControllerGamePad extends WiredCatsController implements Runnable
{
    
    
    //GamePad is just a collection of variables,
    //more of a C++ struct than a class in use.
    GamePad oldPrimaryGP;
    GamePad oldSecondaryGP;
    GamePad newPrimaryGP;
    GamePad newSecondaryGP;
    
    Joystick primaryController;
    Joystick secondaryController;
    
    private static final short SLEEP_TIME = 100; //in milliseconds

    public ControllerGamePad(int limit, WiredCats2415 robot)
    {
        super(limit, robot);
        
        System.out.println("GamePad Controller initializing.");
             
        primaryController = new Joystick(1);
        secondaryController = new Joystick(2);
        
        //primary game pad
        oldPrimaryGP = new GamePad(); //these hold the old values of 
        oldSecondaryGP = new GamePad(); //each game pad, 1 and 2 respectively.
        //secondary game pad.
        
        
        //these hold the new values,
        //to compare to the old.
        newPrimaryGP = new GamePad();
        newSecondaryGP = new GamePad();
        
    }
    /*
    public static final byte AUTONOMOUS = 1;
    public static final byte TELEOP = 2;
    public static final byte ENABLED = 4;
    public static final byte DISABLED = 5;
    */
    public boolean oldEnabled = false;
    
    public void run() 
    {
        while (true)
        {
                if (robot.isEnabled() != oldEnabled)
                {
                    //then the state changed.
                    oldEnabled = robot.isEnabled();
                    if (oldEnabled) 
                    {
                        //state is enabled.
                        fireEvent(new EventEnabled(this));
                    }
                    else
                    {
                        //state is not enabled
                        fireEvent(new EventDisabled(this));
                    }
                }
            
                checkController(primaryController,oldPrimaryGP, newPrimaryGP, (byte)1);
                checkController(secondaryController, oldSecondaryGP, newSecondaryGP, (byte)2);
            
           /* try
            {
               // Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }*/
            
        }
        
    }
        
        private void checkController(Joystick js, GamePad oldGP, GamePad newGP, byte whichController)
        {
            newGP.button_A = js.getRawButton(1);
            newGP.button_B = js.getRawButton(2);
            newGP.button_X = js.getRawButton(3);
            newGP.button_Y = js.getRawButton(4);
            newGP.right_Bumper = js.getRawButton(6);
            
            //for these we need to dead band, 
            //I think that's what it's called...
            //so I just have a private function that takes care of that.
            newGP.leftThumbstickY = getAxisValue(js.getRawAxis(2));
            newGP.rightThumbstickY = getAxisValue(js.getRawAxis(5));
            
            
            
            /*if (oldGP.button_A != newGP.button_A)
            {
                fireEvent(new EventButtonAPressed(whichController, EventButtonPressed.A_PRESSED, this));
                oldGP.button_A = newGP.button_A;
            }
            if (oldGP.button_B != newGP.button_B)
            {
                fireEvent(new EventButtonPressed(whichController, EventButtonPressed.B_PRESSED, this));
                oldGP.button_B = newGP.button_X;
            }
            if (oldGP.button_X != newGP.button_X)
            {
                fireEvent(new EventButtonPressed(whichController, EventButtonPressed.X_PRESSED, this));
                oldGP.button_X = newGP.button_X;
            }
            if (oldGP.button_Y != newGP.button_Y)
            {
                fireEvent(new EventButtonPressed(whichController, EventButtonPressed.Y_PRESSED, this));
                oldGP.button_Y = newGP.button_Y;
            }*/
            
            //start implementing deadzone soon,
            //not sure how I'm going to do that.
            
            if (oldGP.leftThumbstickY != newGP.leftThumbstickY)
            {
                //send a thumbstick event.
//                System.out.println("Firing left Thumbstick Event");
                fireEvent(new EventLeftYAxis(this, whichController, newGP.leftThumbstickY));
                oldGP.leftThumbstickY = newGP.leftThumbstickY;
            }
            if (oldGP.rightThumbstickY != newGP.rightThumbstickY)
            {
//                System.out.println("Firing right Thumbstick Event");
                fireEvent(new EventRightYAxis(this, whichController, newGP.rightThumbstickY));
                oldGP.rightThumbstickY = newGP.rightThumbstickY;
            }
            if (newGP.right_Bumper != oldGP.right_Bumper)
            {
                if (newGP.right_Bumper)
                {
                    fireEvent(new EventRightBumperPressed(this, whichController));
                }
                else
                {
                    fireEvent(new EventRightBumperReleased(this, whichController));
                }
                oldGP.right_Bumper = newGP.right_Bumper;
                
            }
            if (newGP.button_A != oldGP.button_A)
            {
                if (newGP.button_A)
                {
                    fireEvent(new EventButtonAPressed(this, whichController));
                }
                else
                {
                    fireEvent(new EventButtonAReleased(this, whichController));
                }
                oldGP.button_A = newGP.button_A;
            }
        }
        
        /**
         * Grabs the value of the joystick,
         * but returns a percentage because
         * integers are easier to work with.
         * @param i
         * @return 
         */
        private double getAxisValue(double outputValue)
        {
            double CONTROLLER_DEADBAND = .08;//(int)(SmartDashboardUpdater.csvreader.getValue("CONTROLLER_DEADBAND"));
            
            //System.out.println(newValue);
            
            if (Math.abs(outputValue) < CONTROLLER_DEADBAND) //in percentages.
            {
                return 0; //can be treated as zero.
            }          

            return (outputValue - CONTROLLER_DEADBAND)/(1 - CONTROLLER_DEADBAND);
        }
   /**
    * This is an object that holds onto all the information of the controller,
    * allowing for us to check changes in thumbsticks/buttons. 
    */ 
   private class GamePad
   {
        public boolean button_A;
        public boolean button_B;
        public boolean button_X;
        public boolean button_Y;
        public boolean right_Bumper;
        public boolean left_Bumper;
        public double leftThumbstickY;
        public double rightThumbstickY;
        
        public GamePad()
        {
            //initially sets things to false.
            button_A = false;
            button_B = false;
            button_X = false;
            button_Y = false;
            right_Bumper = false;
            left_Bumper = false;
            leftThumbstickY = 0.0;
            rightThumbstickY = 0.0;
        }
        
        //deprecated
        /*public void copyValues(GamePad g)
        {
            button_A = g.button_A;
            button_B = g.button_B;
            button_X = g.button_X;
            button_Y = g.button_Y;
            right_Bumper = g.right_Bumper;
        }*/
        
   }
}