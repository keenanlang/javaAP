package javaAP.yawt.graph;

import javaAP.util.Vector;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Chart implements Iterable<Row>
{
    private ArrayList<Row> myrows;
    private ArrayList<Column> mycols;
    public String title;
    
    public Chart()
    {
        myrows = new ArrayList<Row>();
        mycols = new ArrayList<Column>();
        title = "";
    }
    
    public Chart(String name)
    {
        this();
        title = name;
    }
    
    public int numrows() { return myrows.size(); }
    public int numcols() { return mycols.size(); }
    
    public void add(Column in)
    {
        mycols.add(in);
        
        for(int i = 0; i < numrows(); i += 1)   { myrows.get(i).add(null); }
    }
    
    public void add(Row in)         { myrows.add(in); }
    
    public void put(int row, String col, Object value)
        { this.put(row, index(col), value); }
        
    public void put(Vector point, Object value)
        { this.put((int) point.x, (int) point.y, value); }
        
    public void put(int row, int col, Object value)
        { myrows.get(row).set(col, value); }
    
    
    public int index(String name)
    {
        for(int i = 0; i < numcols(); i += 1)
        {
            if(name.equals(mycols.get(i).title)) { return i; }
        }
        
        return -1;
    }
    
    public Object fetch(int row, String col)
        { return this.fetch(row, index(col)); }
        
    public Object fetch(Vector point)
        {return this.fetch((int) point.x, (int) point.y); }
        
    public Object fetch(int row, int col)
        { return myrows.get(row).get(col); }
    
    public <T> T fetch(Class<T> clazz, int row, String col)
        { return (T) this.fetch(row, col); }
        
    public <T> T fetch(Class<T> clazz, int row, int col)
        { return (T) this.fetch(row, col); }
        
    public <T> T fetch(Class<T> clazz, Vector point)
        { return (T) this.fetch(point); }
    
    public Iterator<Column> cols()      { return this.columns(); }
    public Iterator<Column> columns()   { return mycols.iterator(); }
    
    public Iterator<Row> rows()         { return this.iterator(); }
    public Iterator<Row> iterator()     { return myrows.iterator(); }
    
    public Row row(int pos)             { return myrows.get(pos); }
    public Column col(int pos)          { return this.column(pos); }
    public Column col(String name)      { return this.column(name); }
    public Column column(String name)   { return this.column(index(name)); }
    public Column column(int pos)       { return mycols.get(pos); }
    
    public boolean contains(String name)    { return (index(name) != -1); }
    
    public Chart select(String... colnames)
    {
        Chart output = new Chart();
        
        for(int i = 0; i < colnames.length; i += 1)
        {
            output.add(this.column(colnames[i]));
        }
        
        for(Row r : this)
        {
            Row temp = new Row();
            for(int i = 0; i < colnames.length; i += 1)
            {
                temp.add(r.get(this.index(colnames[i])));
            }
            
            output.add(temp);
        }
        
        return output;
    }
    public Chart sort(String colname) {return null;}
    
    public Chart join(Chart other) {return null;}
}