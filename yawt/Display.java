package javaAP.yawt;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.event.*;

import javaAP.util.Vector;
import javaAP.util.Delay;


public class Display extends Container implements KeyListener, MouseListener, MouseMotionListener,
                                                  MouseWheelListener, WindowListener, ComponentListener
{
    private Frame myframe;
    private Canvas mydrawarea;
    private Graphics2D mypen;
    private BufferStrategy mybs;
    private static int windows_open;
    
    private Delay clickpause;
    
    public Display(int wid, int hei)
    {
        mydrawarea = new Canvas();
            mydrawarea.setSize(wid, hei);
            mydrawarea.addKeyListener((KeyListener) this);
            mydrawarea.addMouseListener((MouseListener) this);
            mydrawarea.addMouseMotionListener((MouseMotionListener) this);
            mydrawarea.addMouseWheelListener((MouseWheelListener) this);
            mydrawarea.setIgnoreRepaint(true);
            
        myframe = new Frame();
            myframe.addWindowListener((WindowListener) this);
            myframe.addComponentListener((ComponentListener) this);
            myframe.setIgnoreRepaint(true);
            myframe.add(mydrawarea);
            myframe.setVisible(true);
            
        this.position(0,0);
        this.size(wid, hei);
            
        clickpause = new Delay(45);
            clickpause.start();
        windows_open += 1;
    }
    
    public void name(String title)
    {
        super.name(title);
        myframe.setTitle(title);
    }
    
    public void size(int wid, int hei)
    {
        Insets border = myframe.getInsets();
        myframe.setSize(new Dimension(wid + (border.left + border.right), hei + (border.top + border.bottom)));
        
        resize_helper(wid,hei);
    }
    
    private void resize_helper(int wid, int hei)
    {
        Insets border = myframe.getInsets();
        
        mydrawarea.setLocation(border.left, border.top);
        mydrawarea.setSize(wid, hei);
        mydrawarea.createBufferStrategy(1);
        
        mybs = mydrawarea.getBufferStrategy();
        mypen = (Graphics2D) mybs.getDrawGraphics();
        
        this.width = wid;
        this.height = hei;
        
        this.raise("resized", width, height);
        
        this.render();
    }
    
    public void componentResized(ComponentEvent e)
    {
        Insets border = myframe.getInsets();
        
        resize_helper(myframe.getWidth() - (border.left + border.right), myframe.getHeight() - (border.top + border.bottom));
    }
    
    public void render()
    {    
        this.render(mypen);
        
        //mydrawarea.repaint();
        mybs.show();
        Toolkit.getDefaultToolkit().sync();
    }
    
    public String buttontype(int i)
    {
        if      (i == MouseEvent.BUTTON1)   { return "left"; }
        else if (i == MouseEvent.BUTTON2)   { return "middle"; }
        else if (i == MouseEvent.BUTTON3)   { return "right"; }
        else                                { return "other"; }
    }
    
    public void mouseClicked(MouseEvent e)
    {
        Vector pos = Vector.instance(e.getX(), e.getY());
        String button = buttontype(e.getButton());
        
        if(clickpause.finished())
        {
            this.clicked(pos, button);
            clickpause.start();
        }
        else
        {
            this.doubleclicked(pos, button);
            clickpause.stop();
        }
    }
    
    public void mousePressed(MouseEvent e)
    {
        Vector p = Vector.instance(e.getX(), e.getY());
        String button = this.buttontype(e.getButton());
        
        this.held(p, button);
    }
    
    public void mouseReleased(MouseEvent e)
    {
        Vector p = Vector.instance(e.getX(), e.getY());
        String button = this.buttontype(e.getButton());
        
        this.released(p, button);
    }
    
    public void mouseDragged(MouseEvent e)
    {
        Vector p = Vector.instance(e.getX(), e.getY());
        String button = this.buttontype(e.getButton());
        
        this.dragging(p, button);
    }
    
    public void mouseMoved(MouseEvent e)
    {
        Vector p = Vector.instance(e.getX(), e.getY());
        
        this.hover(p);
    }
    
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        Vector p = Vector.instance(e.getX(), e.getY());
        int amt = e.getWheelRotation();
        
        this.scrolled(p, amt);
    }
    
    public void close()
    {
        windows_open -= 1;
        
        this.raise("closing");
        
        myframe.dispose();
    }
    
    public void keyPressed(KeyEvent e)
    {              
        this.keydown(decode(e));
    }
    
    public void keyReleased(KeyEvent e)
    {
        this.keyup(decode(e));
    }
    
    private String decode(KeyEvent e)
    {
        char test = e.getKeyChar();
        
        String output = (test == e.CHAR_UNDEFINED) ? 
                e.getKeyText(e.getKeyCode()) : 
                test + "";
        
        if(test == e.VK_ENTER)      { output = "Enter"; }
        else if(test == e.VK_ESCAPE){ output = "Escape"; }
        
        return output;
    }
    
    public void keyTyped(KeyEvent e) { this.chartyped(e.getKeyChar()); }
    
    public void windowClosing(WindowEvent e)
    {
        windows_open -= 1;
        
        if(windows_open == 0)
        {
            System.exit(0);
        }
        else
        {
            e.getWindow().dispose();
        }
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void componentHidden(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
}