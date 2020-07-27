package me.skiincraft.discord.core.view.images;

import java.awt.Dimension;
import java.io.InputStream;

import javafx.scene.image.Image;

public class OusuImages {
	
	public InputStream getImageInputStream(String string) {
		return getClass().getResourceAsStream(string);
	}
	
	public Image getImage(String string) {
		return new Image(getClass().getResourceAsStream(string));
	}
	
	public Image getImage(String string, Dimension size) {
		return new Image(getImageInputStream(string), size.getWidth(), size.getHeight(), false, true);
	}

}
