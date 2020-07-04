package me.skiincraft.discord.core.utils;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TextUtils {
	
	 public static List<String> wrap(String txt, FontMetrics fm, int maxWidth){
		    StringTokenizer st =  new  StringTokenizer(txt)  ;

		    List<String> list = new ArrayList<String>();
		    String line = "";
		    String lineBeforeAppend = "";
		    while (st.hasMoreTokens()){
		       String seg = st.nextToken();
		       lineBeforeAppend = line;
		       line += seg + " ";
		       int width = fm.stringWidth(line);
		       if(width  < maxWidth){
		           continue;
		       }else { //new Line.
		           list.add(lineBeforeAppend);
		           line = seg + " ";
		       }
		    }
		    //the remaining part.
		    if(line.length() > 0){
		        list.add(line);
		    }
		    return list;
		}

}
