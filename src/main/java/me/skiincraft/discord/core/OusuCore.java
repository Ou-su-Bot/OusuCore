package me.skiincraft.discord.core;

import me.skiincraft.beans.InjectManager;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.CommandManager;
import me.skiincraft.discord.core.common.EventListener;
import me.skiincraft.discord.core.configuration.InternalSettings;
import me.skiincraft.discord.core.configuration.Language;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.PluginLoader;
import me.skiincraft.discord.core.repository.GuildRepository;
import me.skiincraft.sql.BasicSQL;
import me.skiincraft.sql.exceptions.RepositoryException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public final class OusuCore {

    private static InternalSettings internalSettings;

    private static OusuPlugin instance;
    private static ShardManager shardManager;
    private static Logger logger;

    private OusuCore() {}

    public static ShardManager getShardManager() {
        return shardManager;
    }

    public static void addLanguage(Language language){
        internalSettings.addLanguage(language);
    }

    public static void removeLanguage(Language language){
        internalSettings.removeLanguage(language);
    }

    public static List<Language> getLanguages(){
        return internalSettings.getLanguages();
    }

    public static void registerCommand(Command command) {
        getCommandManager().registerCommand(command);
    }

    public static void unregisterCommand(Command command) {
        getCommandManager().registerCommand(command);
    }

    public static void registerListener(EventListener listener) {
        shardManager.removeEventListener(listener);
    }

    public static void unregisterListener(EventListener listener) {
        shardManager.addEventListener(listener);
    }

    public static Path getFontPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/fonts/");
    }

    public static Path getAssetsPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/assets/");
    }

    public static Path getLanguagePath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/language/");
    }

    public static Path getBotPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/");
    }

    public static InternalSettings getInternalSettings() {
        return internalSettings;
    }

    public static CommandManager getCommandManager() {
        return CommandManager.getInstance();
    }

    public static OusuPlugin getInstance() {
        return instance;
    }

    public static Path getPluginPath() {
        return Paths.get("bots/" + internalSettings.getBotName());
    }

    public static void callEvent(GenericEvent event, JDA jda) {
        jda.getEventManager().handle(event);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void shutdown() {
        try {
            logger.info("Bot está sendo desativado.");
            instance.onDisable();
            logger.info("Encerrando conexões com JDA");
            if (shardManager != null) {
                shardManager.shutdown();
            }
            logger.info("Desativando conexão com o banco de dados");
            BasicSQL.getSQL().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void printStackTrace(String message, Throwable throwable) {
        getLogger().error(message, throwable);
    }

    public static void printStackTrace(Throwable throwable) {
        getLogger().throwing(throwable);
    }

    public static void inicialize(PluginLoader loader, ShardManager shardManager, InternalSettings internalSettings, Logger logger)  {
        if (OusuCore.instance != null) {
            logger.warn("Tentou criar uma instancia OusuCore, mas é possivel somente 1 instancia(s) ativa(s).");
            return;
        }

        try {
            OusuCore.instance = loader.getOusuPlugin();
            OusuCore.shardManager = shardManager;
            OusuCore.internalSettings = internalSettings;
            OusuCore.logger = logger;
            
            InjectManager.getInstance().mapClasses(instance);
            InjectManager.getInstance().map(Objects.requireNonNull(getGuildRepository()));
            InjectManager.getInstance().map(loader);

            instance.onEnable();
            logger.info(String.format("%s foi carregado com sucesso!", internalSettings.getBotName()));
        } catch (Exception e){
            logger.error("Ocorreu um problema ao carregar onEnable, verifique se está atualizado!", e);
        }
    }

    public static GuildRepository getGuildRepository() {
        try {
            return BasicSQL.getInstance().registerRepository(GuildRepository.class);
        } catch (RepositoryException e) {
            throw new RuntimeException("GuildRepository is Null");
        }
    }
}
