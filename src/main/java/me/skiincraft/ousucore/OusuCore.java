package me.skiincraft.ousucore;

import me.skiincraft.beans.Injector;
import me.skiincraft.ousucore.command.CommandExecutor;
import me.skiincraft.ousucore.command.ICommandManager;
import me.skiincraft.ousucore.common.EventListener;
import me.skiincraft.ousucore.configuration.InternalSettings;
import me.skiincraft.ousucore.language.Language;
import me.skiincraft.ousucore.plugin.OusuPlugin;
import me.skiincraft.ousucore.plugin.PluginLoader;
import me.skiincraft.ousucore.repository.GuildRepository;
import me.skiincraft.ousucore.utils.InjectorUtils;
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
    private static Injector injector;
    private static ICommandManager commandManager;

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

    public static void registerCommand(CommandExecutor command) {
        getCommandManager().registerCommand(command);
    }

    public static void unregisterCommand(CommandExecutor command) {
        getCommandManager().registerCommand(command);
    }

    public static void registerListener(EventListener listener) {
        shardManager.addEventListener(listener);
    }

    public static void unregisterListener(EventListener listener) {
        shardManager.removeEventListener(listener);
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

    public static ICommandManager getCommandManager() {
        return commandManager;
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

    public static Injector getInjector(){
        return injector;
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

    public static void inicialize(PluginLoader loader, Injector injector, ShardManager shardManager, ICommandManager commandManager, InternalSettings internalSettings, Logger logger)  {
        if (OusuCore.instance != null) {
            logger.warn("Tentou criar uma instancia OusuCore, mas é possivel somente 1 instancia(s) ativa(s).");
            return;
        }
        try {
            OusuCore.instance = loader.getOusuPlugin();
            OusuCore.shardManager = shardManager;
            OusuCore.commandManager = commandManager;
            OusuCore.internalSettings = internalSettings;
            OusuCore.logger = logger;
            OusuCore.injector = injector;

            InjectorUtils.configureInjector(injector);
            getInjector().map(shardManager);
            getInjector().map(commandManager);
            getInjector().map(instance);
            getInjector().map(Objects.requireNonNull(getGuildRepository()));
            getInjector().map(loader);

            getInjector().start();
            instance.onEnable();
            getInjector().commit();
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
