package me.skiincraft.discord.core.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.skiincraft.discord.core.sqlite.SQLite;

import java.util.ArrayList;
import java.util.List;

public class InternalSettings {

    private final List<Language> languages;
    private final SQLite sqlite;
    private final JsonElement botFile;

    public InternalSettings(List<Language> languages, SQLite sqlite, JsonElement botFile) {
        this.languages = languages;
        this.sqlite = sqlite;
        this.botFile = botFile;
    }

    public List<Language> getLanguages() {
        return new ArrayList<>(languages);
    }

    public void addLanguage(Language language){
        languages.add(language);
    }

    public String getBotName(){
        return ((JsonObject)botFile).get("BotName").getAsString();
    }

    public String getMain(){
        return ((JsonObject)botFile).get("Main").getAsString();
    }

    public String getDefaultPrefix(){
        return ((JsonObject)botFile).get("Prefix").getAsString();
    }

    public long getOwnerId(){
        return ((JsonObject)botFile).get("OwnerId").getAsLong();
    }

    public void removeLanguage(Language language){
        languages.remove(language);
    }

    public SQLite getSQLite() {
        return sqlite;
    }
}
