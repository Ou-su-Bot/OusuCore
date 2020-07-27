package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.gson.GsonBuilder;

import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.commands.CommandAdapter;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.events.Adapter;
import me.skiincraft.discord.core.exception.ConfigurationNotFound;
import me.skiincraft.discord.core.exception.PluginRunningException;
import me.skiincraft.discord.core.multilanguage.Language;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.objects.DiscordInfo;
import me.skiincraft.discord.core.sqlite.SQLite;
import me.skiincraft.discord.core.view.objects.ViewerUpdater;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Plugin {

	private String name;
	private ThreadGroup threadGroup;
	
	private PluginManager pluginManager;
	private ShardManager shardManager;
	private EventManager eventManager;
	
	private boolean running;
	private boolean instantiated;
	
	private OusuPlugin instancePlugin;

	private SQLite sqLite;
	private DiscordInfo discord;	

	private static ArrayList<ListenerAdapter> events = new ArrayList<>();
	private static ArrayList<Command> commands = new ArrayList<>();

	public Plugin(OusuPlugin mainclass, DiscordInfo discord, PluginManager pm) {
		this.instancePlugin = mainclass;
		this.name = discord.getBotname();
		this.discord = discord;
		this.pluginManager = pm;
		this.eventManager = new EventManager();
	}

	public synchronized final void startPlugin() throws InstantiationException, IllegalAccessException,
			ConfigurationNotFound, NoSuchFieldException, SecurityException {
		if (isRunning()) {
			throw new PluginRunningException("Plugin já esta rodando.");
		}
		
		System.out.println(name + " - Loading Bot");

		ThreadGroup thispluginthreads = new ThreadGroup(getDiscordInfo().getBotname() + "-Threads");
		threadGroup = thispluginthreads;

		// Chamar metodo load do bot.
		instancePlugin.onLoad();

		// Criar pasta local do bot.
		getAssetsPath().mkdirs();
		getLanguagePath().mkdirs();
		getFontPath().mkdirs();

		// Criar aquivos de tradução
		for (Language lang : Language.values()) {
			File translatefile = new File(getPluginPath().getAbsolutePath() + "/language/" + lang.getFileName());
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

		// Mudar field privado plugin
		FieldUtils.writeField(instancePlugin, "plugin", this, true);

		DefaultShardManagerBuilder shardbuilder = new DefaultShardManagerBuilder();

		if (discord == null) {
			throw new ConfigurationNotFound("Configurações do discord estão nula(s)");
		}

		shardbuilder.setToken(discord.getToken());
		sqLite = new SQLite(this);
		shardbuilder.addEventListeners(new CommandAdapter(this));
		shardbuilder.addEventListeners(new Adapter(this));
		shardbuilder.setDisabledCacheFlags(EnumSet.of(CacheFlag.VOICE_STATE));

		shardbuilder.setChunkingFilter(ChunkingFilter.NONE);
		shardbuilder.setShardsTotal(discord.getTotalShards());

		FieldUtils.writeField(instancePlugin, "builder", shardbuilder, true);

		getSQLiteDatabase().abrir();
		getSQLiteDatabase().setup();

		Thread t = new Thread(thispluginthreads, () -> {
			Timer timer = new Timer();
			timer.schedule(new ViewerUpdater(timer), 1000, TimeUnit.SECONDS.toMillis(5));
			instancePlugin.onEnable();
		}, "[" + discord.getBotname() + "-Main]");
		t.start();
	}

	public final void restartPlugin() throws InstantiationException, IllegalAccessException, NoSuchFieldException,
			SecurityException, ConfigurationNotFound {
		if (!isRunning()) {
			return;
		}
		stopPlugin();
		startPlugin();
	}

	@SuppressWarnings("deprecation")
	public
	final void stopPlugin() {
		if (!isRunning()) {
			return;
		}
		if (!isInstantiated()) {
			return;
		}
		if (shardManager == null) {
			return;
		}

		instancePlugin.onDisable();
		shardManager.shutdown();
		threadGroup.stop();
	}
	
	public String getName() {
		return name;
	}

	public SQLite getSQLiteDatabase() {
		return sqLite;
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public ArrayList<ListenerAdapter> getEvents() {
		return events;
	}

	public SQLite getSQLite() {
		return sqLite;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}

	public void addCommand(Command command) {
		commands.add(command);
	}

	public boolean isRunning() {
		return running;
	}

	public DiscordInfo getDiscordInfo() {
		return discord;
	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public boolean isInstantiated() {
		return instantiated;
	}

	public File getPluginPath() {
		return new File("bots/" + discord.getBotname() + "/");
	}

	public File getFontPath() {
		return new File("bots/" + discord.getBotname() + "/fonts/");
	}

	public File getAssetsPath() {
		return new File("bots/" + discord.getBotname() + "/assets/");
	}

	public File getLanguagePath() {
		return new File("bots/" + discord.getBotname() + "/language/");
	}

	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}
	
}
