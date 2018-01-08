package javaAP.xml;

import javaAP.lang.Obj;
import java.util.HashMap;

public class Tag extends Obj
{
    public String name;
    public HashMap<String, String> attrs;
    
    public Tag(String name_in, HashMap<String, String> attrs_in)
    {
        name = name_in;
        attrs = attrs_in;
    }    
}
