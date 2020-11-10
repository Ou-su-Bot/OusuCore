package me.skiincraft.discord.core;

import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.CommandManager;
import me.skiincraft.discord.core.configuration.InternalSettings;
import me.skiincraft.discord.core.configuration.Language;
import me.skiincraft.discord.core.event.Event;
import me.skiincraft.discord.core.event.EventManager;
import me.skiincraft.discord.core.event.Listener;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.sqlite.SQLite;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class OusuCore {

    private static CommandManager commandManager;
    private static EventManager eventManager;
    private static OusuPlugin instance;

    private static ShardManager shardManager;
    private static InternalSettings internalSettings;
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
        commandManager.registerCommand(command);
    }

    public static void unregisterCommand(Command command) {
        commandManager.registerCommand(command);
    }

    public static void registerListener(Listener listener) {
        eventManager.registerListener(listener);
    }

    public static void registerListener(ListenerAdapter listener) {
        eventManager.registerListener(listener);
    }

    public static void unregisterListener(Listener listener) {
        eventManager.unregisterListener(listener);
    }

    public static Path getFontPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/fonts/");
    }

    public static Path getAssetsPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/fonts/");
    }

    public static Path getLanguagePath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/language/");
    }

    public static Path getBotPath() {
        return Paths.get("bots/" + internalSettings.getBotName() + "/");
    }

    public static SQLite getSQLiteDatabase(){
        return internalSettings.getSQLite();
    }

    public static InternalSettings getInternalSettings() {
        return internalSettings;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static OusuPlugin getInstance() {
        return instance;
    }

    public static Path getPluginPath() {
        return Paths.get("bots/" + internalSettings.getBotName());
    }

    public static void callEvent(Event event) {
        eventManager.callEvent(event);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void shutdown() {
        logger.info("Bot está sendo desativado.");
        instance.onDisable();
        logger.info("Encerrando conexões com JDA");
        shardManager.shutdown();
        logger.info("Desativando conexão com o banco de dados");
        getSQLiteDatabase().stop();
        System.exit(0);
    }

    public static void printStackTrace(Throwable throwable) {
        getLogger().throwing(throwable);
    }

    public static void inicialize(CommandManager commandManager, EventManager eventManager, OusuPlugin instance, ShardManager shardManager, InternalSettings internalSettings, Logger logger)  {
        if (OusuCore.commandManager != null){
            logger.warn("Tentou criar uma instancia OusuCore, mas é possivel somente 1 instancia(s) ativa(s).");
            return;
        }

        try {
            Field field = instance.getClass().getDeclaredField("shardManager");
            field.setAccessible(true);
            field.set(instance.getClass(), shardManager);

            OusuCore.commandManager = commandManager;
            OusuCore.eventManager = eventManager;
            OusuCore.instance = instance;
            OusuCore.shardManager = shardManager;
            OusuCore.internalSettings = internalSettings;
            OusuCore.logger = logger;

            boolean sqlconnection = getSQLiteDatabase().connect();
            if (!sqlconnection){
                System.exit(0);
            }
            instance.onEnable();
            logger.info(String.format("%s foi carregado com sucesso!", internalSettings.getBotName()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e){
            logger.error("Ocorreu um problema ao carregar onEnable, verifique se está atualizado!");
            logger.throwing(e);
        }
    }
}
