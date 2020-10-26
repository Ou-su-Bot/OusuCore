package me.skiincraft.discord.core.textfont;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;

public class CustomFont {
	
	public CustomFont() {
		this.plugin = OusuCore.getPluginManager().getPlugin();
	}
	
	private Plugin plugin;
	
	public Font getFont(String fontName, int style, float size) {
		try {
			List<File> files = getFonts().stream().filter(file -> file.getName().substring(0, file.getName().length()-4)
					.equalsIgnoreCase(fontName))
					.collect(Collectors.toList());

			if (files.size() == 0){
				throw new IOException("Font '" + fontName.concat(".ttf") +"' not found");
			}

			Font f = Font.createFont(Font.TRUETYPE_FONT, files.get(0));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);

			f = f.deriveFont(style);
			f = f.deriveFont(size);

			return f;
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return new Font("arial", Font.PLAIN, 12);
	}

	public List<File> getFonts() throws IOException {
		List<File> files = new ArrayList<>();
		Files.newDirectoryStream(Paths.get(plugin.getFontPath().getAbsolutePath() + "/"))
				.forEach(element -> {
					if (element.toFile().getName().toLowerCase().endsWith(".ttf")){
						files.add(element.toFile());
					}
				});
		return files;
	}

}
