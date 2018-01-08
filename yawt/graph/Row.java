package javaAP.yawt.graph;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

public class Row implements Iterable
{
    private List data;

    public Row()                { data = new ArrayList(); }
    public Row(Object... input) { data = Arrays.asList(input); }
    
    public Iterator iterator()  { return data.iterator(); }
    
    public Object get(int index)        { return data.get(index); }
    public void set(int i, Object in)   { data.set(i, in); }
    public void add(Object in)          { data.add(in); }
}