package javaAP.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayDeque;

import javaAP.util.BiMap;


public class Obj
{
    /**
     * Represents a non-existant method or attribute.
     */
    public static final Obj VOID = new Obj()
    {
        public Object __self__()         { return this; }
        public <T> T as(Class<T> type)   { return null; }
        public String toString()         { return "VOID"; }
    };
    
    private final boolean isref, isstatic;
    private final Object mydata;
    
    private static final BiMap PRIMITIVE_SWAP = new BiMap();
    static
    {
        PRIMITIVE_SWAP.put(byte.class,      Byte.class);
        PRIMITIVE_SWAP.put(short.class,     Short.class);
        PRIMITIVE_SWAP.put(char.class,      Character.class);
        PRIMITIVE_SWAP.put(int.class,       Integer.class);
        PRIMITIVE_SWAP.put(long.class,      Long.class);
        PRIMITIVE_SWAP.put(float.class,     Float.class);
        PRIMITIVE_SWAP.put(double.class,    Double.class);
        PRIMITIVE_SWAP.put(boolean.class,   Boolean.class);
    }
    
    private final HashSet<Obj> listeners = new HashSet<Obj>();
    
    
    // **************
    // Getter Methods
    // **************
    /**
     * Gets the data that this Obj points to.
     * 
     * @return  The object this Obj was constructed with or
     *          this if not constructed with an object
     */
    public Object __self__()
    {
        if (!isref)   { return this; }
        else          { return mydata; }
    }
    
    /**
     * Gets the class of the data that this Obj points to.
     * 
     * @return  The class of the Object that this Obj
     *          was constructed with or the class of 
     *          this Obj if not constructed with an
     *          object.
     */
    public Class __class__()
    {
        Object o = this.__self__();
        
        if(isstatic)       { return (Class) o; }
        else if(o == null) { return Obj.class; }
        else               { return o.getClass(); }
    }
    
    public Class __Class__()
    {
        return __class__();
    }
    
    
    // ************
    // Constructors
    // ************
    
    protected Obj()
    {
        isref = false;
        isstatic = false;
        mydata = null;
    }
    
    private Obj(Object data)
    {
        isref = true;
        
        if(data instanceof Obj)
        {
            isstatic = ((Obj) data).isstatic;
            mydata = ((Obj) data).__self__();
        }
        else if(data instanceof Class)
        {
            if(((Class) data).isPrimitive())  { mydata = PRIMITIVE_SWAP.fetch(data); }
            else                            { mydata = data; }
            
            isstatic = true;
        }
        else
        {
            isstatic = false;
            mydata = data;
        }
    }
    
    /**
     * Constructs an Obj pointing to an Object.
     * 
     * @param in    The Object for this Obj to
     *              point to.
     */
    public final static Obj wrap(Object in)
    {
        if (in == VOID)    { return VOID; }
        else               { return new Obj(in); }
    }

    
    // ************
    // Call Methods
    // ************
    
    /**
     * Calls a method of the Object this points to and casts it to
     * a specified type.
     * 
     * @param type      The return type of the method.
     * @param name      The name of the method to call.
     * @param params    The parameters of the method to be called.
     * 
     * @return  The output of the method called or null if no such
     *          method exists.
     */
    public <T> T call(Class<T> type, String name, Object... params)
    {
        return this.call(name, params).as(type);
    }
    
    /**
     * Calls a method of the Object this points to.
     * 
     * @param name      The name of the method to call.
     * @param params    The parameters of the method to be called.
     * 
     * @return  An Obj pointing to the output of the method called,
     *          null if the method returned null, or VOID if no such
     *          method exists.
     */
    public Obj call(String name, Object... params)
    {   
        Class[] types = new Class[params.length];
        Object[] args = new Object[params.length];
        
        for(int x = 0; x < params.length; x += 1)
        {
            Obj o = new Obj(params[x]);
            
            types[x] = o.__class__();
            args[x] = o.__self__();
        }
        
        return this.call(name, types, args);
    }
    
    private Obj call(String name, Class[] types, Object[] args)
    {
        Method tocall = find_method(name, types);
        
        if (tocall == null) { return VOID; }
        
        if (Modifier.isStatic(tocall.getModifiers()))
        {
            try
            {
                Method test = this.__class__().getDeclaredMethod(tocall.getName(), tocall.getParameterTypes());
            }
            catch (Exception e)
            {
                return VOID;
            }
        }
        
        try
        {
            Object output;
            
            if(isstatic) { output = tocall.invoke(((Class) this.__self__()).cast(null), args); }
            else         { output = tocall.invoke(this.__self__(), args); }
            
            if (output == null) { return null; }
            else                { return new Obj(output); }
        }
        catch(IllegalAccessException iae)       { return VOID; }
        catch(IllegalArgumentException iae2)    { return VOID; }
        catch(InvocationTargetException ite)    { return new Obj(ite.getCause()); }
        
    }
    
    
    // ***********
    // Get Methods
    // ***********
    
    /**
     * Retrieves a member of the object this points
     * to and casts it to a specified class. Initially
     * calls __membername__() and then tries to use
     * reflection, finally reverts to __get__(name)
     * 
     * @param type  The class type to return as.
     * @param name  The name of the data member to 
     *              retrieve.
     *              
     * @return  The data member accessed if it exists or
     *          null if it doesn't.
     */
    public <T> T get(Class<T> type, String name)
    {
        Obj temp = this.get(name);
        
        if(temp == null)    { return null; }
        else                { return temp.as(type); }
    }
    
    
    /**
     * Retrieves a member of the object this points
     * to, returning an Obj reference to that value.
     * Initially calls __membername__() and if that
     * method is not specified then will search using
     * reflection. Finally, if nothing suitable is 
     * found, the name is passed to a generic 
     * __get__(name) method.
     * 
     * @param namein    The name of the data member
     *                  to retrieve.
     *                  
     * @return  An Obj reference to the data member
     *          specified or VOID if there is no
     *          member named as such.
     */
    public Obj get(String namein)
    {        
        if(namein.contains("("))        { return get_function(namein); }
        else                            { return get_data_member(namein); }
    }
    
    public Obj __get__(String name)
    {
        return VOID;
    }
    
    
    // ***********
    // Set Methods
    // ***********
    
    /**
     * Sets a data member of this Obj to a 
     * specified value. Tries to call
     * __membername__(input) if it exists, 
     * then moves on to try reflection. If 
     * nothing suitable is found, a generic 
     * __set__(name, input) method is called.
     * 
     * @param name  The name of the data
     *              member to set.
     * @param in    The value to set the
     *              member to.
     */
    public void set(String name, Object in)
    {   
        Obj o = new Obj(in);
        
        if(this.call("__" + name + "__", in) == VOID)
        {
            boolean exception = false;
            Field to_access;
            
            try
            {
                to_access = this.__class__().getField(name);
            }
            catch(NoSuchFieldException e)
            {
                to_access = null;
            }
            
            if(to_access != null && in.getClass() != to_access.getType())
            {
                try
                {
                    this.set(name, o.as(to_access.getType()));
                }
                catch(ClassCastException e)
                {
                    exception = true;
                }
            }
            else if(to_access == null)
            {
                exception = true;
            }
            else
            {
                try
                {
                    to_access.set(this.__self__(), o.__self__());
                }
                catch(IllegalAccessException iae)       { exception = true; }
                catch(IllegalArgumentException iae2)    { exception = true; }
            }
            
            if(exception)
            {
                this.call("__set__", name, in);
            }
        }
    }
    
    public void __set__(String name, Object input)
    {
    }
    
    
    // ************
    // Cast Methods
    // ************
    
    /**
     * Casts this Obj reference to a specified
     * class. A transition can be specified 
     * as a function named __casttype__(), if
     * an array is specified as the type, the
     * casttype must be appended with 'array'
     * before , if no cast function is
     * specified the data the Obj points to is
     * cast and returned.
     * 
     * @param type  The type to cast this Obj to.
     * 
     * @return  A systematic transformation of
     *          the data that this Obj points to.
     */
    
    public <T> T as(Class<T> type)
    {       
        String classname = type.getSimpleName();
        classname = classname.replace("[]", "array");
        
        Obj out = this.call("__" + classname + "__");
        
        try
        {
            Obj output;
            
            if(out != VOID)
            {
                output = out;
            }
            else
            {
                Class other = (Class) PRIMITIVE_SWAP.fetch(type);
                
                if(other != null)
                {
                    Obj out2 = this.call("__" + other.getSimpleName() + "__");
                    
                    if(out2 != VOID)
                    {
                        output = out2;
                    }
                }
                
                output = this;
            }
            
            if (type.isPrimitive())
            {
                Class other = (Class) PRIMITIVE_SWAP.fetch(type);
                
                return (T) other.cast(output.__self__());
            }
            else
            {
                return type.cast(output.__self__());
            }
        }
        catch (ClassCastException e)
        {
            return Obj.wrap(type).from(type, this.__self__());
        }
    }

    public static <T> T from(Class<T> type, Object input)
    {        
        Obj current = new Obj(type);
        
        String classname = input.getClass().getSimpleName();
        classname = classname.replace("[]", "array");
        
        Obj out = current.call("__" + classname + "__", input);
        
        if(out != VOID)
        {
            return out.as(type);
        }
        else
        {
            Class other = (Class) PRIMITIVE_SWAP.fetch(input.getClass());
            
            if(other != null)
            {
                Obj out2 = current.call("__" + other.getSimpleName() + "__", input);
                
                if(out2 != VOID)
                {
                    return out2.as(type);
                }
            }
            
            return null;
        }
    }
    
    public <T> T create(Class<T> type, Object... params)
    {
        Obj output = create(params);
        
        return (output == null) ? null : output.as(type);
    }
    
    public Obj create(Object... params)
    {
        Class[] types = new Class[params.length];
        Object[] args = new Object[params.length];
        
        for(int x = 0; x < params.length; x += 1)
        {
            Obj o = new Obj(params[x]);
            
            types[x] = o.__class__();
            args[x] = o.__self__();
        }
        
        try  { return new Obj(__class__().getConstructor(types).newInstance(args)); }
        catch(NoSuchMethodException e)      { return VOID; }
        catch(IllegalArgumentException iae) { return VOID; }
        catch(InstantiationException ie)    { return VOID; }
        catch(IllegalAccessException iae2)  { return VOID; }
        catch(InvocationTargetException ite){ return new Obj(ite.getCause()); }
    }
    
    
    // ***************
    //  Event Methods
    // ***************
    
    protected void enlist(Obj listener)
    {
        listeners.add(listener);
        call("listener_added", listener);
    }
    
    protected void delist(Obj listener)
    {
        listeners.remove(listener);
        call("listener_removed", listener);
    }
    
    public void observe(Obj other)
    {
        other.enlist(this);
    }
    
    public void ignore(Obj other)
    {
        other.delist(this);
    }
    
    /**
     * @return  true, if any of the listeners had a function to respond to the event
     *          false, if no listener had a correct event.
     */
    public boolean signal(String event, Object... data)
    {
        Object[] newdata = java.util.Arrays.copyOf(data, data.length + 1);
        System.arraycopy(data, 0, newdata, 1, data.length);
        newdata[0] = this;
        
        boolean output = false;
        
        for (Obj listener : listeners)
        {
            Obj temp;
            
            if (listener == this)    { temp = listener.call(event, data); }
            else                     { temp = listener.call(event, newdata); }
            
            
            if (temp != VOID)    {  output = true; }
        }
        
        return output;
    }    
    
    
    // *****
    // Misc.
    // *****
    
    public String __String__()
    {
        return this.toString();
    }
    
    public boolean __boolean__()
    {
        return this.__self__() != null;
    }
    
    /**
     * @return The result of the toString() method of
     *         the object that this Obj points to, or
     *         the baseline Object if it points to
     *         itself.
     */
    public String toString()
    {
        if (!isref)    { return super.toString(); }
        else           { return mydata.toString(); }
    }

    
    // ****************
    // Helper Functions
    // ****************
   
    private Obj get_function(String header)
    {
        int index = header.indexOf("(");
        
        String method_name = header.substring(0, index).trim();
        String param_types = header.substring(index + 1, header.length() - 1).trim();
        
        if(param_types.length() == 0)   { return get_function_no_params(method_name); }
        else                            { return get_function(method_name, param_types); }
    }
    
    private Obj get_function_no_params(String method_name)
    {
        Class[] parameters = new Class[0];
        
        Method m = this.find_method(method_name, parameters);
        
        return (m == null) ? VOID : new Function(this.__self__(), m);
    }
    
    private Obj get_function(String method_name, String param_types)
    {
        String[] params = param_types.split(",");    
        Class[] parameters = new Class[params.length];
        
        for(int i = 0; i < params.length; i += 1)
        {
            parameters[i] = Sys.from_name(params[i].trim());
        }
        
        Method m = this.find_method(method_name, parameters);
        
        return (m == null) ? VOID : new Function(this.__self__(), m);

    }
  
    private Obj get_data_member(String name)
    {        
        Obj temp = this.call("__" + name + "__");
        
        if(temp == VOID)
        {
            try
            {
                Object out = this.__class__().getField(name).get(this.__self__());
                
                if   (out == null) { temp = null; }
                else               { temp = new Obj(out); }
            }
            catch (NoSuchFieldException e)
            {
                Obj out = this.call("__get__", name);
                
                if (out == VOID)    { return __get__(name); }
                else                { return out; }
            }
            catch (IllegalAccessException iae)
            {
                Obj out = this.call("__get__", name);
                
                if (out == VOID)    { return __get__(name); }
                else                { return out; }
            }
        }
        
        return temp;
    }
    
    
    private Method find_method_strict(String name, Class[] set)
    {
        try
        {
            Class mine = this.__class__();
            Method out = mine.getMethod(name, set);
            return out;
        }
        catch (NoSuchMethodException e)
        {
            return null;
        }
    }
    
    private Method find_method(String method_name, Class[] param_types)
    {        
        Method direct = this.find_method_strict(method_name, param_types);
        
        if(direct != null) { return direct; }

        Method[] all_methods = this.__class__().getMethods();
        ArrayDeque<Method> with_name_and_param_length = new ArrayDeque<Method>();
        
        for(Method check : all_methods)
        {
            if(method_name.equals(check.getName()))
            {
                if(param_types.length == check.getParameterTypes().length)
                {
                    with_name_and_param_length.add(check);
                }
            }
        }
        
        if(with_name_and_param_length.size() == 0)
        {
            return null;
        }
        else if(with_name_and_param_length.size() == 1)
        {
            return with_name_and_param_length.peek();
        }
        else
        {
            for(Method test : with_name_and_param_length)
            {
                Class[] params = test.getParameterTypes();
                
                boolean equals_overall = true;
                
                for(int i = 0; i < params.length; i += 1)
                {
                    if(! params[i].equals(param_types[i]))
                    {
                        Class[] supers = Sys.base_classes(param_types[i]);
                        
                        boolean equals_param = false;
                        for(Class c : supers)
                        {
                            if(c.equals(param_types[i]))
                            {
                                equals_param = true;
                                break;
                            }
                        }
                        
                        if(! equals_param)
                        {
                            equals_overall = false;
                            break;
                        }
                    }
                }
                
                if(equals_overall)      { return test; }
            }
        }
        
        return null;
    }
}