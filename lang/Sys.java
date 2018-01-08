package javaAP.lang;


import javaAP.util.Interface;

import javaAP.yawt.Component;
import javaAP.yawt.Display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.*;


/**
 * Class that combines common system tasks.
 */
public final class Sys
{
    /**
     * Replacement for System.in, uses the Interface
     * class which provides more functionality.
     */
    public static final Interface.Input in = Interface.Input.instance();
    
    /**
     * Replacement for System.out, uses the Interface
     * class which provides more functionality.
     */
    public static final Interface.Output out = Interface.Output.instance();
    
    
    /**
     * Displays a BufferedImage within a window on
     * the desktop.
     * 
     * @param out   BufferedImage to display
     * 
     * @return Display containing a simple component that displays
     *         the given BufferedImage. Update the BufferedImage and
     *         call Display.render() to update the window.
     */
    public static final Display display(final BufferedImage out)
    {
        Display window;
        
        Component image = new Component()
                          {
                              public void draw(Graphics2D pen)
                              {
                                  pen.drawImage(out, 0, 0, null);
                              }
                          };
                          
        image.size(out.getWidth(), out.getHeight());
       
        window = new Display(out.getWidth(), out.getHeight());
            window.size(image.width, image.height);
            window.add(image);
        
        window.render();
        
        return window;
    }
    
    
    /**
     * Displays a Component within a window on
     * the desktop.
     * 
     * @param out   Component to display
     * 
     * @return Display containing the given component. Update 
     *         the component and call Display.render() to 
     *         update the window.
     */
    public static final Display display(final Component out)
    {
        Display window;
        
        Component image = out;
       
        window = new Display(image.width, image.height);
            window.size(image.width, image.height);
            window.add(image);
            window.render();
        return window;
    }
    
    /**
     * Returns the Class object associated with the class
     * or interface with the given name. This is similar
     * to Class.forName, except that it will check all
     * currently loaded packages for a class, rather
     * than having to supply the package names yourself.
     * 
     * @param name  Name of the class to search for, can
     *              either be the fully qualified class
     *              name or the simple name.
     *              
     * @return  The class defined by the given name or
     *          null if the class cannot be found.
     */
    public static final Class from_name(String name)
    {
        if(name.equals("byte"))        { return byte.class; }
        else if(name.equals("short"))  { return short.class; }
        else if(name.equals("char"))   { return char.class; }
        else if(name.equals("int"))    { return int.class; }
        else if(name.equals("long"))   { return long.class; }
        else if(name.equals("float"))  { return float.class; }
        else if(name.equals("double")) { return double.class; }
        else if(name.equals("boolean")){ return boolean.class; }
        
        try
        {
            return Class.forName(name);
        }
        catch(Exception e)
        {
            for(Package p : Package.getPackages())
            {
                try                 { return Class.forName(p.getName() + "." + name); }
                catch(Exception ex) { }
            }
        }
        
        return null;
    }
    
    
    /**
     * Finds all the classes that a given class extends from
     * as well as all the interfaces that it implements.
     * 
     * @param in    The class you wish to find superclasses of.
     * 
     * @return  An array containing all the superclasses and
     *          implemented interfaces of the given class.
     */
    public static final Class[] base_classes(Class in)
    {
        ArrayDeque<Class> output = new ArrayDeque<Class>();
        
        Class[] interfaces = in.getInterfaces();
        
        for(Class c : interfaces)   { output.add(c); }
        
        Class clazz = in.getSuperclass();
        
        while(clazz != null)
        {
            output.add(clazz);
            clazz = clazz.getSuperclass();
        }
        
        return output.toArray(new Class[0]);
    }
    
    /**
     * Function to pause program execution on the currently
     * running thread. Similar to Thread.sleep except that
     * failed execution is designated by booleans rather
     * than exceptions.
     * 
     * @param millis    Number of milliseconds to pause.
     * 
     * @return  true, if program paused for specified time.
     *          false, if Thread.sleep threw an exception.
     */
    public static final boolean pause(int millis)
    {
        try                 { Thread.currentThread().sleep(millis); }
        catch(Exception e)  { return false; }
        
        return true;
    }
}