package javaAP.yawt;

import java.awt.Graphics2D;

import java.util.ArrayDeque;

import javaAP.lang.Obj;
import javaAP.util.Vector;

public abstract class Component extends Obj
{
    public int x,y,width,height;
    public String name;
    public boolean visible;

    private static int component_id = 0;
    
    protected Component()
    {
        component_id += 1;
        name = this.getClass().getSimpleName() + component_id;
        visible = true;
    }
    
    public void name(String in)     { name = in; }
    public String name()            { return name; }
    
    public void size(int w, int h)  { width = w; height = h; raise("resized", width, height);}
    public void size(Vector siz)    { width = (int) siz.x; height = (int) siz.y; raise("resized", width, height);}
    public Vector size()            { return Vector.instance(width, height); }
    
    public void position(int xin, int yin)  { x = xin; y = yin; }
    public void position(Vector pos)        { x = (int) pos.x; y = (int) pos.y; }
    public Vector position()                { return Vector.instance(x,y); }
    
    public void register(Object in)     { this.enlist(Obj.wrap(in)); }
    public void deregister(Object in)   { this.delist(Obj.wrap(in)); }
    
    public void raise(String event, Object... args)
    {
        this.call("this_" + event, args);
        this.raise(this.name + "_" + event, args);
    }
    
    protected void clicked(Vector p, String button)          { this.raise("clicked", p, button); }
    protected void doubleclicked(Vector p, String button)    { this.raise("doubleclicked", p, button); }
    protected void held(Vector p, String button)             { this.raise("held", p, button); }
    protected void released(Vector p, String button)         { this.raise("released", p, button); }
    protected void dragging(Vector p, String button)         { this.raise("dragging", p, button); }
    protected void hover(Vector p)                           { this.raise("hover", p); }
    protected void scrolled(Vector p, int amt)               { this.raise("scrolled", p, amt); }
    protected void keydown(String key)                       { this.raise("keydown", key); }
    protected void keyup(String key)                         { this.raise("keyup", key); }
    protected void chartyped(char key)                       { this.raise("chartyped", key); }
    
    public void render(Graphics2D pen)                       { this.draw(pen); }
    public void draw(Graphics2D pen) {}
}
