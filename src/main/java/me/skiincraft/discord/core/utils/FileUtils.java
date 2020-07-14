package me.skiincraft.discord.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;

public class FileUtils {
	
	public static void writeWithGson(File file , Object data) {
		FileWriter fr = null;
		try {
			fr = new FileWriter(file);
			fr.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
