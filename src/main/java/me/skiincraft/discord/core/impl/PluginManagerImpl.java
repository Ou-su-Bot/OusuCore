package me.skiincraft.discord.core.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IteratorUtils;

import me.skiincraft.discord.core.exception.ConfigurationNotFound;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginLoader;
import me.skiincraft.discord.core.plugin.PluginManager;

public class PluginManagerImpl implements PluginManager {
	
	private ArrayList<Plugin> plugin = new ArrayList<Plugin>();
	private boolean started;
	
	public void startPlugin() {
		try {
			Stream<Path> stream = IteratorUtils.toList(Files.newDirectoryStream(Paths.get("bots")).iterator()).stream();
			List<File> list = stream.filter(path -> path.getFileName().toString().contains(".jar"))
					.map(m -> m.toFile())
					.collect(Collectors.toList());
			
			if (list.size() > 1) {
				System.out.println("Foi detectado mais de 1 arquivo jar, na pasta bots.");
			}
			
			List<PluginLoader> filtro = new ArrayList<>();
			for (File file : list) {
				try {
					System.out.println(file);
					PluginLoader pl = new PluginLoader(file);
					pl.getPluginConfiguration();
					filtro.add(pl);
				} catch (RuntimeException exception) {
				}
			}
			if (filtro.size() > 1) {
				System.out.println("Mais de 1 bot foi detectado, carregando o primeiro.");
			}
			try {
				OusuPlugin plugin = filtro.get(0).getPluginMain().asSubclass(OusuPlugin.class).newInstance();
				this.plugin.add(new Plugin(plugin, filtro.get(0).getPluginConfiguration(), this));
				started = true;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			getPlugin().startPlugin();
		} catch (IOException | InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | ConfigurationNotFound e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public void stopPlugin() {
		if (!started) {
			throw new RuntimeException("Não há nenhum plugin iniciado.");
		}
		//getPlugin().stopPlugin();
	}

	public Plugin getPlugin() {
		return plugin.get(0);
	}

}
