package javaAP.util;

import java.util.regex.*;

public class RegEx
{
    public static String[] match(String input, String... matches)
    {
        int current_index = 0;
        
        String[] output = new String[matches.length];
        
        for(int i = 0; i < output.length; i += 1)
        {
            Pattern nextmatch = Pattern.compile(matches[i]);
            Matcher finder = nextmatch.matcher(input);
            
            if(finder.find(current_index))
            {
                output[i] = finder.group();
                current_index = finder.end();
            }
            else
            {
                output[i] = "";
            }
        }
        
        return output;
    }
}