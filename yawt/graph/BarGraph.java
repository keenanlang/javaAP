package javaAP.yawt.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

import javaAP.yawt.Component;
import javaAP.yawt.img.Colors;

public class BarGraph extends Component
{
    private Chart data;
    
    public BarGraph(Chart input)
    {
        data = input;
        this.name(data.title);
    }
    
    public void draw(Graphics2D pen)
    {
        Color[] allcolors = new Color[data.numrows()];
        
        if(data.contains("color"))
        {
            for(int i = 0; i < data.numrows(); i += 1)
                { allcolors[i] = data.fetch(Color.class, i, "color"); }
        }
        
        if(data.contains("Color"))
        {
            for(int i = 0; i < data.numrows(); i += 1)
                { allcolors[i] = data.fetch(Color.class, i, "Color"); }
        }
        
        double[] amounts = new double[data.numrows()];
        double[] amounts_sort = new double[data.numrows()];
        
        for(int i = 0; i < data.numrows(); i += 1)
        {
            amounts[i] = data.fetch(double.class, i, 1);
            amounts_sort[i] = amounts[i];
        }
        
        Arrays.sort(amounts_sort);
    }
}