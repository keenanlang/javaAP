package javaAP.yawt;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayDeque;

import javaAP.lang.Sys;
import javaAP.lang.Obj;
import javaAP.xml.XMLParser;

public class GUIParser extends XMLParser
{
    private ArrayDeque<Container> hierarchy;
    private Component current;
    
    public void parse(String filename)  { super.parse(filename); }
    public void parse(File xmlfile)     { super.parse(xmlfile); }
    
    public Display getDisplay() { return (Display) current; }
    
    public void this_start()
    {
        hierarchy = new ArrayDeque<Container>();
    }

    public void Display_start(HashMap attrs)
    {
        Display output = new Display(0,0);
        current = output;
    }
    
    public void Display_end()
    {
        current = hierarchy.removeLast();
    }
    
    public void Container_start(HashMap attrs)
    {
        String classname = (String) attrs.get("type");
        Class tocreate = Sys.from_name(classname);
        
        hierarchy.addLast((Container) current);
        current = (Component) Obj.create(tocreate);
    }
    
    public void Component_start(HashMap attrs)
    {
        String classname = (String) attrs.get("type");
        Class tocreate = Sys.from_name(classname);
        
        hierarchy.addLast((Container) current);
        current = (Component) Obj.create(tocreate);
    }
    
    public void Component_end()
    {
        Container c = hierarchy.removeLast();
        c.add(current);
        current = c;
    }
    
    public void Container_end()
    {
        Container c = hierarchy.removeLast();
        c.add(current);
        current = c;
    }
    
    public void position_content(String data)
    {
        String[] pos = data.split(",");
        int x = new Integer(pos[0].trim());
        int y = new Integer(pos[1].trim());
        current.position(x,y);
    }
    
    public void size_content(String data)
    {
        String[] siz = data.split(",");
        int width = new Integer(siz[0].trim());
        int height = new Integer(siz[1].trim());
        current.size(width, height);
    }
    
    public void name_content(String data)
    {
        current.name(data.trim());
    }
}