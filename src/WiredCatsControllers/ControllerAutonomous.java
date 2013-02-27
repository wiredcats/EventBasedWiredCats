/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WiredCatsControllers;

import Util2415.LogReader;
import Util2415.Node;
import WiredCatsEvents.AutonomousCommands.CommandIntake;
import WiredCatsEvents.AutonomousCommands.CommandNewArmAngle;
import WiredCatsEvents.AutonomousCommands.CommandNewDesiredPosition;
import WiredCatsEvents.AutonomousCommands.CommandShoot;
import WiredCatsEvents.AutonomousCommands.CommandStopIntake;
import WiredCatsEvents.WiredCatsEvent;
import WiredCatsSystems.SystemArm;
import WiredCatsSystems.SystemDrive;
import WiredCatsSystems.SystemIntake;
import WiredCatsSystems.SystemShooter;
import WiredCatsSystems.WiredCatsSystem;
import java.util.Vector;

/**
 *
 * @author Bruce Crane
 */
public class ControllerAutonomous extends WiredCatsController {
    
    LogReader lr;
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
    boolean shooterReady;
    boolean armReady;
    public ControllerAutonomous(int limit){
        super(limit);
        atDesiredNode = true;
                
        driveReady = false;
        shooterReady = false;
        armReady = false;
    }
    
    public void readLog(String s){
        nodes = lr.readLog(s);
    }
    
    public void begin(){
        enabled = true;
    }
    
    public void stop(){
        enabled = false;
    }
    
    public void reset(String s){
        nodes = lr.readLog(s);
    }

    public void run() {
        while (true) {
            if (enabled) {
                
                if (isEmpty()) break;
                
                if (atDesiredNode){
                    desiredState = take();
                    atDesiredNode = false;
                    setNewDesiredNode();
                    driveReady = false;
                    shooterReady = false;
                    armReady = false;
                }
                
                if (sd.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    driveReady = true;
                }
                if (ss.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    shooterReady = true;
                }
                if (sa.autonomous_AtDesiredNode() == WiredCatsSystem.AUTONOMOUS_COMPLETED){
                    armReady = true;
                }      
                if (driveReady && shooterReady && armReady){
                    atDesiredNode = true;
                }
                
            }
            try {
                Thread.sleep(10);
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
    
    private void setNewDesiredNode(){
        
        if (desiredState.isIntakeOn) {
            sendCommand(new CommandIntake(this), si);
        }
        else {
            sendCommand(new CommandStopIntake(this), si);
        }
        if (desiredState.frisbeesShot < frisbeesShot) {
            sendCommand(new CommandShoot(this), ss);
        }
        frisbeesShot = desiredState.frisbeesShot;
        
        sendCommand(new CommandNewDesiredPosition(this, desiredState.leftTicks, desiredState.rightTicks, 0), sd);
        sendCommand(new CommandNewArmAngle(this, desiredState.armAngle), sa);
        
    }
    
    
    private void sendCommand(WiredCatsEvent command, WiredCatsSystem wcs){
        wcs.eventReceived(command);
    }
    
}
    