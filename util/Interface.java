package javaAP.util;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.ArrayDeque;

import javaAP.lang.*;

public abstract class Interface
{   
    public static class Input extends Obj
    {
        private BufferedReader reader = null;
        private InputStream stream = null;
        
        public InputStream __stream__() { return stream; }
        
        public void __stream__(InputStream paramin)
        {
            this.close();
            
            try
            {
                stream = paramin;
                reader = new BufferedReader(new InputStreamReader(stream));
            }
            catch(Exception e)
            {
                stream = null;
                reader = null;
            }
        }
        
        private Input(InputStream paramin)
        {
            this.set("stream", paramin);
        }

        public static Input instance()
        {
            return new Input(System.in);
        }
        
        public static Input instance(InputStream paramin)
        {
            return new Input(paramin);
        }
        
        public static Input instance(File paramfile)
        {
            try                 { return new Input(new FileInputStream(paramfile)); }
            catch(Exception e)  { return null; }
        }
        
        public static Input instance(URL paramurl)
        {
            try                 { return new Input(paramurl.openStream()); }
            catch(Exception e)  { return null; }
        }
        
        public Hex read()
        {
            try                 { return Hex.from(Hex.class, reader.read()); }
            catch(Exception e)  { return Hex.INVALID; }
        }
        
        public Hex read(int num)
        {
            Hex output = Hex.instance();
            
            for(int index : Range.from(Range.class, num))
            {
                output.append(this.read());
            }
            
            return output;
        }
        
        public Hex readline()
        {
            try                 { return Hex.from(Hex.class, reader.readLine()); }
            catch(Exception e)  { return Hex.INVALID; }
        }
        
        public boolean finished()
        {
            try                   { return ! reader.ready(); }
            catch(Exception e)    { return true; }
        }
        
        public void close()
        {
            try  { if (stream != null) { stream.close(); } }
            catch(Exception e) {}
            
            try  { if (stream != null) { reader.close(); } }
            catch(Exception e) {}
        }
        
        public Iterable<Hex> splitlines()
        {
            return this.splitlines(Hex.class);
        }
        
        public <T> Iterable<T> splitlines(final Class<T> type)
        {
            return new Iterable<T>()
            {
                public Iterator<T> iterator()
                {
                    return new Iterator<T>()
                    {
                        public boolean hasNext()    { return ! finished(); }
                        public T next()             { return readline().as(type); }
                        public void remove()        { }
                    };
                }
            };
        }
    }
    
    public static class Output extends Obj
    {
        private boolean autoflushing = true;
        public  void  __autoflushing__(boolean input) { autoflushing = input; }
        
        private static final String linebreak = System.getProperty("line.separator");
            
        private BufferedWriter writer = null;
        private OutputStream stream = null;
        
        public OutputStream __stream__() { return stream; }
        
        public void __stream__(OutputStream paramin)
        {
            this.close();
            
            try
            {
                stream = paramin;
                writer = new BufferedWriter(new OutputStreamWriter(stream));
            }
            catch(Exception e)
            {
                stream = null;
                writer = null;
            }
        }
        
        private Output(OutputStream paramin)
        {
            this.set("stream", paramin);
        }
        
        public static Output instance()
        {
            return new Output(System.out);
        }
        
        public static Output instance(OutputStream paramin)
        {
            return new Output(paramin);
        }
        
        public static Output instance(File paramfile)
        {
            return Output.instance(paramfile, true);
        }
        
        public static Output instance(File paramfile, boolean append)
        {
            try                 { return new Output(new FileOutputStream(paramfile, append)); }
            catch(Exception e)  { return null; }
        }
        
        public void write(Object output)
        {
            if (output == null) { return; }
            
            this.write(Obj.wrap(output).as(Hex.class));
        }
        
        public void write(Hex output)
        {
            if (output == null) { return; }
            
            char[] temp = output.as(char[].class);
            
            try
            {
                writer.write(temp, 0, temp.length);
                
                if(autoflushing)    { writer.flush(); }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
        public void writeln(Object output)
        {
            this.write(output);
            this.newline();
        }
        
        public void writeln(Hex output)
        {
            this.write(output);
            this.newline();
        }
        
        public void newline()
        {
            this.write(linebreak);
        }
        
        public void flush()
        {
            try              { writer.flush(); }
            catch (Exception e) { }
        }
        
        public void close()
        {
            try  { if(stream != null) { stream.close(); } }
            catch(Exception e) {}
            
            try  { if(stream != null) { writer.close(); } }
            catch(Exception e) {}
        }
    }
}