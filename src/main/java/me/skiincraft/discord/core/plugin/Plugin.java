package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.gson.GsonBuilder;

import me.skiincraft.discord.core.command.CommandManager;
import me.skiincraft.discord.core.common.EvalCommand;
import me.skiincraft.discord.core.configuration.Language;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.jda.CommandAdapter;
import me.skiincraft.discord.core.jda.ListenerAdaptation;
import me.skiincraft.discord.core.sqlite.SQLite;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Plugin {
	
	private final EventManager eventManager;
	private final CommandManager commandManager;
	private final PluginManager pluginManager;

	private final OusuPlugin instancePlugin;
	private final Map<String, Object> pluginConfiguration;
	private final List<Language> languages;

	private ThreadGroup threadGroup;
	private ShardManager shardManager;
	private SQLite sqlite;

	public Plugin(OusuPlugin mainclass, Map<String, Object> configuration, PluginManager pm) {
		this.instancePlugin = mainclass;
		this.pluginConfiguration = configuration;
		this.pluginManager = pm;
		this.eventManager = new EventManager();
		this.commandManager = new CommandManager();
		this.sqlite = new SQLite(this);
		this.languages = new ArrayList<>();
	}
	
	private void loads() {
		for (File file : Arrays.asList(getLanguagePath(), getFontPath(), getAssetsPath())) {
			file.mkdirs();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized final void startPlugin() throws IllegalAccessException, SecurityException {
		System.out.println(getName() + " - Loading Bot");
		loads();
		
		threadGroup = new ThreadGroup(getName() + "-Threads");

		// Chamar metodo load do bot.
		sqlite = new SQLite(this);
		instancePlugin.onLoad();
		// Mudar field privado plugin
		FieldUtils.writeField(instancePlugin, "plugin", this, true);

		Map<String, Object> dmap = (Map<String, Object>) pluginConfiguration.get("discord");
		DefaultShardManagerBuilder shardbuilder = DefaultShardManagerBuilder.createDefault(dmap.get("token").toString());

		shardbuilder.addEventListeners(new CommandAdapter(this));
		shardbuilder.addEventListeners(new ListenerAdaptation(this));
		commandManager.registerCommand(new EvalCommand());
		shardbuilder.disableCache(CacheFlag.VOICE_STATE);

		shardbuilder.setChunkingFilter(ChunkingFilter.NONE);
		shardbuilder.setShardsTotal(Integer.parseInt(dmap.get("shards").toString()));
		
		try {
			this.shardManager = shardbuilder.build();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Esperando todas as shards carregarem...");

		Thread t = new Thread(getThreadGroup(), () -> {
			instancePlugin.onEnable();
			System.out.println("Carregando on Enable");
			try {
				FieldUtils.writeField(instancePlugin, "shardmanager", shardManager, true);
				FieldUtils.writeField(instancePlugin, "plugin", this, true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			// Criar aquivos de tradução
			for (Language lang : languages) {
				File translatefile = new File(getPluginPath().getAbsolutePath() + "/language/" + lang.getLanguageCode() + "_" + lang.getCountryCode() + ".json");
				if (!translatefile.exists()) {
					try {
						translatefile.createNewFile();
						FileWriter writer = new FileWriter(translatefile);
						writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(LanguageManager.jsonTemplate()));
						writer.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}, "[" + getName() + "-Main]");
		t.start();
	}
	
	public ShardManager getShardManager() {
		return shardManager;
	}
	
	public SQLite getSQLite() {
		return sqlite;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}
	
	public String getName() {
		return pluginConfiguration.get("botname").toString();
	}

	public File getPluginPath() {
		return new File("bots/" + getName() + "/");
	}

	public File getFontPath() {
		return new File("bots/" + getName() + "/fonts/");
	}

	public File getAssetsPath() {
		return new File("bots/" + getName() + "/assets/");
	}

	public File getLanguagePath() {
		return new File("bots/" + getName() + "/language/");
	}
	
	public List<Language> getLanguages() {
		return languages;
	}
	
	public void addLanguage(Language language) {
		languages.add(language);
	}

	public Map<String, Object> getPluginConfiguration() {
		return pluginConfiguration;
	}
	
	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}
	
}
