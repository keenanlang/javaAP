package javaAP.lang;

import javaAP.util.Range;

public class Hex extends Obj
{
    public static final Hex INVALID = new Hex(null);

    private StringBuilder data;
    
    private Hex(String data_in)
    {
        if (data_in == null)    { this.data = new StringBuilder(); }
        else                    { this.data = new StringBuilder(data_in.toUpperCase()); }
    }
    
    public static Hex instance()
    {
        return new Hex("");
    }
    
    public Hex join(Hex in)
    {
        if(this == INVALID || in == INVALID)    { return INVALID; }
        else                                    { return new Hex(data + in.toString()); }
    }
    
    public Hex join(Object in)
    {
        return this.join(Obj.wrap(in).as(Hex.class));
    }
   
    public Hex append(Hex in)
    {
        if(this == INVALID || in == INVALID)    { return INVALID; }
        else
        {
            data.append(in.toString());
            
            return this;
        }
    }
    
    public Hex append(Object in)
    {
        return this.append(Obj.wrap(in).as(Hex.class));
    }
    
    private static String convert(long input, int bytes)
    {
        String hexval = Long.toHexString(input).toUpperCase();

        int extra_zeroes = (bytes * 2) - hexval.length();
        
        for (int i = 0; i < extra_zeroes; i += 1) { hexval = "0" + hexval; }
        
        return hexval;
    }
    
    public byte  __byte__()                   { return (byte) __long__(); }
    public static Hex __byte__(byte input)    { return __char__((char) input); }
    
    public char  __char__()                   { return (char) __long__(); }
    public static Hex __char__(char input)    { return new Hex(convert(input, 1)); }
    
    public short __short__()                  { return (short) __long__(); }
    public static Hex __short__(short input)  { return new Hex(convert(input, 2)); }
    
    public int   __int__()                    { return (int) __long__(); }
    public static Hex __int__(int input)      { return new Hex(convert(input, 4)); }
    
    public long  __long__()                   { return Long.parseLong(data.toString(), 16); }
    public static Hex __long__(long input)    { return new Hex(convert(input, 8)); }
    
    public char[] __chararray__()
    {
        int len = (int) data.length() / 2;
        
        char[] output = new char[len];
        
        Range test = Range.from(Range.class, output);
        
        for(int index : Range.from(Range.class, output))
        {
            output[index] = (char) Integer.parseInt(data.substring(2 * index, 2 * index + 2), 16);
        }
        
        return output;
    }
    
    public static Hex __chararray__(char[] input)
    {
        String temp = "";
        
        for(int index : Range.from(Range.class, input))
        {
            temp += convert(input[index], 1);
        }
        
        return new Hex(temp);
    }
    
    public String __String__()                   { return new String(__chararray__()); }
    public static Hex __String__(String input)   { return __chararray__(input.toCharArray()); }
    
    public float __float__()                     { return Float.intBitsToFloat(__int__()); }
    public static Hex __float__(float input)     { return Hex.from(Hex.class, Float.floatToRawIntBits(input)); }
    
    public double __double__()                   { return Double.longBitsToDouble(__long__()); }
    public static Hex __double__(double input)   { return Hex.from(Hex.class, Double.doubleToRawLongBits(input)); }
    
    public String toString()    { return data.toString(); }
}
