/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import Util2415.AutonomousReader;
import Util2415.Node;
import WiredCatsEvents.AutonomousCommands.CommandIntakeOn;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import WiredCatsEvents.AutonomousCommands.CommandShoot;
import WiredCatsEvents.AutonomousCommands.CommandIntakeOff;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsSystems.SystemArm;
import WiredCatsSystems.SystemDrive;
import WiredCatsSystems.SystemIntake;
import WiredCatsSystems.SystemShooter;
import WiredCatsSystems.WiredCatsSystem;
import edu.wpi.first.wpilibj.Timer;
import java.util.Vector;

/**
 *
 * @author Bruce Crane
 */
public class ControllerAutonomous extends WiredCatsController {
    
    AutonomousReader autoReader;
    Vector nodes;
    
    Node desiredState;
    boolean atDesiredNode;
    
    double frisbeesShot;
    
    boolean enabled = false;
    
    SystemDrive sd;
    SystemShooter ss;
    SystemIntake si;
    SystemArm sa;
    
    boolean driveReady;
    boolean armReady;
    boolean shooterReady;
    double delay;
    
    Timer timer;
    
    public ControllerAutonomous(int limit)
    {
        super(limit);
        
        autoReader = new AutonomousReader();
        
        atDesiredNode = true;         
        driveReady = false;
        armReady = false;
        shooterReady = false;
        delay = 0;
        
        timer = new Timer();
        timer.start();
        
    }
    
    public void readLog(String s){
        nodes = autoReader.readLog(s);
    }
    
    public void setNodes(Vector nodes)
    {
        this.nodes = nodes;
        atDesiredNode = true;
    }
    
    public void begin(){
        enabled = true;
    }
    
    public void stop(){
        enabled = false;
    }

    public void run() {
        while (true) {
            if (enabled) {
                
                if (isEmpty()) break;
                
                if (atDesiredNode && timer.get() > delay){
                    System.out.println("setting new node.");
                    desiredState = take();
                    atDesiredNode = false;
                    driveReady = false;
                    armReady = false;
                    shooterReady = false;
                    delay = desiredState.delay;
                    setNewDesiredNode();
                    timer.stop();
                    timer.reset();
                    timer.start();
                }
                
                if (sd.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    driveReady = true;
                }
                if (sa.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    armReady = true;
                }
                if (ss.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    shooterReady = true;
                }
                
                if (driveReady && armReady && shooterReady){
                    atDesiredNode = true;
                    System.out.println(desiredState.toString());
                }
                
            }
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }    
    }
    
    private boolean isEmpty(){
        return nodes.isEmpty();
    }
    
    private Node take(){
        Node temp = (Node)nodes.firstElement();
        nodes.removeElementAt(0);
        return temp;
    }
    
    public void addSystems(SystemDrive sd, SystemIntake si, SystemShooter ss, SystemArm sa){
        this.sd = sd;
        this.si = si;
        this.ss = ss;
        this.sa = sa;
    }
    
    private void setNewDesiredNode()
    {

        sendCommand(new CommandNewDesiredPosition(this, desiredState.leftTicks, desiredState.rightTicks, 0), sd);
        sendCommand(new CommandNewArmAngle(this, desiredState.armAngle), sa);
        
        if (frisbeesShot < desiredState.frisbeesShot)
        {
            sendCommand(new CommandShoot(this), ss);
            frisbeesShot++;
        } else shooterReady = true;
        
        if (desiredState.isIntakeOn) sendCommand(new CommandIntakeOn(this), si);
        else sendCommand( new CommandIntakeOff(this), si);
    }
    
    
    private void sendCommand(WiredCatsEvent command, WiredCatsSystem wcs){
        wcs.eventReceived(command);
    }
    
}
    