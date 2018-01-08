package javaAP.util;

import javaAP.lang.Obj;

public class Maths extends Obj
{
    public static double min(double... values)
    {
        if(values.length == 0) { return 0.0; }
        
        double min = values[0];
        
        for (int i : Range.instance(1, values.length))    { min = Math.min(min, values[i]); }
        
        return min;
    }
    
    public static double max(double... values)
    {
        if(values.length == 0) { return 0.0; }
        
        double max = values[0];
        
        for (int i : Range.instance(1, values.length))    { max = Math.max(max, values[i]); }
        
        return max;
    }
    
    public static double average(double... values)
    {
        if(values.length == 0) { return 0.0; }
        
        double total = values[0];
        
        for (int i : Range.instance(1, values.length))    { total += values[i]; }
        
        return total / values.length;
    }
    
    public static int gcd(int numerator, int denomenator)
    {
        int top = Math.abs(numerator);
        int bot = Math.abs(denomenator);
        
        while (bot != 0)
        {
            int temp = bot;
            bot = top % bot;
            top = temp;
        }
        
        return top;
    }
    
    public static int lcm(int a, int b)
    {
        return (int) Math.abs(a * b) / gcd(a, b);
    }
}