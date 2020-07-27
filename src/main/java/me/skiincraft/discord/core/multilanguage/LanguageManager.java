package me.skiincraft.discord.core.multilanguage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
		this.lang = Language.getGuildLanguage(guild);
		try {
			langfile = "file:" + PluginManager.getPluginManager().getPlugin().getPluginPath().getAbsolutePath()  + "/language/" + lang.getFileName();
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

	public LanguageManager(Language lang) {
		this.lang = lang;
		try {
			langfile = "file:" + PluginManager.getPluginManager().getPlugin().getPluginPath().getAbsolutePath()  + "/language/" + lang.getFileName();
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
			return " " + ob.get(key).getAsString();
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
			return ob.get(key).getAsString().replace("{l}", ";").split(";");
	}
	
	public String getString(Class<?> property, String key) {
		if (!object.has(property.getSimpleName())) {
			JsonObject newobject = new JsonObject();
			newobject.addProperty(key, "notfoundtranslate: " + property + "."+ key);
			array.get(0).getAsJsonObject().add(property.getSimpleName(), newobject);
			write(array);
		}
		JsonObject ob = object.get(property.getSimpleName()).getAsJsonObject();
		
		if (!ob.has(key)) {
			JsonArray newarray = array;
			newarray.get(0).getAsJsonObject().get(property.getSimpleName()).getAsJsonObject().addProperty(key, "notfoundtranslate: " + property.getSimpleName() + "."+ key);
			write(array);
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
