package javaAP.yawt.graph;

import java.awt.Color;
import javaAP.yawt.Display;

import javaAP.lang.Sys;

public class test
{
    public static void main(String args[])
    {
        Chart me = new Chart("test");
            me.add(new Column("name"));
            me.add(new Column("amount"));
            
            me.add(new Row(1,  .05));
            me.add(new Row(2,  .05));
            me.add(new Row(3,  .05));
            me.add(new Row(4,  .05));
            me.add(new Row(5,  .05));
            me.add(new Row(6,  .05));
            me.add(new Row(7,  .05));
            me.add(new Row(8,  .05));
            me.add(new Row(9,  .05));
            me.add(new Row(10, .05));
            me.add(new Row(11, .05));
            me.add(new Row(12, .05));
            me.add(new Row(13, .05));
            me.add(new Row(14, .05));
            me.add(new Row(15, .05));
            me.add(new Row(16, .05));
            me.add(new Row(17, .05));
            me.add(new Row(18, .05));
            me.add(new Row(19, .05));
            me.add(new Row(20, .05));
        
        PieGraph pg = new PieGraph(me);
            pg.size(400,400);
            
        Display out = Sys.display(pg);
       
        out.render();
    }
}