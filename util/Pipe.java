package javaAP.util;

import javaAP.lang.Sys;
import javaAP.lang.Obj;

import java.io.*;
import java.util.ArrayDeque;

/**
 * A Pipe represents a buffered communication
 * between an Input interface and an Output
 * interface. Its primary use is to communicate
 * between two threads.
 */
public class Pipe extends Obj
{
    /**
     * The Interface to retrieve data from this Pipe
     */
    public final Interface.Input input;
    
    /**
     * The Interface to put data to this Pipe
     */
    public final Interface.Output output;
    
    private final ArrayDeque<char[]> in_buffer;
    private final ArrayDeque<char[]> out_buffer;
    
    private final int buffersize;
    
    private int nextRead, numRead, read_end;
    private int nextWrite, numWrite, write_start;
    
    /**
     * Creates a new Pipe and initializes the input
     * and output Interfaces.
     */
    public Pipe()
    {
        buffersize = 8092;
        
        in_buffer = new ArrayDeque<char[]>();
        out_buffer = new ArrayDeque<char[]>();
        
        in_buffer.add(new char[buffersize]);
        out_buffer.add(new char[buffersize]);
        
        input = Interface.Input.instance(this.createinput());
        output = Interface.Output.instance(this.createoutput());
    }
    
    private InputStream createinput()
    {
        return new InputStream()
        {
            public int available()
            {
                return numRead;
            }
            
            public int read()
            {
                if(numRead == 0)    { return -1; }
                
                char[] buff = in_buffer.peekFirst();
                
                int out = buff[nextRead];
                
                nextRead += 1;
                numRead -= 1;
                
                if(nextRead == buffersize)
                {
                    nextRead -= buffersize;
                    in_buffer.removeFirst();
                }
                
                return out;
            }
            
            public void close()
            {
            }
        };
    }
    
    private OutputStream createoutput()
    {
        return new OutputStream()
        {            
            public void write(int b)
            {
                char[] buff = out_buffer.peekLast();
                
                buff[nextWrite] = (char) b;
                
                nextWrite += 1;
                numWrite += 1;
                
                if(nextWrite == buffersize)
                {
                    nextWrite -= buffersize;
                    out_buffer.addLast(new char[buffersize]);
                }
            }
            
            public void flush()
            {
                char[] in_buff = in_buffer.peekLast();
                char[] out_buff = out_buffer.peekFirst();
        
                while(numWrite > 0)
                {
                    in_buff[read_end] = out_buff[write_start];
            
                    read_end += 1;
                    write_start += 1;
                    numWrite -= 1;
                    numRead += 1;
            
                    if(read_end == buffersize)
                    {
                        read_end -= buffersize;
                        in_buffer.addLast(new char[buffersize]);
                        in_buff = in_buffer.peekLast();
                    }
            
                    if(write_start == buffersize)
                    {
                        write_start -= buffersize;
                        out_buffer.removeFirst();
                        out_buff = out_buffer.peekFirst();
                    }
                }
            }
            
            public void close()
            {
            }
        };
    }
}