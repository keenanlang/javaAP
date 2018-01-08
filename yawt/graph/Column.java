package javaAP.yawt.graph;

import java.util.Iterator;
import java.util.ArrayList;

public class Column
{
    public final String title;
    public final Class type;
    
    public Column(String name)
    {
        title = name;
        type = Object.class;
    }
    
    public Column(String name, Class clazz)
    {
        title = name;
        type = clazz;
    }   
}