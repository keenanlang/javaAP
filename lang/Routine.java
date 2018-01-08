package javaAP.lang;



public class Routine extends Obj
{
    private Thread current = new Thread();
    private Obj target;
    
    public void run(final String function, final Object targetin, final Object... input)
    {
        this.target = Obj.wrap(targetin);
        
        current = new Thread(new Runnable()
                             {
                                 public void run()
                                 {
                                     target.call(function, input);
                                 }
                             });
        current.start();
    }
    
    /**
     * Runs a given function in parallel a number of times, using 
     * the given inputs. Each iteration is run in series however.
     * 
     * @param amt       The number of times to run the function.
     * @param function  The name of the function to run.
     * @param input     The parameters required by the function.
     */
    public void loop(final int amt, final String function, final Object targetin, final Object... input)
    {
        this.target = Obj.wrap(targetin);
        
        current = new Thread(new Runnable()
                             {
                                 public void run()
                                 {
                                     for(int i = 0; i < amt; i += 1)
                                     {
                                         target.call(function, input);
                                     }
                                 }
                             });
        current.start();
    }
    
    /**
     * Returns whether this Routine is currently running a thread
     * or not.
     * 
     * @return false if no function is currently being run in 
     * a thread, true otherwise.
     */
    public boolean running()
    {
        return current.isAlive();
    }
}