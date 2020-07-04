package me.skiincraft.discord.core.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import org.apache.commons.lang3.reflect.FieldUtils;

import me.skiincraft.discord.core.commands.Command;
import me.skiincraft.discord.core.commands.CommandAdapter;
import me.skiincraft.discord.core.exception.BotConfigNotFoundException;
import me.skiincraft.discord.core.exception.PluginRunningException;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.multilanguage.LanguageManager.Language;
import me.skiincraft.discord.core.objects.DataBaseInfo;
import me.skiincraft.discord.core.objects.DiscordInfo;
import me.skiincraft.discord.core.sqlite.SQLite;
import me.skiincraft.discord.core.utils.FileUtils;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Plugin {
	
	private DiscordInfo discord;
	private boolean sqlitedatabase;
	private PluginManager pluginManager;
	private boolean running;
	private boolean instantiated;
	
	private Class<? extends OusuPlugin> pluginMainClass;
	private static OusuPlugin instancePlugin;
	
	private SQLite SQLiteDatabase;

	private ShardManager shardManager;
	private DataBaseInfo databaseinfo;
	
	private static ArrayList<ListenerAdapter> events = new ArrayList<>();
	private static ArrayList<Command> commands = new ArrayList<>();	
	
	public Plugin(Class<? extends OusuPlugin> mainclass, DiscordInfo discord, PluginManager pm) {
		this.pluginMainClass = mainclass;
		this.discord = discord;
		this.pluginManager = pm;
	}
	
	public Class<? extends OusuPlugin> getPluginMainClass() {
		return pluginMainClass;
	}

	public SQLite getSQLiteDatabase() {
		return SQLiteDatabase;
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public DataBaseInfo getDatabaseinfo() {
		return databaseinfo;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
	public ArrayList<ListenerAdapter> getEvents(){
		return events;
	}
	
	public boolean isLiteDatabase() {
		return sqlitedatabase;
	}
	
	public DataBaseInfo getDatabaseInfo() {
		return databaseinfo;
	}
	
	public SQLite getSQLite() {
		return SQLiteDatabase;
	}
	
	protected void addCommand(Command command) {
		commands.add(command);
	}
	
	protected void addListener(ListenerAdapter listener) {
		events.add(listener);
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
		return new File("bots\\" + discord.getBotname() + "\\");
	}
	
	public File getFontPath() {
		return new File("bots\\" + discord.getBotname() + "\\fonts\\");
	}
	
	public File getAssetsPath() {
		return new File("bots\\" + discord.getBotname() + "\\assets\\");
	}
	
	public File getLanguagePath() {
		return new File("bots\\" + discord.getBotname() + "\\language\\");
	}

	
	protected final void startPlugin() throws InstantiationException, IllegalAccessException,
			BotConfigNotFoundException, NoSuchFieldException, SecurityException {
		if (isRunning()) {
			throw new PluginRunningException("Plugin já esta rodando.");
		}
		System.out.println("Rodando plugin");

		instancePlugin = (OusuPlugin) getPluginMainClass().newInstance();
		ThreadGroup thispluginthreads = new ThreadGroup(getDiscordInfo().getBotname() + "-Threads");

		// Chamar metodo load do bot.
		instancePlugin.onLoad();
		
		// Criar pasta local do bot.
		getAssetsPath().mkdirs();
		getLanguagePath().mkdirs();
		getFontPath().mkdirs();
		
		// Criar aquivos de tradução
		for (Language lang:Language.values()) {
			File translatefile = new File(getPluginPath().getAbsolutePath() + "/language/" + lang.getFileName());
			if (!translatefile.exists()) {
				try {
					translatefile.createNewFile();
					FileUtils.writeWithGson(translatefile, LanguageManager.jsonTemplate());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		// Mudar field privado plugin
		FieldUtils.writeField(instancePlugin, "plugin", this, true);

		DefaultShardManagerBuilder shardbuilder = new DefaultShardManagerBuilder();

		if (discord == null) {
			throw new BotConfigNotFoundException("Configurações do discord estão nula(s)");
		}

		
		shardbuilder.setToken(discord.getToken());
		shardbuilder.addEventListeners(new CommandAdapter(this));
		for (ListenerAdapter e : events) {
			shardbuilder.addEventListeners(e);
		}
		shardbuilder.setDisabledCacheFlags(EnumSet.of(CacheFlag.VOICE_STATE));

		shardbuilder.setChunkingFilter(ChunkingFilter.NONE);
		shardbuilder.setShardsTotal(discord.getTotalShards());

		pluginManager.getPlugins().add(this);
		FieldUtils.writeField(instancePlugin, "builder", shardbuilder, true);
		
		SQLiteDatabase = new SQLite(this);
		
		getSQLiteDatabase().abrir();
		getSQLiteDatabase().setup();

		Thread t = new Thread(thispluginthreads, () ->{
			instancePlugin.onEnable();
		}, "[" + discord.getBotname() +"-Main]");
		t.start();
	}
	
	@SuppressWarnings("deprecation")
	protected final void stopPlugin() {
		if (!isRunning()) {
			return;
		}
		
		if (shardManager == null) {
			return;
		}
		if (!isInstantiated()) {
			return;
		}
		instancePlugin.onDisable();
		ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
		String threadgp = Thread.currentThread().getThreadGroup().getName();
		if (threadgp.equalsIgnoreCase(getDiscordInfo().getBotname() + "-Threads")) {
			shardManager.shutdown();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			threadgroup.stop();
		}
	}

}
