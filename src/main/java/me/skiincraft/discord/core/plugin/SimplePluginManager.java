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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.exception.BotConfigNotFoundException;
import me.skiincraft.discord.core.exception.IncorrectListenerException;
import me.skiincraft.discord.core.objects.DiscordInfo;
import me.skiincraft.discord.core.objects.PluginConfig;
import me.skiincraft.discord.core.objects.PluginData;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SimplePluginManager implements PluginManager {
	
	private static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	
	@Override
	public ArrayList<Plugin> getPlugins() {
		return plugins;
	}

	@Override
	public Plugin getPlugin(String plugin) {
		for (Plugin plug : getPlugins()) {
			if (plug.getDiscordInfo().getBotname().equalsIgnoreCase(plugin)) {
				return plug;
			}
		}
		return null;
	}

	@Override
	public boolean hasPlugin(String plugin) {
		for (Plugin plug : getPlugins()) {
			if (plug.getDiscordInfo().getBotname().equalsIgnoreCase(plugin)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public PluginConfig getPluginConfig(File file) {
		try {
			return getPluginConfig(file.getAbsolutePath().replace(file.getName(), ""), file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public PluginConfig getPluginConfig(String path, String jarfile) throws FileNotFoundException {
		String inputFile = "jar:file:" + path + jarfile.replace(".jar", "") + ".jar!/bot.json";
		try {
			URL inputUrl = new URL(inputFile);
			JarURLConnection conn = (JarURLConnection) inputUrl.openConnection();
			InputStream stream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			JsonElement ele = new JsonParser().parse(reader);
			JsonArray array = ele.getAsJsonArray();
			JsonObject object = array.get(0).getAsJsonObject();
			JsonObject objectdiscord = object.get("DiscordInfo").getAsJsonObject();
			
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

	@Override
	public PluginConfig getPluginConfig(String jarfile) {
		try {
			return getPluginConfig("bots/", jarfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PluginData getPluginData(String botname, String bot_main, String filename) throws BotConfigNotFoundException {
		return new PluginData(botname, bot_main, filename);
	}

	@Override
	public Plugin[] getPlugins(File path) {
		File[] files = new File(path.getName()).listFiles();
		List<File> filelist = Arrays.asList(files);
		
		List<String> pluginsname = new ArrayList<>();
		List<String> pluginpath = new ArrayList<>();
		filelist.forEach(f ->{
			if (f.getName().contains(".jar")) {
				pluginsname.add(f.getName());
				pluginpath.add(f.getAbsolutePath().replace(f.getName(), ""));
			}
		});
		
		if (pluginsname.size() == 0) {
			return new Plugin[0];
		}
		List<Plugin> plugin = new ArrayList<Plugin>();
		for (int i = 0; i < pluginsname.size(); i++) {
			try {
			PluginConfig config = getPluginConfig(pluginpath.get(i), pluginsname.get(i));
			PluginData data;
			data  = new PluginData(config.getBotname(), config.getBot_main(), pluginsname.get(i).replace(".jar", "") + ".jar");
			plugin.add(new Plugin(data.getMainclass().asSubclass(OusuPlugin.class), config.getDiscordInfo(), this));
			} catch (Exception e) {
				//e.printStackTrace();
				continue;
			}
		}
		Plugin[] plugins = new Plugin[plugin.size()];
		plugin.toArray(plugins);
		
		return plugins;
	}

	@Override
	public void registerCommands(Command command, OusuPlugin plugin) {
		plugin.getPlugin().addCommand(command);
	}

	@Override
	public void registerEvents(ListenerAdapter listener, OusuPlugin plugin) {
		if (plugin.getPlugin().isRunning() == true) {
			throw new IncorrectListenerException("Evento registrado após o inicio do bot.");
		}
		plugin.getPlugin().addListener(listener);
	}

	@Override
	public void disablePlugin(Plugin plugin) {
		plugin.stopPlugin();
	}

	@Override
	public void enablePlugin(Plugin plugin) {
		try {
			plugin.startPlugin();
		} catch (InstantiationException | IllegalAccessException | BotConfigNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Plugin getPluginByBotId(long id) {
		for (Plugin plugin : getPlugins()) {
			if (plugin.getDiscordInfo().getBotId() == id) {
				return plugin;	
			}
		}
		return null;
	}

	@Override
	public Plugin getPluginByBotId(String id) {
		return getPluginByBotId(Long.valueOf(id));
	}

}
