package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PluginLoader {
	
	private File file;
	private String path;
	private String filename;
	
	public PluginLoader(File jarfile) {
		this.file = jarfile;
		this.path = jarfile.getAbsolutePath().replace(file.getName(), "");
		this.filename = jarfile.getName();
	}
	
	private JsonArray array;
	
	public Map<String, Object> getPluginConfiguration() {
		try {
			URL url = new URL("jar:file:" + path + filename.replace(".jar", "") + ".jar!/bot.json");
			JarURLConnection connection = (JarURLConnection) url.openConnection();
			connection.setDefaultUseCaches(false);
			connection.setUseCaches(false);
			array = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonArray();
		} catch (Exception e) {
			throw (RuntimeException) e;
		}
		
		if (array == null) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> discord = new HashMap<>();
		JsonObject object = array.get(0).getAsJsonObject();
		
		map.put("botname", object.get("BotName").getAsString());
		map.put("ownerid", object.get("OwnerId").getAsLong());
		map.put("main", object.get("Main").getAsString());
		
		discord.put("defaultprefix", object.get("DiscordInfo").getAsJsonObject().get("DefaultPrefix").getAsString());
		discord.put("token", object.get("DiscordInfo").getAsJsonObject().get("Token").getAsString());
		discord.put("botid", object.get("DiscordInfo").getAsJsonObject().get("BotId").getAsLong());
		discord.put("shards", object.get("DiscordInfo").getAsJsonObject().get("Shards").getAsInt());
		
		map.put("discord", discord);
		return map;
	}
	
	public Class<?> getPluginMain() {
		Map<String, Object> configuration = getPluginConfiguration();
		try {
			Class<?> clazz = Class.forName((String) configuration.get("main"), true, new URLClassLoader(new URL[] {new URL("file:bots/" + filename.replace(".jar", "") + ".jar")}));
			if (clazz.getSuperclass() != OusuPlugin.class) {
				throw new ClassNotFoundException("Esta classe indicada não extende OusuPlugin.");
			}
			return clazz;
		} catch (ClassNotFoundException | MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
