package me.skiincraft.discord.core.configuration;


import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OusuConfiguration {

    public boolean createSQLConfiguration(Path fileName) throws IOException, ConfigurationException {
        if (Files.exists(fileName)){
            return false;
        }
        Configurations configurations = new Configurations();
        File file = createIfNotExists(fileName);
        FileBasedConfigurationBuilder<INIConfiguration> based = configurations.iniBuilder(file);
        INIConfiguration configuration = based.getConfiguration();
        configuration.addProperty("Database.template", "postgres");
        configuration.addProperty("DatabaseCredentials.server", "localhost");
        configuration.addProperty("DatabaseCredentials.database", "localhost");
        configuration.addProperty("DatabaseCredentials.port", 5432);
        configuration.addProperty("DatabaseCredentials.user", "postgres");
        configuration.addProperty("DatabaseCredentials.password", 123);
        based.save();
        return true;
    }

    public boolean createSettingsFile(Path fileName) throws IOException, ConfigurationException {
        if (Files.exists(fileName)){
            return false;
        }
        Configurations configurations = new Configurations();
        File file = createIfNotExists(fileName);
        FileBasedConfigurationBuilder<INIConfiguration> based = configurations.iniBuilder(file);
        INIConfiguration configuration = based.getConfiguration();
        configuration.addProperty("BotConfiguration.token", "your-token");
        configuration.addProperty("BotConfiguration.shards", 1);
        configuration.addProperty("OtherConfiguration.chunkfilter", true);
        configuration.addProperty("OtherConfiguration.cacheflag", "default");
        configuration.addProperty("OtherConfiguration.gatewayintents", "default");

        based.save();
        return true;
    }

    public INIConfiguration getConfiguration(Path file) {
        try {
            Configurations configurations = new Configurations();
            return configurations.ini(file.toFile());
        } catch (ConfigurationException e){
            e.printStackTrace();
        }
        return null;
    }

    private File createIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path.toFile().getAbsoluteFile();
    }
}
