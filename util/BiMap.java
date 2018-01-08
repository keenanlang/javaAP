package javaAP.util;

import java.util.HashMap;
import javaAP.lang.Obj;


/**
 * This class represents a Bidirectional HashMap.
 * Keys and Values are instead related pairs of
 * values where each value is unique to its pair.
 */
public class BiMap extends Obj
{
    private HashMap implement;
    
    /**
     * Basic Constructor
     */
    public BiMap()    { implement = new HashMap(); }
    
    /**
     * Adds a pair of values to the BiMap.
     * 
     * @param first First Value
     * @param pair  Second Value
     */
    public final void put(Object first, Object pair)
    {
        this.remove(first);
        this.remove(pair);
        
        implement.put(first, pair);
        implement.put(pair, first);
    }
    
    /**
     * Retrieves the paired value for the
     * given input.
     * 
     * @param value Search value
     * 
     * @return The paired value or null if
     *         not found.
     */
    public final Object fetch(Object value)
    {
        return implement.get(value);
    }

    /**
     * Removes the given value and its pair
     * from the BiMap.
     * 
     * @param value One value of the pair you
     *              wish to remove.
     *              
     * @return The other value of the pair
     */
    public final Object remove(Object value)
    {
        Object temp = implement.remove(value);
        
        implement.remove(temp);
        
        return temp;
    }
    
    /**
     * Removes all pairs from the BiMap.
     */
    public final void clear()                   { implement.clear(); }
    
    /**
     * Checks if any of the pairs contained by
     * this BiMap are equal to the given value.
     * 
     * @param value Search value
     * 
     * @return  true, if the search value was found
     *          false, if it wasn't
     */
    public final boolean contains(Object value) { return implement.containsValue(value); }
    
    public final boolean equals(Object o)       { return implement.equals(o); }
    public final int hashCode()                 { return implement.hashCode(); }
    public final boolean empty()                { return implement.isEmpty(); }
    
    /**
     * Returns the number of data pairs contained
     * by this BiMap.
     * 
     * @return  The number of data pairs contained
     *          by this BiMap.
     */
    public final int size()                     { return (int) implement.size() / 2; }
}