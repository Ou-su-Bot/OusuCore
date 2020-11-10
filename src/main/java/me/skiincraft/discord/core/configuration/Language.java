package me.skiincraft.discord.core.configuration;

import java.util.Locale;

import me.skiincraft.discord.core.OusuCore;
import net.dv8tion.jda.api.entities.Guild;

public final class Language {
	
	private final Locale locale;
	
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
		return OusuCore.getLanguages().stream().filter(l-> l.getLanguageName().equalsIgnoreCase(new GuildDB(guild).get("language"))).findAny().orElse(getDefaultLanguage());
	}
	
}
