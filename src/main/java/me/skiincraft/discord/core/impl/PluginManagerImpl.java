package me.skiincraft.discord.core.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.skiincraft.discord.core.configuration.Command;
import me.skiincraft.discord.core.event.Listener;
import me.skiincraft.discord.core.exception.ConfigurationNotFound;
import me.skiincraft.discord.core.objects.PluginConfig;
import me.skiincraft.discord.core.objects.PluginData;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginInit;
import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PluginManagerImpl implements PluginManager {
	
	private static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private PluginInit pluginInit;
	
	public PluginManagerImpl(PluginInit init) {
		this.pluginInit = init;
	}

	@Override
	public void loadPlugin(Path path) {
		List<File> fileList = new ArrayList<>();
		try {
			Files.newDirectoryStream(path).forEach(file -> {
				if (file.getFileName().toString().contains(".jar")) {
					System.out.println(file.getFileName());
					fileList.add(file.toFile());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> pluginName = fileList.stream().map(file -> file.getName()).collect(Collectors.toList());
		List<String> pluginpath = fileList.stream().map(file -> file.getAbsolutePath().replace(file.getName(), ""))
				.collect(Collectors.toList());
		
		if (pluginName.size() == 0) {
			return;
		}
		
		List<Plugin> plugin = new ArrayList<Plugin>();
		
		for (int i = 0; i < pluginName.size(); i++) {
			try {
				PluginConfig config = pluginInit.getPluginConfig(pluginpath.get(i), pluginName.get(i));
				PluginData 	data = new PluginData(config.getBotName(), config.getBotMain(), fileList.get(0));
				
				OusuPlugin ousuPlugin = data.getMainclass().asSubclass(OusuPlugin.class).newInstance();
				
				plugin.add(new Plugin(ousuPlugin, config.getDiscordInfo(), this));
			} catch (ConfigurationNotFound | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		if (plugin.size() == 0) {
			return;
		}
		
		//Get first plugin loaded
		
		/* Este era o antigo objectivo do OusuCore, carregar multiplos
		 * bots em 1 só aplicação, mas agora isso não ira mais acontecer.
		 * 
		Plugin[] plugins = new Plugin[plugin.size()];
		plugin.toArray(plugins); Old 
		*/
		plugins.add(plugin.get(0));
		return;
	}

	@Override
	public void registerCommands(Command command) {
		getPlugin().addCommand(command);
	}

	@Override
	public void registerEvents(ListenerAdapter listener) {
		getPlugin().getEventManager().registerListener(listener);
	}
	
	public void registerEvents(Listener listener) {
		getPlugin().getEventManager().registerListener(listener);
	}

	@Override
	public void disablePlugin() {
		getPlugin().stopPlugin();
	}

	@Override
	public void enablePlugin() {
		try {
			getPlugin().startPlugin();
		} catch (InstantiationException | IllegalAccessException | ConfigurationNotFound e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void restartPlugin() {
		try {
			getPlugin().restartPlugin();
		} catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException
				| ConfigurationNotFound e) {
			e.printStackTrace();
		}
	}

	public Plugin getPlugin() {
		return plugins.get(0);
	}

}
