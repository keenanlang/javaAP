package javaAP.lang;

import java.lang.reflect.Method;


/**
 * Class to implement basic first-class function abilities
 * in Java. Generally used by the Obj class to return
 * functions from objects, however it can be used as an
 * anonymous class to create a function not associated with
 * any object.
 */
public class Function extends Obj
{
    /**
     * Function simple name
     */
    public final String name;
    
    /**
     * Class types for each parameter needed
     * by this function in the same order as
     * would be given by invoking the function.
     */
    public final Class[] param_types;
    
    /**
     * Class type of the objects returned by
     * this function.
     */
    public final Class return_type;

    private final Method toinvoke;
    private Object target;
    
    private boolean anonymous;
    
     /**
     * Constructor for Functions created as
     * anonymous classes. Uses the data from
     * the first defined function in the
     * anonymous class.
     */
    protected Function()
    {
        Method mine;
        
        try                 { mine = this.__class__().getMethods()[0]; }
        catch(Exception e)  { mine = null; }
        
        toinvoke = mine;
        
        name = toinvoke.getName();
        target = null;
        
        param_types = toinvoke.getParameterTypes();
        return_type = toinvoke.getReturnType();
        
        anonymous = true;
    }
    
    Function(Object targ, Method method_in)
    {
        name = method_in.getName();
        target = targ;
        
        toinvoke = method_in;
        
        param_types = toinvoke.getParameterTypes();
        return_type = toinvoke.getReturnType();
        
        anonymous = false;
    }
    
    public Obj call(String name, Object... params)
    {          
        Class[] types = new Class[params.length];
        Object[] args = new Object[params.length];
        
        for(int x = 0; x < params.length; x += 1)
        {
            Obj o = Obj.wrap(params[x]);
            
            types[x] = o.__class__();
            args[x] = o.__self__();
        }
        
        return this.call(name, types, args);
    }
    
    private Obj call(String name, Class[] types, Object[] args)
    {
        if(target != null && anonymous)
        {            
            Obj output = (Obj.wrap(target)).call(name, args);
            
            return output;
        }
        else
        {
            return super.call(name, args);
        }
    }
    
    public Obj get(String namein)
    {
        if(target != null && anonymous)
        {
            return (Obj.wrap(target)).get(namein);
        }
        else
        {
            return super.get(namein);
        }
    }
    
    public void set(String name, Object in)
    {
        if(target != null && anonymous)
        {
            (Obj.wrap(target)).set(name, in);
        }
        else
        {
            super.set(name, in);
        }
    }
    
    /**
     * Calls the function defined by this class.
     * 
     * @param params    List of objects that are
     *                  required by the function
     *                  as parameters.
     *                  
     * @return  An Obj linked to the output of the
     *          function.
     */
    public Obj invoke(Object... params)
    {
        if(!anonymous)
        {
            Object targ = (target == null) ? this : target;
            
            return (Obj.wrap(targ)).call(name, params);
        }
        else
        {
            return this.call(name, params);
        }
    }
    
    /**
     * Calls the function defined by this class on an
     * object.
     * 
     * @param targ      The object to invoke this method
     *                  upon.
     * 
     * @param params    List of objects that are
     *                  required by the function
     *                  as parameters.
     *                  
     * @return  An Obj linked to the output of the
     *          function.
     */
    public Obj invoke_on(Object targ, Object... params)
    {
        Object temp = target;
        target = targ;
        
        Obj output = (Obj.wrap(this)).call(name, params);
        
        target = temp;
        
        return output;
    }
    
    public String toString()
    {
        String full = this.name + "(";
        
        for(int i = 0; i < param_types.length; i += 1)
        {
            if(i > 0) { full = full + ", "; }
            
            full = full + param_types[i].getName();
        }
        
        full = full + ")";
        
        return full;
    }
}