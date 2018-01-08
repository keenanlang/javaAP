package javaAP.xml;

import javaAP.lang.Obj;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * The XMLParser class black-boxes lesser needed functionality of
 * the javax SAXParser. Standardizes attributes on HashMap rather
 * than the Attributes class.
 * 
 * All methods are invoked dynamically rather than staticly. When a
 * tag is encountered, the parser tries to invoke a function named
 * <tagname>_start with a Tag as its only parameter. If it doesn't
 * find it the parser will instead invoke a general function tag_start
 * with the same parameters.
 * 
 * The same procedure applies for both content and tags ending, with
 * the invoked functions being <tagname>_content(String content) and
 * <tagname>_end(), respectively. Functions this_start() and this_end()
 * denote the start and end of the document, respectively.
 */
public class XMLParser extends Obj
{
    protected ArrayDeque<Tag> tags;
    private File toparse = null;
    
    private Obj currContext;
    
    public XMLParser()
    {
        currContext = this;
        this.observe(this);
    }
    
    public void setContext(Obj context)
    {
        currContext.ignore(this);
        context.observe(this);
        currContext = context;
    }
    
    public void clearContext()
    {
        setContext(this);
    }
    
    /**
     * Instructs the parser to parse the
     * file.
     * 
     * @param filename  The String describing
     *                  the path to the file
     *                  you wish to parse.
     *                  
     * @return null, classes extending
     *         XMLParser should override this
     *         method to return data from
     *         parsing.
     */
    public Object parse(String filename)
    {
        this.parse(new File(filename));
        return null;
    }
    
    /**
     * Instructs the parser to parse the
     * file.
     * 
     * @param filename  The File you wish to
     *                  parse.
     *                  
     * @return null, classes extending
     *         XMLParser should override this
     *         method to return data from
     *         parsing.
     */
    public Object parse(File xmlfile)
    {
        toparse = xmlfile;
        tags = new ArrayDeque<Tag>();
        this.startParse();
        return null;
    }
    
    private void startParse()
    {
        try
        {
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
        
            sp.parse(toparse, new DefaultHandler()
            {
                public void startDocument()
                {
                    signal("this_start");
                }
            
                public void endDocument()
                {
                    signal("this_end");
                }
            
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                {
                    HashMap<String, String> attrs = new HashMap<String, String>();
                
                    for(int i = 0; i < attributes.getLength(); i += 1)
                    {
                        attrs.put(attributes.getLocalName(i), attributes.getValue(i));
                    }
                
                    Tag temp = new Tag(qName, attrs);
                    
                    if(! signal(qName + "_start", temp))
                    {
                        signal("tag_start", temp);
                    }
                    
                    tags.addLast(temp);
                }
                
                public void endElement(String uri, String localName, String qName)
                {
                    if(! signal(qName + "_end"))
                    {
                        signal("tag_end", tags.peekLast());
                    }
                    
                    tags.removeLast();
                }
                
                public void characters(char[] ch, int start, int length)
                {                   
                    String data = new String(ch, start, length);
                    Tag currenttag = tags.peekLast();
                    
                    if (data.trim().isEmpty())    { return; }
                    
                    if(! signal(currenttag.name + "_content", data))
                    {
                        signal("tag_content", currenttag, data);
                    }
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}