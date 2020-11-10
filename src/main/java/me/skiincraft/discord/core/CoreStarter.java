package me.skiincraft.discord.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.skiincraft.discord.core.command.CommandManager;
import me.skiincraft.discord.core.configuration.CoreSettings;
import me.skiincraft.discord.core.configuration.InternalSettings;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.jda.CommandAdapter;
import me.skiincraft.discord.core.jda.ListenerAdaptation;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.ShardBuilderLoader;
import me.skiincraft.discord.core.sqlite.SQLite;
import me.skiincraft.discord.core.utils.CoreUtils;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoreStarter {

	public static String[] JAVA_ARGUMENTS;
	static Logger logger = LogManager.getLogger(CoreStarter.class);
	
	public static void main(String[] args) {
		logger.info("Starting log4J - OusuCore\n");
		CoreStarter.JAVA_ARGUMENTS = args;
		CoreUtils.createPath("bots", "library", "dependency", "logs");
		CoreUtils.createConfig();

		File botJar = getAvailableBot();
		if (botJar == null) {
			logger.error("Não foi encontrado nenhum bot na pasta ./bots");
			return;
		}

		AtomicReference<JsonObject> botJson = new AtomicReference<>();
		try {
			JarURLConnection connection = (JarURLConnection) new URL("jar:file:" + botJar.getAbsolutePath() + "!/bot.json").openConnection();
			botJson.set(new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject());
			logger.info(String.format("%s Está sendo iniciado...", botJson.get().get("BotName").getAsString()));
		} catch (IOException e) {
			logger.error(e.getMessage());
			return;
		}

		loadDependency();
		loadLibrary();

		URL url = ((URLClassLoader) Thread.currentThread()
				.getContextClassLoader())
				.findResource("net/dv8tion/jda");

		if (url == null) {
			logger.fatal("JDA não está instalado na pasta ./library");
			return;
		}

		AtomicReference<DefaultShardManagerBuilder> atomicShardBuilder = new AtomicReference<>();
		AtomicReference<OusuPlugin> atomicOusuPlugin = new AtomicReference<>();
		AtomicReference<CoreSettings> atomicCoreSettings = new AtomicReference<>();


		try {
			Class<?> clazz = Class.forName(botJson.get().get("Main").getAsString(), true, new URLClassLoader(new URL[]{botJar.toURI().toURL()}));
			atomicOusuPlugin.set(clazz.asSubclass(OusuPlugin.class).newInstance());
			atomicCoreSettings.set(CoreSettings.of(new JsonParser().parse(new FileReader(new File("settings.json"))).getAsJsonObject()));
			atomicShardBuilder.set(DefaultShardManagerBuilder.createDefault(atomicCoreSettings.get().getToken()));
		} catch (NoClassDefFoundError e) {
			logger.error(String.format("Alguma dependência está faltando no seu bot! (%s)", e.getMessage()));
			return;
		} catch (ClassNotFoundException e) {
			logger.error(String.format("Ops! A classe main está incorreta: (%s)", e.getMessage()));
			return;
		} catch (ClassCastException e) {
			logger.error(String.format("A classe main não está estendendo %s", OusuPlugin.class.getSimpleName()));
			return;
		} catch (JsonParseException e) {
			logger.error("O arquivo settings.json está invalido, verifique se há problemas.");
			return;
		} catch (Exception e) {
			logger.error("Ops... Alguma coisa aconteceu! " + e.getMessage());
			return;
		}

		OusuPlugin plugin = atomicOusuPlugin.get();
		logger.info("Carregando " + plugin.getClass().getSimpleName());
		CoreUtils.createPath("bots/" + botJson.get().get("BotName").getAsString() + "/",
				new String[]{"assets", "fonts", "language"});

		plugin.onLoad();
		DefaultShardManagerBuilder shardBuilder = atomicShardBuilder.get();
		if (botJson.get().has("Preloader")) {
			try {
				Class<?> clazz = Class.forName(botJson.get().get("Preloader").getAsString(), true, new URLClassLoader(new URL[]{botJar.toURI().toURL()}));
				ShardBuilderLoader builder = clazz.asSubclass(ShardBuilderLoader.class).newInstance();
				builder.inicialize(shardBuilder);
			} catch (NoClassDefFoundError e) {
				logger.error(String.format("Alguma dependência está faltando no seu bot! (%s)", e.getMessage()));
				return;
			} catch (ClassNotFoundException e) {
				logger.error(String.format("Ops! A classe Preloader está incorreta: (%s)", e.getMessage()));
				return;
			} catch (ClassCastException e) {
				logger.error(String.format("A classe Preloader não está implementando %s", ShardBuilderLoader.class.getSimpleName()));
				return;
			} catch (Exception e) {
				logger.error("Ops... Alguma coisa aconteceu! " + e.getMessage());
				return;
			}
		} else {
			CoreSettings coreSettings = atomicCoreSettings.get();
			applyCacheFlag(shardBuilder, coreSettings.getCacheFlags());
			applyGatewayIntents(shardBuilder, coreSettings.getGatewayIntents());

			shardBuilder.enableCache(coreSettings.getCacheFlags());
			shardBuilder.addEventListeners(new CommandAdapter());
			shardBuilder.addEventListeners(new ListenerAdaptation());
			shardBuilder.setChunkingFilter(coreSettings.getFilter());
			shardBuilder.setShardsTotal(coreSettings.getShards());
		}

		logger.info("ShardBuilder foi configurado com sucesso.");
		try {
			logger.info("Iniciando ShardManager, espero que dê certo!");
			ShardManager shardManager = shardBuilder.build();
			OusuCore.inicialize(CommandManager.getInstance(), EventManager.getInstance(), plugin, shardManager, new InternalSettings(new ArrayList<>(), new SQLite(botJson.get().get("BotName").getAsString()), botJson.get()), logger);
		} catch (LoginException | CompletionException e) {
			logger.warn("Suas credenciais estão incorretas verifique o Token em settings.json", e);
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
			Stream<Path> stream = CoreUtils.iteratorToList(Files.newDirectoryStream(Paths.get("bots")).iterator()).stream();
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
