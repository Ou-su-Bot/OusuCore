package me.skiincraft.discord.core.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;

import me.skiincraft.discord.core.utils.ColorT.CMap;
import me.skiincraft.discord.core.utils.ColorT.DenormalizedVBox;
import me.skiincraft.discord.core.view.OusuViewer;

public class ImageUtils {
	
	public static BufferedImage getDiscordAssets(String link) {
		try {
			URL url = null;
			try {url = new URL(link);}
			catch (MalformedURLException e) {
				return ImageIO.read(OusuViewer.class.getResourceAsStream("no_bot_image.png"));
			}
			
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			return ImageIO.read(con.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream toInputStream(File imagefile) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(imagefile), "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(BufferedImage image) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(URL imageurl) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(imageurl), "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(String url) throws IOException {
		return toInputStream(new URL(url));
	}
	
	
	

	public static Color getPredominatColor(BufferedImage image) {
		try {
			BufferedImage img = new BufferedImage(40, 40, 2);
			Graphics2D graph = img.createGraphics();
			graph.drawImage(image, 0, 0, 40, 40, null);
			graph.dispose();

			CMap result;

			result = ColorT.computeMap(img, 3);

			Iterator<DenormalizedVBox> boxes = result.getBoxes().iterator();

			int[] cor = boxes.next().getColor();
			return new Color(cor[0], cor[1], cor[2]);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean existsImage(URL url) {
		try {
			BufferedImage b = ImageIO.read(url);
			b.createGraphics();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean existsImage(String url) {
		try {
			BufferedImage b = ImageIO.read(new URL(url));
			b.createGraphics();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
