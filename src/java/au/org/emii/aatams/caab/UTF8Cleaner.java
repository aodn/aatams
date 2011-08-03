package au.org.emii.aatams.caab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Removes non-UTF-8 bytes sequences from a file (this is part of the process
 * of importing CAAB data in to PostGRES).
 * 
 * Also adds a unique ID at the beginning of each line.
 * 
 * @author jburgess
 */
public class UTF8Cleaner
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        long id = 10000000 - 1; // So that the id matches the spcode.
        
        File inFile = new File(args[0]);
        FileInputStream inStream = new FileInputStream(inFile);
        
        File outFile = new File(args[1]);
        FileOutputStream outStream = new FileOutputStream(outFile);
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        
        for (String line; (line = reader.readLine()) != null;) 
        {
//            line = line.replaceAll("[^\\x20-\\x7e]", "");
            line = line.replaceAll("[^\\x00-\\xff]", "");
            
            writer.write(String.valueOf(id));
            writer.write("\t");        
            writer.write(line);
            writer.write('\n');
            
            id++;
        }
        
        writer.close();
        reader.close();
    }
}
