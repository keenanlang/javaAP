package javaAP.yawt.img;

import java.awt.Color;
import javaAP.lang.Obj;
import java.util.Random;

/**
 * Colors contains a variety of named colors as well
 * as useful functions for dealing with colors.
 */
public class Colors extends Obj
{
    public static final Color crimson = new Color(0xDC143C);
    public static final Color salmon  = new Color(0xFA8072);
    
    public static final Color hot_pink = new Color(0xFF69B4);
    public static final Color deep_pink = new Color(0xFF1493);
    
    public static final Color coral = new Color(0xFF7F50);
    public static final Color dark_orange = new Color(0xFF8C00);
    public static final Color red_orange = new Color(0xFF4500);
    public static final Color brick_red = new Color(0xB22222);
    
    public static final Color gold = new Color(0xFFD700);
    public static final Color light_yellow = new Color(0xFFFFED);
    public static final Color khaki = new Color(0xF0E6BC);
    public static final Color dark_khaki = new Color(0xBDB76B);
    
    public static final Color lavender = new Color(0xE6E6FA);
    public static final Color violet = new Color(0xEE82EE);
    public static final Color dark_violet = new Color(0x9400D3);
    public static final Color indigo = new Color(0x4B0082);
    
    public static final Color yellow_green = new Color(0xADFF2F);
    public static final Color lime = new Color(0x00FF00);
    public static final Color dark_green = new Color(0x006400);
    public static final Color olive = new Color(0x808000);
    
    public static final Color teal = new Color(0x008080);
    public static final Color aquamarine = new Color(0x7FFFD4);
    public static final Color turquoise = new Color(0x40E0D0);
    public static final Color sky_blue = new Color(0x87CEEB);
    public static final Color dark_blue = new Color(0x00008B);
    
    public static final Color tan = new Color(0xD2B48C);
    public static final Color wheat = new Color(0xF5DEB3);
    public static final Color sienna = new Color(0xA0522D);
    
    public static final Color slate  = new Color(0x708090);
    
    private static final long seed = (new Random()).nextLong();

    
    /**
     * Generates an array of visually distinct Colors.
     * 
     * @param amt   The amount of Colors to generate.
     * 
     * @return  The generated array of Colors.
     */
    public static Color[] generatecolors(int amt)
    {
        Random rand = new Random(seed);
        Color[] out = new Color[amt];
        float total = 0.0f;
        float delta = (float) (1.0 / amt);
        
        for(int i = 0; i < amt; i += 1)
        {
            float hue = total;
            float sat = (float)(.9 + (.1 * (rand.nextFloat() - .5)));
            float bri = (float)(.9 + (.1 * (rand.nextFloat() - .5)));
            
            out[i] = Color.getHSBColor(hue, sat, bri);
            
            total += delta;
        }
        
        return out;
    }
    
    /**
     * Finds the complementary Color to a given input.
     * 
     * @param in    The original Color.
     * 
     * @return  A new Color whose RGB values are the
     *          RGB values of the original Color
     *          each xor-ed with 255.
     */
    public static Color complement(Color in)
    {
        int newR = in.getRed() ^ 255;
        int newG = in.getGreen() ^ 255;
        int newB = in.getBlue() ^ 255;
        
        return new Color(newR, newG, newB);
    }
}