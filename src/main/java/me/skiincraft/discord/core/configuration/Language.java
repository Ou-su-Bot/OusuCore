package me.skiincraft.discord.core.configuration;

import java.util.Locale;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;

import net.dv8tion.jda.api.entities.Guild;

public final class Language {
	
	private Locale locale;
	
	public Language(Locale locale) {
		this.locale = locale;
	}
	
	public String getLanguageName() {
		return locale.getDisplayLanguage(Locale.ENGLISH);
	}
	
	public String getName() {
		return locale.getDisplayLanguage(locale);
	}
	
	public String getCountry() {
		return locale.getDisplayCountry(Locale.ENGLISH);
	}
	
	public String getCountryCode() {
		return locale.getISO3Country().substring(0, 2);
	}
	
	public String getLanguageCode() {
		return locale.getLanguage().toUpperCase();
	}

	public Locale getLocale() {
		return locale;
	}
	
	public static Language getDefaultLanguage() {
		return new Language(Locale.getDefault());
	}
	
	public static Language getGuildLanguage(Guild guild) {
		Plugin plugin = OusuCore.getPluginManager().getPlugin();
		GuildDB db = new GuildDB(guild);
		
		return plugin.getLanguages().stream().filter(l-> l.getLanguageName().equalsIgnoreCase(db.get("language"))).findAny().orElse(getDefaultLanguage());
	}
	
}
