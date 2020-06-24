package me.skiincraft.discord.core.objects;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import me.skiincraft.discord.core.exception.BotConfigNotFoundException;

public class PluginData {
	
	private String botname;
	private String bot_main;
	private String filename;
	
	private Class<?> mainclass;
	
	public PluginData(String botname, String bot_main, String filename) throws BotConfigNotFoundException {
		this.botname = botname;
		this.bot_main = bot_main;
		this.filename = filename;
		
		try {
			mainclass = Class.forName(bot_main, true,
					new URLClassLoader(new URL[] {new URL("file:bots\\" + filename.replace(".jar", "") + ".jar")}));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new BotConfigNotFoundException("A classe main está invalida.");
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
