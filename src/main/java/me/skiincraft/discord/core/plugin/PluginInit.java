package me.skiincraft.discord.core.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.discord.core.exception.ConfigurationNotFound;
import me.skiincraft.discord.core.objects.DiscordInfo;
import me.skiincraft.discord.core.objects.PluginConfig;

/*
 * Classe temporaria, até eu arrumar 
 * as minhas ideias, e organizar.
 */

public class PluginInit {
	
	public PluginConfig getPluginConfig(File file) throws ConfigurationNotFound {
		return getPluginConfig(file.getAbsolutePath().replace(file.getName(), ""), file.getName());
	}
	
	
	
	public PluginConfig getPluginConfig(String path, String jarfile) throws ConfigurationNotFound {
		String inputFile = "jar:file:" + path + jarfile.replace(".jar", "") + ".jar!/bot.json";
		try {
			URL inputUrl = new URL(inputFile);
			InputStream stream;
			JarURLConnection conn;
			try {
			 conn = (JarURLConnection) inputUrl.openConnection();
			 stream = conn.getInputStream();
			} catch (FileNotFoundException e) {
					throw new ConfigurationNotFound("Não foi possivel achar o arquivo bot.json em " + jarfile);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			
			JsonElement ele = new JsonParser().parse(reader);
			JsonArray array = ele.getAsJsonArray();
			JsonObject object = array.get(0).getAsJsonObject();
			JsonObject objectdiscord = object.get("DiscordInfo").getAsJsonObject();
			
			stream.close();
			conn.setDefaultUseCaches(false);
			conn.setUseCaches(false);
			
			return new PluginConfig(new DiscordInfo(
					object.get("BotName").getAsString(),
					objectdiscord.get("DefaultPrefix").getAsString(),
					objectdiscord.get("Token").getAsString(),
					objectdiscord.get("BotId").getAsLong(),
					object.get("OwnerId").getAsLong(),
					objectdiscord.get("Shards").getAsInt()),
					object.get("BotName").getAsString(),
					object.get("Main").getAsString(),
					jarfile.replace(".jar", "") + ".jar");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public PluginConfig getPluginConfig(String jarfile) throws ConfigurationNotFound {
			return getPluginConfig("bots/", jarfile);
	}

}
