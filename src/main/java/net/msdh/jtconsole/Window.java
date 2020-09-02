package net.msdh.jtconsole;

import java.awt.*;
import java.util.Arrays;


public final class Window {

    public static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
	public static final Color DEFAULT_BACKGROUND = Color.BLACK;
	public static final Font DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 18);

	private int capacity;

	public int rows;
	public int columns;

	public Color[] background;
	public Color[] foreground;
	public Font[] font;
	public char[] text;

    public int cursorX;
	public int cursorY;


	Window(int columns, int rows) {

      this.capacity = columns * rows;

      this.text = new char[capacity];
	  this.background = new Color[capacity];
	  this.foreground = new Color[capacity];
	  this.font = new Font[capacity];

      Arrays.fill(background, DEFAULT_BACKGROUND);
      Arrays.fill(foreground, DEFAULT_FOREGROUND);
      Arrays.fill(font, DEFAULT_FONT);
      Arrays.fill(text, ' ');

	  this.rows = rows;
	  this.columns = columns;

      this.cursorX = 0;
	  this.cursorY = 0;

	}

	@SuppressWarnings("unused")
	private void resize(int newCapacity) {
		if(capacity >= newCapacity){
		  return;
        }

		char[] newText = new char[newCapacity];
		Color[] newBackground = new Color[newCapacity];
		Color[] newForeground = new Color[newCapacity];
		Font[] newFont = new Font[newCapacity];

		int size = rows * columns;

		if(size > 0){
		  System.arraycopy(text, 0, newText, 0, size);
		  System.arraycopy(foreground, 0, newForeground, 0, size);
		  System.arraycopy(background, 0, newBackground, 0, size);
		  System.arraycopy(font, 0, newFont, 0, size);
		}

		text = newText;
		foreground = newForeground;
		background = newBackground;
		font = newFont;
		capacity = newCapacity;
	}


	public void setDataAt(char c, Color fg, Color bg,Font f){

	  int pos = cursorX + cursorY * columns;
	  text[pos] = c;
	  foreground[pos] = fg;
	  background[pos] = bg;
	  font[pos] = f;
      moveCursor(c);

      if(cursorY==rows){
        scroll(1);
      }
	}

	public void setCharAt(char c, Color fg, Color bg,Font f){
	  int pos = cursorX + cursorY * columns;
	  text[pos] = c;
	  foreground[pos] = fg;
	  background[pos] = bg;
	  font[pos] = f;
	}

    private void moveCursor(char c) {
	  switch (c) {
	  case '\n':
		cursorY++;
		cursorX = 0;
    	break;
	  default:
		cursorX++;
		if(cursorX >= columns) {
		  cursorX = 0;
		  cursorY++;
		}
		break;
	  }
	}

    public void moveCursorRight() {
	  cursorX++;
	  if(cursorX >= columns) {
	    cursorX = 0;
	    cursorY++;
	  }
    }

    public void moveCursorLeft() {
	  cursorX--;

	  if(cursorX < 0) {
        if(cursorY>0){
          cursorX = (columns-1);
	      cursorY--;
        }
        else{
         cursorX = 0;
        }
	  }
	}

	public char getCharAt(int column, int row) {
		int offset = column + row * columns;
		return text[offset];
	}

	public Color getForegroundAt(int column, int row) {
		int offset = column + row * columns;
		return foreground[offset];
	}

	public Color getBackgroundAt(int column, int row) {
		int offset = column + row * columns;
		return background[offset];
	}

	public Font getFontAt(int column, int row) {
		int offset = column + row * columns;
		return font[offset];
	}

	public void fillArea(char c, Color fg, Color bg, Font f, int column,int row, int width, int height) {

		for (int q = Math.max(0, row); q < Math.min(row + height, rows); q++) {
			for (int p = Math.max(0, column); p < Math.min(column + width, columns); p++) {
				int offset = p + q * columns;
				text[offset] = c;
				foreground[offset] = fg;
				background[offset] = bg;
				font[offset] = f;
			}
		}

	}

    public void scroll(int i){

      char[] temp = new char[capacity-columns];          //создаем временный массив-буфер размером на одну строку меньше для сдвига экрана
      Color[] tempBG = new Color[capacity-columns];
      Color[] tempFG = new Color[capacity-columns];
      Font[] tempF = new Font[capacity-columns];


      if(i==1){    //scroll up


          System.arraycopy(text, columns, temp, 0, capacity - columns);  //копируем текущий экран без верхней строки во временный массив
          System.arraycopy(temp, 0, text, 0, temp.length);             //копируем временный массив начиная от верхка основного массива

          System.arraycopy(background, columns, tempBG, 0, capacity - columns);  //копируем текущий экран без верхней строки во временный массив
          System.arraycopy(tempBG, 0, background, 0, tempBG.length);             //копируем временный массив начиная от верхка основного массива

          System.arraycopy(foreground, columns, tempFG, 0, capacity - columns);  //копируем текущий экран без верхней строки во временный массив
          System.arraycopy(tempFG, 0, foreground, 0, tempFG.length);             //копируем временный массив начиная от верхка основного массива

          System.arraycopy(font, columns, tempF, 0, capacity - columns);  //копируем текущий экран без верхней строки во временный массив
          System.arraycopy(tempF, 0, font, 0, tempF.length);             //копируем временный массив начиная от верхка основного массива

          clearLine(rows-1);

          cursorY--;
      }
      else{ //scroll down
        //scrollCount++;

        System.arraycopy(text, 0, temp, 0, capacity - columns);  //копируем текущий экран без нижней строки во временный массив
        System.arraycopy(temp, 0, text, columns, temp.length);             //копируем временный массив начиная от второй строки основного массива

        System.arraycopy(background, 0, tempBG, 0, capacity - columns);  //копируем текущий экран без нижней строки во временный массив
        System.arraycopy(tempBG, 0, background, columns, tempBG.length);             //копируем временный массив начиная от второй строки основного массива

        System.arraycopy(foreground, 0, tempFG, 0, capacity - columns);  //копируем текущий экран без нижней строки во временный массив
        System.arraycopy(tempFG, 0, foreground, columns, tempFG.length);             //копируем временный массив начиная от второй строки основного массива

        System.arraycopy(font, 0, tempF, 0, capacity - columns);  //копируем текущий экран без нижней строки во временный массив
        System.arraycopy(tempF, 0, font, columns, tempF.length);             //копируем временный массив начиная от второй строки основного массива


        cursorY++;
      }
    }

    public void clearLine(int y){

       char[] zeroLine  = new char[columns];                //создаем пустую строку
       Color[] zeroBG = new Color[columns];
       Color[] zeroFG = new Color[columns];
       Font[] zeroF = new Font[columns];

       Arrays.fill(zeroLine,' ');                          //инициализируем пустую строку
       Arrays.fill(zeroBG,DEFAULT_BACKGROUND);
       Arrays.fill(zeroFG,DEFAULT_FOREGROUND);
       Arrays.fill(zeroF,DEFAULT_FONT);


       System.arraycopy(zeroLine, 0, text, y * columns, columns);  //забиваем самую нижнюю строку
       System.arraycopy(zeroBG, 0, background, y * columns, columns);
       System.arraycopy(zeroFG, 0, foreground, y * columns, columns);
       System.arraycopy(zeroF, 0, font, y * columns, columns);

    }


    public void setLine(int x, int y, ConsoleLine cl){
       System.arraycopy(cl.line, 0, text, x + y * columns, cl.line.length);  //заменяем верхнюю строку строкой из буфера
       System.arraycopy(cl.background, 0, background, x + y * columns, cl.background.length);
       System.arraycopy(cl.foreground, 0, foreground, x + y * columns, cl.foreground.length);
       System.arraycopy(cl.font, 0, font, x + y * columns, cl.font.length);
    }

    public void backSpaceChar(){
        moveCursorLeft();
        setCharAt(' ',DEFAULT_FOREGROUND,DEFAULT_BACKGROUND,DEFAULT_FONT);
    }
}