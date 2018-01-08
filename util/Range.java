package javaAP.util;

import javaAP.lang.Obj;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Range extends Obj implements Iterable<Integer>
{
    /**
     * Start index
     */
    public final int first;
    
    /**
     * End index
     */
    public final int last;
    
    /**
     * Change between consecutive numbers
     */
    public final int increment;
    
    /**
     * Number of different values that this 
     * iterator iterates over
     */
    public final int length;
    
    
    private Range(int start, int end, int dx)
    {
        first = start;
        last = end;
        
        if(dx == 0)         { increment = 1; }
        else                { increment = dx; }
        
        if(start == end)    { length = 1; }
        else                { length = (int) ((Math.abs(first - last) + 1) / dx); }
    }
    
    
    /**
     * Creates a Range from 1 to a given number
     * with an increment of 1.
     * 
     * @param end   Final number.
     */
    public static Range __int__(int end) { return Range.instance(1, end); }
    public static Range instance(int end) { return Range.from(Range.class, end); }
    
    /**
     * Creates a Range between two numbers inclusive
     * with an increment of 1.
     * 
     * @param start Start number
     * @param end   Final number
     */
    public static Range instance(int start, int end) { return Range.instance(start, end, Integer.signum(end - start)); }
    
    /**
     * Creates a Range over the indices of an array.
     * Starts at 0 and goes to the last index with an
     * increment of 1.
     * 
     * @param array The array to index.
     */
    public static Range __bytearray__(byte[] array)    { return Range.instance(0, array.length - 1); }
    public static Range __shortarray__(short[] array)  { return Range.instance(0, array.length - 1); }
    public static Range __chararray__(char[] array)    { return Range.instance(0, array.length - 1); }
    public static Range __intarray__(int[] array)      { return Range.instance(0, array.length - 1); }
    public static Range __longarray__(long[] array)    { return Range.instance(0, array.length - 1); }
    public static Range __floatarray__(float[] array)  { return Range.instance(0, array.length - 1); }
    public static Range __doublearray__(double[] array){ return Range.instance(0, array.length - 1); }
    public static Range __Objectarray__(Object[] array){ return Range.instance(0, array.length - 1); }
    
    public static Range __String__(String line)        { return Range.instance(0, line.length() - 1); }
    
    /**
     * Creates a Range from a given start to a given
     * end with a given increment.
     * 
     * @param start Start number
     * @param end   Final number
     * @param dx    Increment
     */
    public static Range instance(int start, int end, int dx) { return new Range(start, end, dx); }
    
    /**
     * Returns the Iterator represented by this Range.
     * 
     * @return  the Iterator that iterates over the numbers
     *          in this Range.
     */
    public Iterator<Integer> iterator()
    {
        return new Iterator<Integer>()
        {
            private int counter = first - increment;
        
            public boolean hasNext()
            {
                return ! (counter >= last);
            }
        
            public Integer next()
            {
                if (counter == last) { throw new NoSuchElementException(); }
                counter += increment;
                    
                return counter;
            }
        
            public void remove() {}
        };
    }
}