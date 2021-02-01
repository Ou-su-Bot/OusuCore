package me.skiincraft.ousucore.common;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.skiincraft.ousucore.OusuCore;

public class CustomFont {
	
	public static Font getFont(String fontName, int style, float size) {
		try {
			List<File> files = getFontsFiles().stream().filter(file -> file.getName().substring(0, file.getName().length()-4)
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


	public static List<File> getFontsFiles() throws IOException {
		List<Path> files = new ArrayList<>();
		Files.newDirectoryStream(OusuCore.getFontPath()).forEach(files::add);
		return files.stream().map(Path::toFile).filter(file -> file.getName().toLowerCase().endsWith(".ttf"))
				.collect(Collectors.toList());
	}

}
