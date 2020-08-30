package me.skiincraft.discord.core.textfont;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;

public class CustomFont {
	
	public CustomFont() {
		this.plugin = OusuCore.getPluginManager().getPlugin();
	}
	
	private Plugin plugin;
	
	public Font getFont(String fontname, int style, float size) {
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File(plugin.getFontPath().getAbsolutePath() + "/" +fontname + ".ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);

			f = f.deriveFont(style);
			f = f.deriveFont(size);

			return f;
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

}
