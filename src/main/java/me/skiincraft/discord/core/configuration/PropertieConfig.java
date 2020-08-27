package me.skiincraft.discord.core.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropertieConfig {

	private Properties properties;
	
	@SuppressWarnings("deprecation")
	private static void writeConfiguration(File file) throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		prop.setProperty("interface", "true");
		prop.setProperty("auto-restart", "true");
		prop.setProperty("restart-time", "60");
		OutputStream out = new FileOutputStream(file);
		prop.save(out, "OusuBot - Configuration");
	}
	
	public PropertieConfig() {
		File file = new File("config.properties");
		properties = new Properties();
		if (!file.exists()) {
			try {
				file.createNewFile();
				writeConfiguration(file);
				properties.load(new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			properties.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getString(String string) {
		return properties.getProperty(string);
	}
}
