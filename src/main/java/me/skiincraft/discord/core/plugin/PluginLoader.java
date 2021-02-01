package me.skiincraft.discord.core.plugin;

import com.google.gson.Gson;
import me.skiincraft.discord.core.exception.BotLoaderException;
import me.skiincraft.discord.core.plugin.objects.BotFile;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class PluginLoader {

    private final OusuPlugin ousuPlugin;
    private final ClassLoader classloader;
    private final BotFile botFile;
    private final File file;

    public PluginLoader(File file) throws BotLoaderException {
        this.file = file;
        this.classloader = loadJar(file);
        this.botFile = loadBotFile();
        this.ousuPlugin = loadOusuPlugin();
    }

    public OusuPlugin getOusuPlugin() {
        return ousuPlugin;
    }

    public ClassLoader getClassloader() {
        return classloader;
    }

    public BotFile getBotFile() {
        return botFile;
    }

    public File getFile() {
        return file;
    }

    private OusuPlugin loadOusuPlugin() throws BotLoaderException {
        try {
            Class<?> ousuPlugin = classloader.loadClass(botFile.getBotMain());
            return ousuPlugin.asSubclass(OusuPlugin.class).newInstance();
        } catch (ClassNotFoundException e){
            throw new BotLoaderException("Classe main do bot não foi encontrada.", e, 2);
        } catch (IllegalAccessException e) {
            throw new BotLoaderException("Problema não identificado...", e, 3);
        } catch (InstantiationException e) {
            throw new BotLoaderException("Não foi possível instanciar a classe Main", e, 4);
        } catch (ClassCastException e){
            throw new BotLoaderException(String.format("A Classe main não estende %s", OusuPlugin.class.getSimpleName()), e, 3);
        } catch (NoClassDefFoundError e){
            throw onDependencyException(e);
        }
    }

    private BotLoaderException onDependencyException(NoClassDefFoundError error){
        return new BotLoaderException(String.format("Alguma dependência está faltando no seu bot! (%s)", error.getMessage()), error, 5);
    }

    private BotFile loadBotFile() throws BotLoaderException {
        InputStream inputStream = classloader.getResourceAsStream("bot.json");
        if (Objects.isNull(inputStream)){
            throw new BotLoaderException("Não foi possivel encontrar o arquivo de configuração do bot.", 1);
        }
        return new Gson().fromJson(new InputStreamReader(inputStream), BotFile.class);
    }

    private URLClassLoader loadJar(File jarFile) throws BotLoaderException {
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(classLoader);
            return classLoader;
        } catch (Exception e){
            throw new BotLoaderException("Houve um problema ao carregar o arquivo .jar", e, 0);
        }
    }
}
