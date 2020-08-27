package me.skiincraft.discord.core.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Locale;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.entities.Guild;

public class LanguageManager {

	private Language lang;
	private JsonObject object;
	private JsonArray array;
	private String langfile;
	
	public LanguageManager(Guild guild) {
		this(Language.getGuildLanguage(guild));
	}

	public LanguageManager(Language lang) {
		this.lang = lang;
		try {
			langfile = "file:" + PluginManager.getPluginManager().getPlugin().getPluginPath().getAbsolutePath()  + "/language/" + lang.getFileName();
			InputStream in = new URL(langfile).openStream();
			
			JsonArray arr = new JsonParser().parse(new InputStreamReader(in)).getAsJsonArray();
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
	
	
	void write(JsonArray jsonarray){
		FileWriter io = null;
		try {
			io = new FileWriter(new File(langfile.substring(5)));
			io.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonarray));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				io.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public String getString(String property, String key) {
		if (!object.has(property)) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property, newobject);
			
			write(array);
		}
		JsonObject ob = object.get(property).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property + "."+ key);
			write(array);
			return " "+property+"."+key;
		}
			try {
				return " " + new String(ob.get(key).getAsString().getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	public String[] getStrings(String property, String key) {
		if (!object.has(property)) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property, newobject);
			write(array);
		}
		JsonObject ob = object.get(property).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property + "."+ key);
			write(array);
			return new String[]{" "+property+"."+key};
		}
			try {
				return new String(ob.get(key).getAsString().getBytes(), "UTF-8").replace("{l}", ";").split(";");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	public String getString(Class<?> property, String key) {
		return getString(property.getSimpleName(), key);
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
	
	public enum Language {
		Portuguese("PT_BR.json", "BR", new Locale("pt", "BR")),
		English("EN_US.json", "EN", new Locale("en", "US"));

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

}
