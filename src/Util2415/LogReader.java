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
import javax.microedition.io.InputConnection;
import java.io.InputStream;

/**
 *
 * @author Bruce Crane
 */
public class LogReader 
{
    
    private String deliminator = " ";
    private InputStream theFile;
    private InputConnection connection;
    
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
             connection = (InputConnection) Connector.open("file:///" + fileName, Connector.READ);
            theFile = connection.openDataInputStream();
            
            while (hasMore)
            {
                String first = readToken();
                if (first == null)
                {
                    hasMore = false;
                    break;
                }
                
                nodes.addElement(new Node(
                        Double.parseDouble(first),
                        Double.parseDouble(readToken()),
                        Integer.parseInt(readToken()),
                        Double.parseDouble(readToken()),
                        Double.parseDouble(readToken()),
                        Double.parseDouble(readToken())));
            }
            
            theFile.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        
        return nodes;
    }
    
    private String readToken() throws IOException
    {
        String s = "";
        int c = theFile.read();
        while (c != -1 && c != (int)'\n' && c != (int)' ')
        {
            s += (char)c;
            c = theFile.read();
            if (c == -1) hasMore = false;
        }
        return s;
    }
    
}
