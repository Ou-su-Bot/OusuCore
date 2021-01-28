package me.skiincraft.discord.core.language;

import java.util.Arrays;
import java.util.Locale;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.repository.OusuGuild;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;

public final class Language extends LanguageBundle {

	private final Region[] regions;

	public Language(Locale locale, Region... regions) {
		super(locale, OusuCore.getLanguagePath());
		this.regions = regions;
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
	
	public static Language getDefaultLanguage() {
		return new Language(Locale.getDefault());
	}
	
	public static Language getGuildLanguage(Guild guild) {
		return OusuCore.getLanguages().stream().filter(l-> l.getLanguageName().equalsIgnoreCase(getLanguageStringFromDatabase(guild)))
				.findAny()
				.orElse(getGuildLanguageFromDatabase(guild));
	}

	public static Language getGuildLanguageFromDatabase(Guild guild){
		return Arrays.stream(Locale.getAvailableLocales())
				.filter(locale -> locale.getDisplayLanguage(Locale.ENGLISH)
						.equalsIgnoreCase(getLanguageStringFromDatabase(guild)))
				.findFirst()
				.map(Language::new)
				.orElse(getDefaultLanguage());
	}

	private static String getLanguageStringFromDatabase(Guild guild){
		return OusuCore.getGuildRepository().getById(guild.getIdLong())
				.map(OusuGuild::getLanguage).orElse(Locale.ENGLISH.getDisplayLanguage());
	}

	public Region[] getRegions() {
		return regions;
	}
}
