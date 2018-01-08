package javaAP.yawt.graph;

import javaAP.yawt.Component;
import javaAP.yawt.img.Colors;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

public class PieGraph extends Component
{
    private Chart data;

    public PieGraph(Chart input)
    {
        data = input;
        this.name(data.title);
    }
    
    public void draw(Graphics2D pen)
    {
        Color[] allcolors = Colors.generatecolors(data.numrows());
        
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
        
        double[] slices = new double[data.numrows()];
        
        int startAngle = 0;
        
        for(int i = 0; i < slices.length; i += 1)
        {
            slices[i] = data.fetch(double.class, i, 1);
            
            int numdegrees = (int) (360 * slices[i]);
            
            if(i == data.numrows() - 1) { numdegrees = 360 - startAngle; }
            
            Arc2D out = new Arc2D.Double(this.x, this.y, this.width, this.height,
                                        startAngle, numdegrees, Arc2D.PIE);
            
            pen.setColor(allcolors[i]);
            pen.fill(out);
            pen.setColor(Color.black);
            pen.draw(out);
            
            startAngle += numdegrees;
        }
    }
}