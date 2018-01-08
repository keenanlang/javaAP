package javaAP.util;

import java.awt.event.*;
import javax.swing.Timer;

import javaAP.lang.Obj;

/**
 * Delay is a timed object used to check
 * if a certain amount of time has elapsed.
 */
public class Delay extends Obj implements ActionListener
{
    private final int RESOLUTION = 15;

    private int current;
    private int endpoint;
    private final Timer keeptime;
    private boolean done;
    
    //In milliseconds
    /**
     * Creates a new Delay with the specified
     * timing. Minimum timing resolution is
     * only 15 milliseconds so Delay is only
     * guaranteed to finish within that 15ms
     * of the given value.
     * 
     * @param x The amount of time in milliseconds
     *          that this Delay should check.
     */
    public Delay(int x)
    {
        endpoint = x;
        done = false;
        current = 0;
        keeptime = new Timer(RESOLUTION, this);
    }
    
    /**
     * If the Delay hasn't been started already,
     * start it.
     */
    public void start()
    {
        if(keeptime.isRunning()) { return; }
        
        done = false;
        
        keeptime.start();
    }
    
    /**
     * Pauses the countdown of the delay.
     */
    public void pause()
    {
        keeptime.stop();
    }
    
    /**
     * Pauses the countdown of the delay
     * and sets the progress back to zero.
     */
    public void stop()
    {
        keeptime.stop();
        current = 0;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        current += RESOLUTION;
        
        if (current > endpoint)
        {
            keeptime.stop();
            done = true;
        }
    }
    
    /**
     * Checks if the Delay has finished its current run
     * 
     * @return  true, if the Delay has reached the end of
     *          its given timespan for its most recent run.
     *          false, if the Delay is still running.
     */
    public boolean finished()   { return !keeptime.isRunning() && done; }
    
    /**
     * Returns the timespan that this Delay was created with.
     * 
     * @return  The timespan that this Delay counts towards.
     */
    public int length()         { return endpoint; }
    
    /**
     * Returns the current progress of the Delay towards its
     * length.
     * 
     * @return  The current value of time that has elapsed
     *          while the Delay has been running.
     */
    public int progress()       { return current; }
    
}