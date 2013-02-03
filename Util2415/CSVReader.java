package Util2415;

import java.util.Hashtable;
import java.lang.Double;

/**
 * Code to read from CSV file and reupdate constants
 *
 * @author SamCrane
 */
public class CSVReader {

    // They use a sort of pidgin Hashtable which works slightly differently
    // http://docs.oracle.com/javame/config/cldc/ref-impl/cldc1.1/jsr139/index.html
    
    private Hashtable resources = new Hashtable();

    public double getValue(String s) {
        CSVReader.OurDouble d = (CSVReader.OurDouble)resources.get(s); 
        return d.d;
    }
    
    public void setValue(String s, double d)
    {
        resources.put(s, new OurDouble(d));
    }

    public void getFromFile(String s) {
        try {
            Scanner sc = new Scanner(s);
            String tempString;
            CSVReader.OurDouble tempDouble;
            while (sc.hasNext()) {
                tempString = sc.next();
                tempDouble = new CSVReader.OurDouble( Double.parseDouble(sc.next()) );
                resources.put(tempString, tempDouble);
            }
            sc.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }
    
    public boolean contains(String s)
    {
        return resources.contains(s);
    }
    
    private class OurDouble
    {
        public double d;
        
        public OurDouble(double d1)
        {
            this.d = d1;
        }
    }
}
