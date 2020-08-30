package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.gson.GsonBuilder;

import me.skiincraft.discord.core.command.CommandManager;
import me.skiincraft.discord.core.configuration.Language;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.jda.CommandAdapter;
import me.skiincraft.discord.core.jda.ListenerAdaptation;
import me.skiincraft.discord.core.sqlite.SQLite;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Plugin {

	private SQLite sqlite;
	private EventManager eventManager;
	private CommandManager commandManager;
	private PluginManager pluginManager;
	private ShardManager shardManager;
	
	
	private Map<String, Object> pluginConfiguration;
	private ThreadGroup threadGroup;
	private OusuPlugin instancePlugin;
	
	private List<Language> languages;

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
		getAssetsPath().mkdirs();
		getLanguagePath().mkdirs();
		getFontPath().mkdirs();
	}

	@SuppressWarnings("unchecked")
	public synchronized final void startPlugin() throws InstantiationException, IllegalAccessException,	NoSuchFieldException, SecurityException {
		System.out.println(getName() + " - Loading Bot");
		loads();
		
		threadGroup = new ThreadGroup(getName() + "-Threads");

		// Chamar metodo load do bot.
		sqlite = new SQLite(this);
		instancePlugin.onLoad();
		// Mudar field privado plugin
		FieldUtils.writeField(instancePlugin, "plugin", this, true);

		DefaultShardManagerBuilder shardbuilder = new DefaultShardManagerBuilder();

		Map<String, Object> dmap = (Map<String, Object>) pluginConfiguration.get("discord");
		
		shardbuilder.setToken(dmap.get("token").toString());
		shardbuilder.addEventListeners(new CommandAdapter(this));
		shardbuilder.addEventListeners(new ListenerAdaptation(this));
		
		shardbuilder.setDisabledCacheFlags(EnumSet.of(CacheFlag.VOICE_STATE));

		shardbuilder.setChunkingFilter(ChunkingFilter.NONE);
		shardbuilder.setShardsTotal(1);
		
		try {
			this.shardManager = shardbuilder.build();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Esperando todas as shards carregarem...");
		for (JDA jda : shardManager.getShards()) {
			try {
				jda.awaitReady();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
			
		System.out.println("Todas as shards foram carregadas.");

		Thread t = new Thread(getThreadGroup(), () -> {
			instancePlugin.onEnable();
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
