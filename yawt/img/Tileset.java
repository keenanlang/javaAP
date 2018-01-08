package javaAP.yawt.img;

import javaAP.lang.Obj;
import javaAP.util.BiMap;
import javaAP.util.Vector;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.File;


public class Tileset extends Obj
{
    private BufferedImage mytiles;
    private final BiMap names;
    
    public Vector tilesize;
    
    public void tilesize_set(Vector v)  { tilesize = v; }
    public void tilewidth_set(int amt)  { this.setTileWidth(amt); }
    public void tileheight_set(int amt) { this.setTileHeight(amt); }
    
    public int tilewidth_get()          { return (int) tilesize.x; }
    public int tileheight_get()         { return (int) tilesize.y; }
    public int width_get()              { return mytiles.getWidth(); }
    public int height_get()             { return mytiles.getHeight(); }

    public Tileset(String filename)
    {
        this(new File(filename));
    }
    
    public Tileset(File inputfile)
    {
        try
        {
            mytiles = ImageIO.read(inputfile);
        }
        catch(Exception e)
        {
            mytiles = null;
        }
        
        names = new BiMap();
        tilesize = Vector.instance(0,0);  
    }
    
    public int tilesWide()
    {
        if(this.getTileWidth() == 0) { return 0; }
        
        return (int) (this.getWidth() / this.getTileWidth());
    }
    
    public int tilesHigh()
    {
        if(this.getTileHeight() == 0) { return 0; }
        
        return (int) (this.getHeight() / this.getTileHeight());
    }
    
    public int getWidth()   { return mytiles.getWidth(); }
    public int getHeight()  { return mytiles.getHeight(); }
    
    public int getTileWidth()   { return (int) tilesize.x; }
    public int getTileHeight()  { return (int) tilesize.y; }
    public Vector getTileSize() { return tilesize; }
    
    public void setTileWidth(int amt)           { tilesize = Vector.instance(amt, tilesize.y); }
    public void setTileHeight(int amt)          { tilesize = Vector.instance(tilesize.x, amt); }
    public void setTileSize(int wid, int hei)   { tilesize = Vector.instance(wid, hei); }
    public void setTileSize(Vector v)           { tilesize = v; }
    
    public void name(String title, Vector v)    { names.put(title, v); }
    public void name(String title, int x, int y){ names.put(title, Vector.instance(x,y)); }
    
    public BufferedImage fetch(String title)
    {
        Vector v = (Vector) names.fetch(title);
        
        return this.fetch(v);
    }
    
    public BufferedImage fetch(Vector v)
    {
        if(v != null)
        {
            int x_start = (int) (v.x * tilesize.x);
            int y_start = (int) (v.y * tilesize.y);
            
            return mytiles.getSubimage(x_start, y_start, (int) tilesize.x, (int) tilesize.y);
        }
        else
        {
            return null;
        }
    }
    
    public BufferedImage fetch(int id)
    {
        int y_start = (int) ((id / this.tilesWide()) * tilesize.y);
        int x_start = (int) ((id % this.tilesWide()) * tilesize.x);
        
        return mytiles.getSubimage(x_start, y_start, (int) tilesize.x, (int) tilesize.y);
    }
    
    public int getID(String title)
    {
        Vector v = (Vector) names.fetch(title);
        
        return (int) (v.y * this.tilesWide() + v.x);
    }
    
    public String getTileName(int id)
    {
        int x = id % this.tilesWide();
        int y = (int) (id / this.tilesWide());
        
        return (String) names.fetch(Vector.instance(x,y));
    }
}