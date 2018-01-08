package javaAP.net;

import java.net.*;
import java.io.File;

import javaAP.lang.Obj;
import javaAP.util.Interface;

/**
 * Web contains useful functions for when dealing with
 * web pages.
 */
public class Web extends Obj
{
    /**
     * Downloads a text/html web page to a specified file.
     * 
     * @param url_resource  The String representing the URL 
     *                      location to read from.
     *                      
     * @param output        The String representing the File
     *                      location to write to.
     *                      
     * @return  The File that the URL data was written to.
     */
    public static File download(String url_resource, String output) { return download(url_resource, output, false); }
    
    /**
     * Downloads a web page to a specified file, can be either
     * text/html or data like mp3.
     * 
     * @param url_resource  The String representing the URL 
     *                      location to read from.
     *                      
     * @param output        The String representing the File
     *                      location to write to.
     * 
     * @param binaryfile    Whether or not the URL File you
     *                      are reading from should be treated
     *                      as binary data or not.
     *                      
     * @return  The File that the URL data was written to.
     */
    public static File download(String url_resource, String output, boolean binaryfile) { return download(url_resource, new File(output), binaryfile); }
    
    /**
     * Downloads a text/html web page to a specified file.
     * 
     * @param url_resource  The String representing the URL 
     *                      location to read from.
     *                      
     * @param output        The File location to write to.
     *                      
     * @return  The File that the URL data was written to.
     */
    public static File download(String url_resource, File output)   { return download(url_resource, output, false); }
    
    /**
     * Downloads a web page to a specified file, can be either
     * text/html or data like mp3. Creates a temporary File to
     * store the data.
     * 
     * @param url_resource  The String representing the URL 
     *                      location to read from.
     * 
     * @param binaryfile    Whether or not the URL File you
     *                      are reading from should be treated
     *                      as binary data or not.
     *                      
     * @return  The File that the URL data was written to.
     */
    public static File download(String url_resource, boolean binaryfile)
    {
        try
        {
            String[] filename = url_resource.substring(url_resource.lastIndexOf("/")).split(".");
            
            File output;
            
            if(filename.length == 1)
            {
                output = File.createTempFile(filename[0], null);
            }
            else
            {
                output = File.createTempFile(filename[0], filename[1]);
            }
        
            return download(url_resource, output, binaryfile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Downloads a web page to a specified file, can be either
     * text/html or data like mp3.
     * 
     * @param url_resource  The String representing the URL 
     *                      location to read from.
     *                      
     * @param output        The File location to write to.
     * 
     * @param binaryfile    Whether or not the URL File you
     *                      are reading from should be treated
     *                      as binary data or not.
     *                      
     * @return  The File that the URL data was written to.
     */
    public static File download(String url_resource, File output, boolean binaryfile)
    {
        try
        {
            
            Interface.Output out = Interface.Output.instance(output);
            Interface.Input in  = Interface.Input.instance(new URL(url_resource));
        
            out.set("autoflushing", false);
            
            int blocksize = 1024;
        
            while(! in.finished())
            {
                out.write(in.read(blocksize));
            }
            
            out.close();
            in.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return output;
    }
    
    /**
     * Checks whether a given URL represents an existing
     * web page or not.
     * 
     * @param url   A String representing a URL to check
     *              for data.
     *              
     * @return      true, if the function could successfully
     *              connect to an HTTP server at the given
     *              url.
     *              false, otherwise.
     */
    public static boolean exists(String url)
    {
        try
        {
            HttpURLConnection.setFollowRedirects(false);
            
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("HEAD");
            
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e)
        {
            return false;
        }
    }
}