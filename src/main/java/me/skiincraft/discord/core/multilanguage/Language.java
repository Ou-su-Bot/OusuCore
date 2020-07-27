package me.skiincraft.discord.core.multilanguage;

import java.util.Locale;

import me.skiincraft.discord.core.database.GuildDB;
import net.dv8tion.jda.api.entities.Guild;

public enum Language {
	Portuguese("PT_BR.json", "BR", new Locale("pt", "BR")), English("EN_US.json", "EN", new Locale("en", "US"));

	private String fileName;
	private String countrycode;
	private Locale locale;

	Language(String fileName, String countrycode, Locale locale) {
		this.fileName = fileName;
		this.countrycode = countrycode;
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getFileName() {
		return fileName;
	}

	public String getLanguageCode() {
		return fileName.replace(".json", "").replace("_", "-");
	}

	public String getCountrycode() {
		return countrycode;
	}

	public static Language getGuildLanguage(Guild guild) {
		return Language.valueOf(new GuildDB(guild).get("language"));
	}
}
