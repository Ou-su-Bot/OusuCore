package me.skiincraft.discord.core.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.discord.core.OusuCore;
import net.dv8tion.jda.api.entities.Guild;

public class LanguageManager {

	private final Language lang;
	private JsonObject object;
	private JsonArray array;
	private String langFile;
	
	public LanguageManager(Guild guild) {
		this(Language.getGuildLanguage(guild));
	}

	public LanguageManager(Language language) {
		this.lang = language;
		try {
			langFile = "file:" + OusuCore.getPluginPath().toFile().getAbsolutePath()  + "/language/" + lang.getLanguageCode() + "_" + lang.getCountryCode() + ".json";
			InputStream in = new URL(langFile).openStream();
			
			JsonArray arr = new JsonParser().parse(new InputStreamReader(in)).getAsJsonArray();
			object = arr.get(0).getAsJsonObject();
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
			io = new FileWriter(new File(langFile.substring(5)));
			io.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonarray));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				assert io != null;
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
		return " " + new String(ob.get(key).getAsString().getBytes(), StandardCharsets.UTF_8).replace("{l}", "\n");
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
		return new String(ob.get(key).getAsString().getBytes(), StandardCharsets.UTF_8).replace("{l}", ";").split(";");
	}
	
	public String getString(Class<?> property, String key) {
		return getString(property.getSimpleName(), key);
	}
}
