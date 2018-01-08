package javaAP.net.html;

import javaAP.lang.Obj;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Enumeration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.Charset;

import java.net.URL;

import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.MutableAttributeSet;

public class HTMLParser extends Obj
{
    protected ArrayDeque<String>  tagstack;
    protected ArrayDeque<HashMap> infostack;
    private HTMLEditorKit.ParserCallback callback_methods;
    private ParserDelegator pd;
    
    public HTMLParser()
    {
        tagstack = new ArrayDeque<String>();
        infostack = new ArrayDeque<HashMap>();
        pd = new ParserDelegator();
        this.create_methods();
    }
    
    public Object parse(String url_to_parse)
    {       
        return this.parse(url_to_parse, null);
    }
    
    public Object parse(URL to_parse)
    {
        return this.parse(to_parse, null);
    }
    
    public Object parse(File to_parse)
    {
        return this.parse(to_parse, null);
    }
    
    public Object parse(String url_to_parse, String encoding)
    {
        try                 { this.parse(new URL(url_to_parse), encoding); }
        catch(Exception e)  { e.printStackTrace();}
        
        return null;
    }
    
    public Object parse(URL to_parse, String encoding)
    {
        this.reset();
        this.parsehelper(to_parse, encoding);
        return null;
    }
    
    public Object parse(File file_to_parse, String encoding)
    {
        try                 { this.parse(file_to_parse.toURI().toURL(), encoding); }
        catch(Exception e)  { e.printStackTrace(); }
        
        return null;
    }
    
    private void reset()
    {
        tagstack.clear();
        infostack.clear();
    }
    
    private void parsehelper(URL toparse, String encoding)
    {
        BufferedReader document = null;
        
        try
        {
            call("this_start");
            
            if(encoding != null && Charset.isSupported(encoding))
                { document = new BufferedReader(new InputStreamReader(toparse.openStream(), Charset.forName(encoding))); }
            else
                { document = new BufferedReader(new InputStreamReader(toparse.openStream())); }
            
            pd.parse(document, callback_methods, true);   
            
            call("this_end");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void create_methods()
    {
        callback_methods = new HTMLEditorKit.ParserCallback()
            {
                public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
                {
                    String name = t.toString();
                    HashMap<String, String> attrs = new HashMap<String, String>();
                    
                    for (Enumeration e = a.getAttributeNames(); e.hasMoreElements() ;)
                    {
                        Object elem = e.nextElement();
                        String key = new String(elem.toString().getBytes(Charset.defaultCharset()));
                        String value = new String(a.getAttribute(elem).toString().getBytes(Charset.defaultCharset()));
                        attrs.put(key, value);
                    }
                    
                    tagstack.addLast(name);
                    
                    if(call(name + "_start", infostack, attrs) == VOID)
                    {
                        call("tag_start", name, infostack, attrs);
                    }
                    
                    infostack.addLast(attrs);
                }
                
                public void handleEndTag(HTML.Tag t, int pos)
                {
                    String name = t.toString();
                    
                    if(call(name + "_end", infostack) == VOID)
                    {
                        call("tag_end", name, infostack);
                    }
                    
                    tagstack.removeLast();
                    infostack.removeLast();
                }
                
                public void handleText(char[] data, int pos)
                {
                    String text = new String(new String(data).getBytes(Charset.defaultCharset()));
                    String currenttag = (String) tagstack.peekLast();
                    
                    if(call(currenttag + "_content", infostack, text) == VOID)
                    {
                        call("tag_content", currenttag, infostack, text);
                    }
                }
                
                public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
                {
                    String name = t.toString();
                    HashMap<String, String> attrs = new HashMap<String, String>();
                    
                    for (Enumeration e = a.getAttributeNames(); e.hasMoreElements() ;)
                    {
                        Object elem = e.nextElement();
                        String key = new String(elem.toString().getBytes(Charset.defaultCharset()));
                        String value = new String(a.getAttribute(elem).toString().getBytes(Charset.defaultCharset()));
                        attrs.put(key, value);
                    }
                    
                    if(call(name + "_start", attrs) == VOID)
                    {
                        call("tag_start", name, attrs);
                    }
                }
            };
    }
}