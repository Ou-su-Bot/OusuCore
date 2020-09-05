package net.msdh.jtconsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import static java.awt.event.KeyEvent.*;

@SuppressWarnings("serial")
public class JTConsole extends JPanel implements KeyListener, MouseListener {

	private static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
	private static final Color DEFAULT_BACKGROUND = Color.BLACK;
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 15);

	private Window dataScreen;

	private int fontWidth;
	private int fontHeight;
	private int fontYOffset;

	private int indexCommand;

	private boolean cursorVisible;
	private boolean endScrol;
	private int scrollPoz;
	private boolean incomplitComand;

	private Font mainFont;
	private Font currentFont;
	private Color currentForeground;
	private Color currentBackground;

	private String commandLine;
	private ConsoleAction action;

	private Vector<String> commands;
	private Vector<ConsoleLine> lines;

	private int commandsSize;
	private int linesSize;
	private boolean readOnly;

	public int height;
	public int width;

	public int rows;
	public int columns;

	public JTConsole(int columns, int rows) {

		this.rows = rows;
		this.columns = columns;

		dataScreen = new Window(columns, rows);
		setPreferredSize(new Dimension(width, height));

		this.mainFont = null;
		this.currentFont = null;

		setMainFont(DEFAULT_FONT);
		setFont(mainFont);

		height = rows * fontHeight;
		width = columns * fontWidth;

		this.addMouseListener(this);
		this.addKeyListener(this);
		this.commandLine = "";
		this.scrollPoz = 0;
		this.indexCommand = 0;
		this.currentForeground = DEFAULT_FOREGROUND;
		this.currentBackground = DEFAULT_BACKGROUND;
		this.endScrol = true;
		this.incomplitComand = false;

		this.cursorVisible = false;

		this.commands = new Vector<>();
		this.lines = new Vector<>();
		this.commandsSize = 20;
		this.linesSize = 200;
		this.readOnly = false;
	}

	public void setMainFont(Font font) {

		mainFont = font;
		FontRenderContext fontRenderContext = new FontRenderContext(mainFont.getTransform(), false, false);
		Rectangle2D charBounds = mainFont.getStringBounds("X", fontRenderContext);
		fontWidth = (int) charBounds.getWidth();
		fontHeight = (int) charBounds.getHeight();
		fontYOffset = -(int) charBounds.getMinY();

		setPreferredSize(new Dimension(dataScreen.columns * fontWidth, dataScreen.rows * fontHeight));
		repaint();

	}

	public void setFont(Font f) {
		currentFont = f;
	}

	public int getLinesSize() {
		// return linesSize;
		return lines.size();
	}

	public void setCursorVisible(boolean visible) {
		cursorVisible = visible;
	}

	public int getRows() {
		return dataScreen.rows;
	}

	public int getColumns() {
		return dataScreen.columns;
	}

	public int getFontWidth() {
		return fontWidth;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void repaintArea(int column, int row, int width, int height) {
		int fw = getFontWidth();
		int fh = getFontHeight();
		repaint(column * fw, row * fh, width * fw, height * fh);
	}

//	public void resize(int columns, int rows) {
//		//throw new UnsupportedOperationException();
//	}

	public void clear() {
		clearArea(0, 0, dataScreen.columns, dataScreen.rows);
	}

	public void resetCursor() {
		dataScreen.cursorX = 0;
		dataScreen.cursorY = 0;
	}

	public void clearScreen() {
		clear();
		resetCursor();
	}

	private void clearArea(int column, int row, int width, int height) {
		dataScreen.fillArea(' ', currentForeground, currentBackground, currentFont, column, row, width, height);
		repaintArea(0, 0, width, height);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		Rectangle r = g.getClipBounds();

		// calculate x and y range to redraw
		int x1 = (int) (r.getMinX() / fontWidth);
		int x2 = (int) (r.getMaxX() / fontWidth) + 1;
		int y1 = (int) (r.getMinY() / fontWidth);
		int y2 = (int) (r.getMaxY() / fontWidth) + 1;

		int curX = getCursorX();
		int curY = getCursorY();

		for (int j = Math.max(0, y1); j < Math.min(y2, dataScreen.rows); j++) {
			int offset = j * dataScreen.columns;
			int start = Math.max(x1, 0);
			int end = Math.min(x2, dataScreen.columns);

			while (start < end) {
				Color nfg = dataScreen.foreground[offset + start];
				Color nbg = dataScreen.background[offset + start];
				Font nf = dataScreen.font[offset + start];

				int i = start + 1;

				if ((j == curY) && (start == curX)) {
					// if (cursorVisible && cursorBlinkOn && cursorInverted) {
					if (cursorVisible) {
						// swap foreground and background colours
						Color t = nfg;
						nfg = nbg;
						nbg = t;
					}
				} else {
					// detect run
					while ((i < end) && (!((j == curY) && (i == curX))) && (nfg == dataScreen.foreground[offset + i])
							&& (nbg == dataScreen.background[offset + i]) && (nf == dataScreen.font[offset + i])) {
						i++;
					}
				}

//                while ((i < end)&&(nfg == dataScreen.foreground[offset + i])&&(nbg == dataScreen.background[offset + i])&&(nf == dataScreen.font[offset + i])){
//				  i++;
//                }

				// set font
				g.setFont(nf);

				// draw background
				g.setBackground(nbg);
				g.clearRect(fontWidth * start, j * fontHeight, fontWidth * (i - start), fontHeight);

				// draw chars up to this point
				g.setColor(nfg);
				for (int k = start; k < i; k++) {
					g.drawChars(dataScreen.text, offset + k, 1, k * fontWidth, j * fontHeight + fontYOffset);
				}
				start = i;
			}
		}
	}

	public void setCursorPos(int column, int row) {
		if ((column < 0) || (column >= dataScreen.columns))
			throw new Error("Invalid X cursor position: " + column);
		if ((row < 0) || (row >= dataScreen.rows))
			throw new Error("Invalid Y cursor position: " + row);

		dataScreen.cursorX = column;
		dataScreen.cursorY = row;
	}

	public int getCursorX() {
		return dataScreen.cursorX;
	}

	public int getCursorY() {
		return dataScreen.cursorY;
	}

	public void setForeground(Color c) {
		currentForeground = c;
	}

	public void setBackground(Color c) {
		currentBackground = c;
	}

	public Color getForeground() {
		return currentForeground;
	}

	public Color getBackground() {
		return currentBackground;
	}

	public char getCharAt(int column, int row) {
		return dataScreen.getCharAt(column, row);
	}

	public Color getForegroundAt(int column, int row) {
		return dataScreen.getForegroundAt(column, row);
	}

	public Color getBackgroundAt(int column, int row) {
		return dataScreen.getBackgroundAt(column, row);
	}

	public Font getFontAt(int column, int row) {
		return dataScreen.getFontAt(column, row);
	}

	public static int stacktraces = 0;

	public void captureStdOut() {
		PrintStream printStream = new PrintStream(System.out) {
			public void println(String x) {
				writeln(x);
			}
		};
		PrintStream printStream2 = new PrintStream(System.err) {
			public void println(String x) {
				writeln(x, Color.RED, DEFAULT_BACKGROUND);
			}
		};

		System.setOut(printStream);
		System.setErr(printStream2);
	}

	private void moveCursorRight() {
		dataScreen.moveCursorRight();
		repaint();
	}

	private void moveCursorLeft() {
		dataScreen.moveCursorLeft();
		repaint();
	}

	public void writeln(String line) {
		write(line + '\n');
		addLine(line + '\n', DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
	}

	public void writeln(String line, Color foreGround, Color backGround) {
		write(line + '\n', foreGround, backGround);
	}

	public void write(String line, Color foreGround, Color backGround) {
		Color foreTemp = currentForeground;
		Color backTemp = currentBackground;
		setForeground(foreGround);
		setBackground(backGround);
		write(line);
		setForeground(foreTemp);
		setBackground(backTemp);
		addLine(line, foreGround, backGround);
	}

	public void write(int x, int y, String line, Color foreGround, Color backGround) {
		Color foreTemp = currentForeground;
		Color backTemp = currentBackground;
		setForeground(foreGround);
		setBackground(backGround);
		write(x, y, line);
		setForeground(foreTemp);
		setBackground(backTemp);
		addLine(line, foreGround, backGround);
	}

	private void write(String string) {
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			write(c);
		}
	}

	private void write(int x, int y, String string) {
		setCursorPos(x, y);
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			write(c);
		}
	}

	public void write(char c) {
		dataScreen.setDataAt(c, currentForeground, currentBackground, currentFont);
		repaint();
	}

	public void backSpaceChar() {
		dataScreen.backSpaceChar();
		repaint();
	}

	public void setFocus() {
		this.requestFocus();
	}

	public void addConsoleListener(ConsoleAction action) {
		this.action = action;
	}

	public void addLine(String line, Color fg, Color bg) {
		if (lines.size() >= linesSize) {
			lines.remove(0);
		}

		Color[] bgLine = new Color[columns];
		Color[] fgLine = new Color[columns];
		Font[] fLine = new Font[columns];

		Arrays.fill(bgLine, bg);
		Arrays.fill(fgLine, fg);
		Arrays.fill(fLine, DEFAULT_FONT);

		Vector<char[]> tLines = parseLine(line);

		for (char[] chs : tLines) {
			/// todo возможно надо дополнить короткие строки до размера терминала пустими
			/// символами

			char[] newMass = new char[columns];
			Arrays.fill(newMass, ' ');
			System.arraycopy(chs, 0, newMass, 0, chs.length);
			ConsoleLine cl = new ConsoleLine(newMass, bgLine, fgLine, fLine);
			lines.add(cl);
		}
	}

	private Vector<char[]> parseLine(String line) {
		Vector<char[]> result = new Vector<>();

		Pattern p = Pattern.compile("\n");
		String[] tokens = p.split(line);

		for (String subLine : tokens) {

			int max = subLine.length() / columns;
			if (max > 0) {
				for (int i = 0; i <= max; i++) {
					if ((subLine.length()) > columns) {
						result.add(subLine.substring(0, columns).toCharArray());
						subLine = subLine.substring(columns, subLine.length());
					} else {
						result.add(subLine.toCharArray());
					}
				}
			} else {
				result.add(subLine.toCharArray());
			}
		}
		return result;
	}

	public void addCommand(String command) {
		if (commands.size() >= commandsSize) {
			commands.remove(0);
		}
		commands.add(command);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int size = lines.size();
		switch (e.getKeyCode()) {
		case VK_SHIFT: {
			break;
		}
		case VK_BACK_SPACE: {
			if (incomplitComand) {
				if (commandLine.length() > 0) {
					commandLine = commandLine.substring(0, commandLine.length() - 1);
					this.backSpaceChar();
				}
			}
			break;
		}
		case VK_DOWN: {
			if (!incomplitComand) {
				if ((0 <= indexCommand) && (indexCommand < commands.size())) {
					int y = getCursorY();
					String tmp = commands.get(indexCommand); // kopiruem iz vektora vo vremennuu stroku

					dataScreen.clearLine(y);
					write(0, y, tmp);
					commandLine = tmp;
					indexCommand++;
				}
			}
			break;
		}
		case VK_UP: {
			if (!incomplitComand) { // esli komanda uzhe nachata vihodim
				if (indexCommand > 0) { // esli konec vectora to vihodim
					int y = getCursorY();
					String tmp = commands.get(indexCommand - 1); // kopiruem iz vektora vo vremennuu stroku
					dataScreen.clearLine(y);
					write(0, y, tmp);
					commandLine = tmp;
					indexCommand--;
				}
			}
			break;
		}
		case VK_PAGE_UP: {
			// System.out.print("PAGEUP");
			if (scrollPoz <= (size - dataScreen.rows)) {
				// System.out.print("scrollPoz: " + scrollPoz);
				endScrol = false;
				dataScreen.scroll(-1);

				int index = size - (dataScreen.rows + scrollPoz);
				ConsoleLine cl = lines.get(index);
				dataScreen.setLine(0, 0, cl);

				repaint();
				scrollPoz++;
			}
			break;
		}
		case VK_PAGE_DOWN: {
			System.out.print("PAGEDOWN");
			if (!endScrol) {
				if (scrollPoz != 1) {
					scrollPoz--;
					dataScreen.scroll(1);

					int index = size - scrollPoz;
					ConsoleLine cl = lines.get(index);
					dataScreen.setLine(0, dataScreen.rows - 1, cl);
					repaint();
					// scrollPoz--;
				} else {
					// dataScreen.clearLine(dataScreen.rows-1);
					dataScreen.scroll(1);
					repaint();
					scrollPoz = 0;
					endScrol = true;
					// waddch(body,'#');
				}
			}
			break;
		}
		case VK_LEFT: {
			this.moveCursorLeft();
			break;
		}
		case VK_RIGHT: {
			this.moveCursorRight();
			break;
		}
		default: {
			if (readOnly)
				return;
			if (e.getKeyChar() == '\n') {
				this.write(e.getKeyChar());
				incomplitComand = false;
				addLine(commandLine + '\n', DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
				addCommand(commandLine);
				indexCommand = commands.size();
				if (action != null) {
					action.onLine(commandLine);
				}
				commandLine = "";
			} else {
				incomplitComand = true;
				commandLine = commandLine + e.getKeyChar();
				this.write(e.getKeyChar());
			}
			break;
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
