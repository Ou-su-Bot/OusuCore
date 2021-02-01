package me.skiincraft.ousucore.configuration;

import me.skiincraft.ousucore.language.Language;
import me.skiincraft.ousucore.plugin.objects.BotFile;

import java.util.ArrayList;
import java.util.List;

public class InternalSettings {

    private final List<Language> languages;
    private final BotFile botFile;

    public InternalSettings(BotFile botFile) {
        this.languages = new ArrayList<>();
        this.botFile = botFile;
    }

    public List<Language> getLanguages() {
        return new ArrayList<>(languages);
    }

    public void addLanguage(Language language){
        languages.add(language);
    }

    public String getBotName(){
        return botFile.getBotName();
    }

    public String getMain(){
        return botFile.getBotMain();
    }

    public String getDefaultPrefix(){
        return botFile.getPrefix();
    }

    public long getOwnerId(){
        return botFile.getOwnerId();
    }

    public void removeLanguage(Language language){
        languages.remove(language);
    }

}
