package me.skiincraft.discord.core.objects;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import me.skiincraft.discord.core.exception.ConfigurationNotFound;

public class PluginData {
	
	private String botname;
	private String bot_main;
	private String filename;
	
	private Class<?> mainclass;
	
	public PluginData(String botname, String bot_main, String filename) throws ConfigurationNotFound {
		this.botname = botname;
		this.bot_main = bot_main;
		this.filename = filename;
		
		try {
			String main = bot_main;
			mainclass = Class.forName(main, true,
					new URLClassLoader(new URL[] {new URL("file:bots\\" + filename.replace(".jar", "") + ".jar")}));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new ConfigurationNotFound("A classe main está invalida. ");
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
	}
	
	public PluginData(String botname, String bot_main, File file) throws ConfigurationNotFound {
		this.botname = botname;
		this.bot_main = bot_main;
		this.filename = file.getName();
		
		try {
			String main = bot_main;
			mainclass = Class.forName(main, true,
					new URLClassLoader(new URL[] {new URL("file:" + file.getAbsoluteFile().toString())}));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new ConfigurationNotFound("A classe main está invalida. ");
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
	}
	
	public String getBotname() {
		return botname;
	}

	public String getBot_main() {
		return bot_main;
	}

	public String getFilename() {
		return filename;
	}

	public Class<?> getMainclass() {
		return mainclass;
	}
	
	

}
