/* 
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.rt.core.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class StringParser {
    
    private String line;         //basically used to such any specific char sequences from a specific readline if requested
    private BufferedReader bf;
    private String lineTokens[];
    private int index;
    
    public StringParser(BufferedReader bf)
    {
        this.line = "";
        this.bf = bf;
        this.lineTokens = new String[0];
        this.index = 0;
    }
    
    public StringParser(String directory, String file)
    {
        Path path = FileSystems.getDefault().getPath(directory, file);
        try 
        {
            bf = Files.newBufferedReader(path, Charset.defaultCharset());
        } 
        catch (IOException ex)
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        line = "";
        lineTokens = new String[0];
        index = 0;
    }
    
    public StringParser(URI uri)
    {
        File file = new File(uri);
        Path path = file.toPath();
        
        try 
        {
            bf = Files.newBufferedReader(path, Charset.defaultCharset());
        } 
        catch (IOException ex)
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        line = "";
        lineTokens = new String[0];
        index = 0;
    }
    
    public void close()
    {
        try 
        {
            bf.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void skipTokens(int value)
    {
        String string = null;
        
        for(int i = 0; i<value; i++)
        {
            string = getNextToken();
            if(string == null)
                return;
        }
    }
    
    public String getNextToken()
    {
        while(true)
        {
            String tok = fetchNextToken();
            if (tok == null)
                return null;
            else
                return tok;
        }
    }
    
    public boolean getNextBoolean()
    {
        return Boolean.valueOf(getNextToken());
    }

    public int getNextInt() 
    {
        return Integer.parseInt(getNextToken());
    }

    public float getNextFloat() 
    {
        return Float.parseFloat(getNextToken());
    }
            
    public boolean hasNext()
    {
        return peekNextToken() != null;
    }
    
    public boolean peekNextToken(String string)
    {
        String nextToken = peekNextToken();
        if(nextToken != null && string != null)
            if(nextToken.equals(string))
                return true;
        return false;
    }
    
    public String peekNextToken()
    {
        while(true)
        {
            String t = fetchNextToken();
            if (t == null)
                return null; // nothing left
            else
            {
                index--;
                return t;
            }
        }
    }
    
    public boolean peekNextTokenIsNumber()
    {
        String str = peekNextToken();
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
   
    
    private String fetchNextToken()
    {
        if (bf == null)
            return null;
        while (true) 
        {            
            if (index < lineTokens.length)
                return lineTokens[index++];
            else if (!getNextLine())
                return null;
        }
    }
    
    private boolean getNextLine()
    {       
        String readLine = null;
        while(true)
        {
            try 
            {
                readLine = bf.readLine();                
            } 
            catch (IOException ex)
            {
                Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (readLine == null)
                return false;
            
            if(readLine.isEmpty())
                continue;
                       
            lineTokens = readLine.trim().split("[/\\s]+");            
            line = readLine;
            
            index = 0;
            return true;
        }
    }
    
    public boolean contains(CharSequence sequence)
    {
        if(line == null) return false;        
        return line.contains(sequence);
    }
    
    public String getLine()
    {
        return line;
    }
    
    public static void writeString(File file, String string)
    {        
        try
        {
            if(!file.exists())
                file.createNewFile();
            else           
                Files.write(file.toPath(), string.getBytes());            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public static StringParser getStringParser(File file)
    {
        try
        {
            if(!file.exists())
                file.createNewFile();
            else   
            {
                StringParser parser = new StringParser(Files.newBufferedReader(file.toPath(), Charset.defaultCharset()));
                return parser;
            }
        }
        catch (IOException ex) 
        {
            Logger.getLogger(StringParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
