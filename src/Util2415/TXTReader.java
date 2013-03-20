package Util2415;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Code to read from .txt file and reupdate constants
 *
 * @author SamCrane
 */
public class TXTReader {

    private Hashtable resources = new Hashtable();

    public double getValue(String s) {
        TXTReader.OurDouble d = (TXTReader.OurDouble) resources.get(s);
        return d.d;
    }

    public void setValue(String s, double d) {
        resources.put(s, new OurDouble(d));
    }

    public void getFromFile(String s) {
        try {
            Scanner sc = new Scanner(s);
            String tempString;
            TXTReader.OurDouble tempDouble;
            while (sc.hasNext()) {
                tempString = sc.next();
                tempDouble = new TXTReader.OurDouble(Double.parseDouble(sc.next()));
                resources.put(tempString, tempDouble);
            }
            sc.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    public boolean contains(String s) {
        return resources.contains(s);
    }
    
    public void pushToSmartDashboard()
    {
        Enumeration keys = resources.keys();
        String key;
        while (keys.hasMoreElements())
        {
            key = (String)keys.nextElement();
            SmartDashboard.putNumber(key, getValue(key));
        }
    }
    
    public Enumeration getKeys()
    {
        return resources.keys();
    }
    
    /*
     * Nested class of "OurDouble" in order to be able to get the Hashmap working
     * For some reason, it does not play nice with the real "Double" class
     */
    private class OurDouble {
        public double d;

        public OurDouble(double d1) {
            this.d = d1;
        }
    }
    
}
