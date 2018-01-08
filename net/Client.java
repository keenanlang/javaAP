package javaAP.net;


import java.io.*;
import java.net.Socket;

import javaAP.lang.Obj;
import javaAP.lang.Routine;
import javaAP.util.Interface;


public class Client extends Interface
{
	public int port;
	public String server;
	private Socket me = null;
	private Routine checkconnection = null;
	
	public Client(String address, int portin)
	{
	    port = portin;
	    server = address;
	    
	    try
	    {
	        me = new Socket(server, port);
	        in = new BufferedReader(new InputStreamReader(me.getInputStream()));
	        out = new BufferedWriter(new OutputStreamWriter(me.getOutputStream()));
	    }
	    catch (Exception e)
	    	{}

	  	
        checkconnection = new Routine(this);
        checkconnection.loop("isconnected");
	}	        
	
	public void isconnected()
	{
	    try { out.write("poke", 0,4); out.newLine(); out.flush(); }
	    catch (Exception e)
		{
	        checkconnection.stop();
	        this.close();
	    	this.disconnected();
	    }
	}
	
	public void disconnected() {}    
}