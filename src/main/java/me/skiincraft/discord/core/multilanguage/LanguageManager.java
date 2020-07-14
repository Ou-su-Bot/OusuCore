package me.skiincraft.discord.core.multilanguage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.discord.core.database.GuildDB;
import me.skiincraft.discord.core.plugin.OusuPlugin;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.FileUtils;
import net.dv8tion.jda.api.entities.Guild;

public class LanguageManager {

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
		
		public static Language getGuildLanguage(Plugin plugin, Guild guild) {
			return Language.valueOf(new GuildDB(plugin, guild).get("language"));	
		}
	}
	
	private Language lang;
	private JsonObject object;
	private JsonArray array;
	private String langfile;
	
	public LanguageManager(Plugin plugin, Guild guild) {
		new LanguageManager(plugin, Language.getGuildLanguage(plugin, guild));
	}
	
	public LanguageManager(OusuPlugin plugin, Language lang) {
		new LanguageManager(plugin.getPlugin(), lang);
	}

	public LanguageManager(Plugin plugin, Language lang) {
		this.lang = lang;
		try {
			langfile = "file:" + plugin.getPluginPath().getAbsolutePath()  + "/language/" + lang.getFileName();
			InputStream in = new URL(langfile).openStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			JsonArray arr = new JsonParser().parse(reader).getAsJsonArray();
			JsonObject obj = arr.get(0).getAsJsonObject();
			object = obj;
			array = arr;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Language getLanguage() {
		return lang;
	}
	
	public String getString(String property, String key) {
		if (!object.has(property)) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property, newobject);
			FileUtils.writeWithGson(new File(langfile.substring(5)), array);
		}
		JsonObject ob = object.get(property).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property + "."+ key);
			FileUtils.writeWithGson(new File(langfile.substring(5)), newarray);
			return " "+property+"."+key;
		}
			return " " + ob.get(key).getAsString();
	}
	
	public String[] getStrings(String property, String key) {
		if (!object.has(property)) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property, newobject);
			FileUtils.writeWithGson(new File(langfile.substring(5)), array);
		}
		JsonObject ob = object.get(property).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property + "."+ key);
			FileUtils.writeWithGson(new File(langfile.substring(5)), newarray);
			return new String[]{" "+property+"."+key};
		}
			return ob.get(key).getAsString().replace("{l}", ";").split(";");
	}
	
	public String getString(Class<?> property, String key) {
		if (!object.has(property.getSimpleName())) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property.getSimpleName(), newobject);
			FileUtils.writeWithGson(new File(langfile.substring(5)), array);
		}
		JsonObject ob = object.get(property.getSimpleName()).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property.getSimpleName()).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property.getSimpleName() + "."+ key);
			FileUtils.writeWithGson(new File(langfile.substring(5)), newarray);
			return " "+property.getSimpleName()+"."+key;
		}
			return " " + ob.get(key).getAsString();
	}
	
	public static JsonArray jsonTemplate() {
		JsonArray a = new JsonArray();
		JsonObject ob1 = new JsonObject();
		JsonObject obj1 = new JsonObject();
		JsonObject obj2 = new JsonObject();
		obj1.addProperty("EXAMPLE_DESCRIPTION", "This command is a example");
		obj2.addProperty("PING_MESSAGE", "personal");
		ob1.add("CommandDescription", obj1);
		ob1.add("Example", obj2);
		a.add(ob1);
		return a;
	}
}
