package me.skiincraft.discord.core.objects;

import java.util.Random;

public class RandomThumbnail {
	
	private String[] urls;
	
	public RandomThumbnail(String... urls) {
		this.urls = urls;
	}
	
	public String[] getUrls() {
		return urls;
	}
	
	public String getUrl(int i) {
		return urls[(urls.length <= i) ? urls.length-1 : i];
	}
	
	public String getRandomThumbnail() {
		return urls[new Random().nextInt(urls.length-1)];
	}
	
	public String toString() {
		return getRandomThumbnail();
	}

}
