package javaAP.yawt;

import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javaAP.util.Vector;

import java.awt.Graphics2D;

public class Container extends Component implements Iterable<Component>
{
    private HashMap<String, Component> names;
    private ArrayDeque<Component> mycomponents;
    private boolean focus_lock = false;
    
    public Container()
    {
        names = new HashMap<String, Component>();
        mycomponents = new ArrayDeque<Component>();
    }
    
    public Container(int xin, int yin, int wid, int hei)
    {
        this();
        this.position(xin, yin);
        this.size(wid, hei);
    }
    
    public Container(Vector pos, int wid, int hei)
    {
        this();
        this.position(pos);
        this.size(wid, hei);
    }
    
    public Container(Vector pos, Vector siz)
    {
        this();
        this.position(pos);
        this.size(siz);
    }
    
    public Iterator<Component> iterator()  { return mycomponents.iterator(); }
    
    public void lock_focus()    { focus_lock = true; }
    public void unlock_focus()  { focus_lock = false; }
    
    public Component focus()
    {
        try                             { return mycomponents.getFirst(); }
        catch(NoSuchElementException e) { return null; }
    }
    
    public void focus(Component tofocus)
    {
        if(! focus_lock)
        {
            boolean has = mycomponents.remove(tofocus);
            if(has) { mycomponents.addFirst(tofocus); }
        }
    }
    
    public void add(String component_name, Component toadd)
    {
        toadd.name(component_name);
        toadd.register(this);
        
        if(! focus_lock)    { mycomponents.addFirst(toadd); }
        else
        {
            Component temp = this.focus();
            mycomponents.addFirst(toadd);
            this.focus(temp);
        }
        
        names.put(component_name, toadd);
    }
    
    public void add(Component toadd)   { this.add(toadd.name, toadd); }
    
    public boolean remove(String component_name)
        { return mycomponents.remove(names.remove(component_name)); }
        
    public boolean remove(Component toremove)
        { return this.remove(toremove.name); }
    
    public Component fetch(String component_name)
        { return names.get(component_name); }
        
    public Component survey(Vector pos)
    {
        for(Component comp : this)
        {
            if(pos.within(comp.position(), comp.size()))
                { return comp; }
        }
        
        return null;
    }
    
    public Component survey(int x, int y)
        { return this.survey(Vector.instance(x,y)); }
        
    public void render(Graphics2D pen)
    {
        Graphics2D copy = (Graphics2D) pen.create();
        
        this.draw(pen);
        
        for(Iterator<Component> iter = mycomponents.descendingIterator(); iter.hasNext(); )
        {
            Graphics2D pen2 = (Graphics2D) copy.create();
            
            Component c = iter.next();
            
            if(c.visible)
            {
                pen2.translate(c.x, c.y);
            
                c.render(pen2);
            }
            
            pen2.dispose();
        }
        
        copy.dispose();
    }
    
    protected void keydown(String key)
    {
        this.raise("keydown", key);
        
        if(this.focus() != null)    { this.focus().keydown(key); }
    }
    
    protected void keyup(String key)
    {
        this.raise("keyup", key);
        
        if(this.focus() != null)    { this.focus().keyup(key); }
    }
    
    protected void held(Vector pos, String button)
    {
        this.raise("held", pos, button);
        
        Component grabbed = this.survey(pos);
        
        if(grabbed != null)
        {
            this.focus(grabbed);
            this.lock_focus();
            
            grabbed.held(pos.offset(grabbed.position().scale(-1)), button);
        }
    }
    
    protected void dragging(Vector pos, String button)
    {
        this.raise("dragging", pos, button);
        
        if(this.focus() != null)
            { this.focus().dragging(pos.offset(this.focus().position(), -1), button); }
    }
    
    protected void released(Vector pos, String button)
    {
        this.raise("released", pos, button);
        
        if(this.focus() != null)
            { this.focus().released(pos.offset(this.focus().position(), -1), button); }
    }
    
    protected void chartyped(char key)
    {
        this.raise("chartyped", key);
        
        if(this.focus() != null)    { this.focus().chartyped(key); }
    }
    
    protected void hover(Vector pos)
    {
        this.raise("hover", pos);
        
        Component comp = this.survey(pos);
        
        if(comp != null)    { comp.hover(pos.offset(comp.position(), -1)); }
    }
    
    protected void scrolled(Vector pos, int amt)
    {
        this.raise("scrolled", pos, amt);
        
        Component comp = this.survey(pos);
        
        if(comp != null)    { comp.scrolled(pos.offset(comp.position(), -1), amt); }
    }
    
    protected void clicked(Vector pos, String button)
    {
        this.raise("clicked", pos, button);
        
        Component comp = this.survey(pos);
        
        if(comp != null)
        {
            this.focus(comp);
            comp.clicked(pos.offset(comp.position(), -1), button);
        }
    }
    
    protected void doubleclicked(Vector pos, String button)
    {
        this.raise("doubleclicked", pos, button);
        
        Component comp = this.survey(pos);
        
        if(comp != null)    { comp.doubleclicked(pos.offset(comp.position(), -1), button); }
    }
}