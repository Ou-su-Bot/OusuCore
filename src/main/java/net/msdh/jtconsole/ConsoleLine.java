package net.msdh.jtconsole;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: TkachenkoAA
 * Date: 15.06.17
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLine {
    public int size;
    public char[] line;
    public Color[] background;
	public Color[] foreground;
	public Font[] font;


    public ConsoleLine(char[] line, Color[] background, Color[] foreground, Font[] font) {
      this.line = line;
      this.background = background;
      this.foreground = foreground;
      this.font = font;
    }

    public String toString(){
      return String.valueOf(line);
    }

}
