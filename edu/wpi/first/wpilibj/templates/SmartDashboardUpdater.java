/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Systems.RobotSystem;
import Util2415.CSVReader;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Vector;

/**
 *
 * @author BruceCrane
 */
public class SmartDashboardUpdater implements Runnable
{
    
    /**
     * I was kind of inspired by the whole event based 
     * system on this one, more specifically the 
     * subscription system. I decided that all systems
     * would subscribe to the smartDashboard, such that
     * they each detailed what values they wanted updated.
     * Every five seconds, the smartdashboard updater
     * checks and updates these values. The values are 
     * stored in the CSVReader, in that hashtable we've got.
     * The systems can access that hashtable whenever they
     * please.
     */
    private static final int SLEEP_TIME = 3000;
    
    private Vector subscribedSystems = new Vector();
    
    public static CSVReader csvreader = new CSVReader();
    
    public SmartDashboardUpdater(String fileName)
    {
        csvreader.getFromFile(fileName);
    }
    
    public void run() 
    {
        //initial values that are set.
        
        for (int i = 0; i < subscribedSystems.size(); i++)
        {
            doesContainValues( ((RobotSystem)subscribedSystems.elementAt(i)).getUpdateValues() );
            putValuesToSmartDashboard(((RobotSystem)subscribedSystems.elementAt(i)).getUpdateValues());
        }
        //System.out.println(">>>>>>>>>>>>>>>> I GOT TO THIS POINT <<<<<<<<<<<<<<<<<<<<<<<<"); //debugging.
        
        while (true)
        {
            
            for (int i = 0; i < subscribedSystems.size(); i++)
            {
                //check all values each system wants.
                checkValues( ((RobotSystem)subscribedSystems.elementAt(i)).getUpdateValues() );
                
            }
            //SmartDashboard.putNumber("TESTING_VALUE", 2000);
            
            /*try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ex) {
               */
        }
        
    }
    
    public void addSubscribedSystem(RobotSystem rs)
    {
        subscribedSystems.addElement(rs);
    }
    
    private void checkValues(String[] values)
    {
        //checks each value, updates based off of values.
        
        for (int i = 0; i < values.length; i++)
        {
            csvreader.setValue(values[i], SmartDashboard.getNumber(values[i]));
        }
        
    }
    
    /**
     * Checks if the hashmap contains these values.
     * It always should, and if it doesn't then there
     * is a problem with your code.
     * @param values
     * @return 
     */
    private boolean doesContainValues(String[] values)
    {
        for (int i = 0; i < values.length; i++)
        {
            if (!csvreader.contains(values[i]))
            {
                //System.out.println("Values " + values[i] + " was not found in the text file.");
                return false;
            }

        }
        
        return true;
    }
    
    private void putValuesToSmartDashboard(String[] value)
    {
        for (int i = 0; i < value.length; i++)
        SmartDashboard.putNumber(value[i], csvreader.getValue(value[i]));
    }
    
}
