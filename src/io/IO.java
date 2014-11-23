package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Class IO has static methods to read and write to functions.
 *
 * However, class WriterRunnable actually does the writing.
 *
 */
public abstract class IO {
    public static String readFile(String filename) {
        String returnValue = "";
        FileReader file = null;

        try {
            file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            String line = "";
            while ((line = reader.readLine()) != null) {
                returnValue += line + "\n";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    //ignore issues
                }
            }
        }
        return returnValue;
    } //readFile 

    public static void writeFile(String filename, String s) {
    	try {
            File file = new File(filename);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(s);
            output.close();
          } catch ( IOException e ) {
             e.printStackTrace();
          }
    } //writeFile
}
