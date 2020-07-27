package me.skiincraft.discord.core.objects;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import me.skiincraft.discord.core.exception.ConfigurationNotFound;

public class PluginData {
	
	private String botName;
	private String botMain;
	private String fileName;
	
	private Class<?> mainClass;
	
	public PluginData(String botname, String bot_main, String filename) throws ConfigurationNotFound {
		this.botName = botname;
		this.botMain = bot_main;
		this.fileName = filename;
		
		try {
			String main = bot_main;
			mainClass = Class.forName(main, true,
					new URLClassLoader(new URL[] {new URL("file:bots\\" + filename.replace(".jar", "") + ".jar")}));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new ConfigurationNotFound("A classe main está invalida. ");
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
	}
	
	public PluginData(String botname, String botMain, File file) throws ConfigurationNotFound {
		this.botName = botname;
		this.botMain = botMain;
		this.fileName = file.getName();
		
		try {
			String main = botMain;
			mainClass = Class.forName(main, true,
					new URLClassLoader(new URL[] {new URL("file:" + file.getAbsoluteFile().toString())}));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new ConfigurationNotFound("A classe main está invalida. ");
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
	}
	
	public String getBotName() {
		return botName;
	}

	public String getBotMain() {
		return botMain;
	}

	public String getFilename() {
		return fileName;
	}

	public Class<?> getMainclass() {
		return mainClass;
	}
	
	

}
