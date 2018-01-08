package javaAP.net;


import java.net.Socket;
import java.net.ServerSocket;

import java.util.Iterator;
import java.util.ArrayDeque;

import javaAP.lang.Obj;
import javaAP.lang.Routine;
import javaAP.util.Interface;


public class Server extends Obj implements Iterable<Interface>
{
	private ArrayDeque<Interface> connections;
	private ArrayDeque<Routine> threads;
	private ServerSocket me = null;
	private Routine mainloop = null;
	public int port;
	
	public Server(int portin)
	{
	    port = portin;
	    
	    connections = new ArrayDeque<Interface>();
	    threads = new ArrayDeque<Routine>();
	    mainloop = new Routine(this);
	    
	    try 				{ me = new ServerSocket(port); }
	    catch (Exception e) {}
	}
	
	public void start()
	{
	    mainloop.loop("getconnection");
	}
	
	public void stop()
	{
	    mainloop.stop();
	    
	    for(Routine r : threads)		{ r.stop(); }
	    for(Interface io : connections) { io.close(); }
	    
	    try 				{ me.close();}
	    catch(Exception e)  {}
	}
	
	public void getconnection()
	{
	    try
	    {
	        Socket incoming = me.accept();
	        	        
	        Interface io = new Interface(incoming.getInputStream(), incoming.getOutputStream());
	        	io.set("textstream", true);
	        	
	        Routine temp = new Routine(this);
	        
	        connections.add(io);
	        threads.add(temp);
	        
	        temp.loop("getcommand", io);
	    }
	    catch (Exception e)
	    {
	    }
	}
	
	public final void poke(Interface in, String message)
	{
	}
	
	public void getcommand(Interface io)
	{
	    String command = io.readline();
	    
	    String[] commands = command.split(" ", 2);
	    String temp = "";
	    
	    if (commands.length > 1)
	    {
	        temp = commands[1];
	    }
	    
	    this.call(commands[0], io, temp);
	}
	
	public Iterator<Interface> iterator() { return connections.iterator(); }	        
}