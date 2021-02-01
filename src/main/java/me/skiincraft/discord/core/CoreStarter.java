package me.skiincraft.discord.core;

import me.skiincraft.beans.InjectorFactory;
import me.skiincraft.beans.annotation.Component;
import me.skiincraft.beans.stereotypes.CommandMap;
import me.skiincraft.beans.stereotypes.EventMap;
import me.skiincraft.beans.stereotypes.RepositoryMap;
import me.skiincraft.beans.stereotypes.UtilMap;
import me.skiincraft.discord.core.command.impl.CommandParser;
import me.skiincraft.discord.core.command.impl.DefaultCommandManager;
import me.skiincraft.discord.core.configuration.CoreSettings;
import me.skiincraft.discord.core.configuration.InicializeOptions;
import me.skiincraft.discord.core.configuration.InternalSettings;
import me.skiincraft.discord.core.configuration.OusuConfiguration;
import me.skiincraft.discord.core.jda.GuildEvents;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.PluginLoader;
import me.skiincraft.discord.core.repository.GuildRepository;
import me.skiincraft.discord.core.utils.CoreUtils;
import me.skiincraft.sql.BasicSQL;
import me.skiincraft.sql.exceptions.RepositoryException;
import me.skiincraft.sql.platform.SQLConfiguration;
import me.skiincraft.sql.platform.template.PostgreSQL;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoreStarter {

	private static final Class[] injectorAnnotations =  new Class[]
			{Component.class, CommandMap.class, EventMap.class, RepositoryMap.class, UtilMap.class};

	static Logger logger = LogManager.getLogger(CoreStarter.class);
	
	public static void main(String[] args) {
		System.setOut(IoBuilder.forLogger(logger)
				.setLevel(Level.INFO)
				.buildPrintStream());

		CoreStarter.getLogger().info("Starting log4J - OusuCore\n");

		CoreUtils.createPath("bots", "library", "dependency", "logs");
		CoreUtils.createConfigurationFiles();

		File botJar = getAvailableBot();
		if (Objects.isNull(botJar)) {
			CoreStarter.getLogger().error("Não foi encontrado nenhum bot na pasta ./bots");
			return;
		}

		loadDependency();
		loadLibrary();

		if (Objects.isNull(((URLClassLoader) Thread.currentThread()
				.getContextClassLoader())
				.findResource("net/dv8tion/jda"))) {
			CoreStarter.getLogger().fatal("JDA não está instalado na pasta ./library");
			return;
		}

		CoreSettings settings = CoreSettings.of(new OusuConfiguration().getConfiguration(Paths.get("Settings.ini")));
		PluginLoader pluginLoader = new PluginLoader(botJar);

		OusuPlugin plugin = pluginLoader.getOusuPlugin();
		CoreStarter.getLogger().info("Carregando " + plugin.getClass().getSimpleName());
		CoreUtils.createPath("bots/" + pluginLoader.getBotFile().getBotName() + "/",
				new String[]{"assets", "fonts", "language"});
		InicializeOptions inicializeOptions = new InicializeOptions(new DefaultCommandManager("OusuCommands", 10), settings);
		plugin.onLoad(inicializeOptions);

		DefaultShardManagerBuilder shardBuilder = DefaultShardManagerBuilder.createDefault(inicializeOptions.getToken());
		applyCacheFlag(shardBuilder, inicializeOptions.getCacheFlag());
		applyGatewayIntents(shardBuilder, inicializeOptions.getGatewayIntent());
		applyOthers(shardBuilder, inicializeOptions, settings);
		loadSQLDatabase();

		CoreStarter.getLogger().info("ShardBuilder foi configurado com sucesso.");
		try {
			CoreStarter.getLogger().info("Iniciando ShardManager, espero que dê certo!");
			OusuCore.inicialize(pluginLoader, InjectorFactory.getInstance().createNewInjector(plugin.getClass(), injectorAnnotations),
					shardBuilder.build(), inicializeOptions.getCommandManager(), new InternalSettings(pluginLoader.getBotFile()), logger);
		} catch (LoginException | CompletionException e) {
			CoreStarter.getLogger().warn("Suas credenciais estão incorretas verifique o Token em settings.ini", e);
		}
	}

	private static void applyGatewayIntents(DefaultShardManagerBuilder smb, List<GatewayIntent> gatewayIntents){
		if (gatewayIntents.size() == 0){
			smb.disableIntents(Arrays.asList(GatewayIntent.values()));
			logger.info("Nenhum Gateway Intent está ativo.");
		} else {
			smb.enableIntents(gatewayIntents);
			logger.info("Estão habilitados os Gateways: " + gatewayIntents.stream()
					.map(i -> "[" + i.name() + "] ").collect(Collectors.joining()));
		}
	}

	private static void applyCacheFlag(DefaultShardManagerBuilder smb, List<CacheFlag> cacheFlags){
		if (cacheFlags.size() == 0){
			smb.disableCache(Arrays.asList(CacheFlag.values()));
			logger.info("Nenhum Cache Flag está ativo.");
		} else {
			smb.enableCache(cacheFlags);
			logger.info("Estão habilitados os Cache Flags: " + cacheFlags.stream()
					.map(i -> "[" + i.name() + "] ").collect(Collectors.joining()));
		}
	}

	private static void applyOthers(DefaultShardManagerBuilder smb, InicializeOptions inicializeOptions, CoreSettings settings){
		smb.setEventManagerProvider((id) -> new AnnotatedEventManager());
		smb.setChunkingFilter(inicializeOptions.getChunkfilter());
		smb.addEventListeners(new CommandParser(inicializeOptions.getCommandManager()));
		smb.addEventListeners(new GuildEvents());
		smb.setChunkingFilter(settings.getFilter());
		smb.setShardsTotal(settings.getShards());
	}

	private static void loadSQLDatabase(){
		INIConfiguration ini = new OusuConfiguration().getConfiguration(Paths.get("SQLConfiguration.ini"));
		SQLConfiguration configuration = SQLConfiguration.getPostgresConfig();
		configuration.setHost(String.format("jdbc:postgresql://%s:%s/", ini.getString("DatabaseCredentials.server"),
				ini.getLong("DatabaseCredentials.port")));
		configuration.setDatabase(ini.getString("DatabaseCredentials.database"));
		configuration.setUser(ini.getString("DatabaseCredentials.user"));
		configuration.setPassword(ini.getString("DatabaseCredentials.password"));
		try {
			BasicSQL.create(new PostgreSQL(configuration)).registerRepository(GuildRepository.class);
		} catch (InstantiationException | SQLException | RepositoryException e) {
			CoreStarter.getLogger().fatal("Não foi possível carregar o banco de dados.", e);
			System.exit(0);
		}
	}

	private static void loadLibrary() {
		try {
			for (Path path : Files.newDirectoryStream(Paths.get("library"))) {
				if (path.toFile().getName().contains(".jar")) {
					URLClassLoader c = new URLClassLoader(new URL[]{path.toFile().toURI().toURL()});
					if (c.findResource("net/dv8tion/jda") != null) {
						CoreUtils.addClassPathURL(path.toFile());
						c.close();
					}
					c.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void loadDependency() {
		try {
			for (Path path : Files.newDirectoryStream(Paths.get("dependency"))) {
				if (path.toFile().getName().contains(".jar")) {
					CoreUtils.addClassPathURL(path.toFile());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Nullable
	public static File getAvailableBot() {
		try {
			Stream<Path> stream = CoreUtils.iteratorToList(Files.newDirectoryStream(Paths.get("bots"))
					.iterator())
					.stream();

			List<File> list = stream.filter(path -> path.getFileName().toString().contains(".jar"))
					.map(Path::toFile)
					.filter(file -> {
						try {
							JarURLConnection connection = (JarURLConnection) new URL("jar:file:" + file.getAbsolutePath() + "!/bot.json").openConnection();
							return connection.getInputStream() != null;
						} catch (Exception ignored) {
							return false;
						}
					})
					.collect(Collectors.toList());

			if (list.size() == 0) {
				return null;
			}

			if (list.size() > 1) {
				logger.info("Foi detectado mais de 1 arquivo jar, na pasta bots.");
			}

			return list.get(0);
		} catch (IOException e) {
			return null;
		}
	}

	public static Logger getLogger() {
		return logger;
	}
}
