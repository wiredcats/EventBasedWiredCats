/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util2415;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;

/**
 *
 * @author Bruce Crane
 */
public class LogReader 
{
    
    private String deliminator = " ";
    private DataInputStream theFile;
    FileConnection fc;
    
    boolean hasMore;
    
    public LogReader()
    {
        hasMore = true;
    }
    
    public Vector readLog(String fileName)
    { 
        Vector nodes = new Vector();
        
        try
        {
            fc = (FileConnection)Connector.open("file:///WiredCatsLogs/" + fileName + ".txt", Connector.READ);
            fc.create();
            theFile = fc.openDataInputStream();
            
            while (hasMore)
            {
                nodes.addElement(new Node(readToken(), readToken(), readToken(), readToken(), readToken()));
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        return nodes;
    }
    
    private double readToken() throws IOException
    {
        String s = "";
        int c = theFile.read();
        while (c != -1 || c != (int)'\n' || c != (int)' ')
        {
            s += c;
            c = theFile.read();
            if (c == -1) hasMore = true;
        }
        
        return Double.parseDouble(s);
    }
    
}
