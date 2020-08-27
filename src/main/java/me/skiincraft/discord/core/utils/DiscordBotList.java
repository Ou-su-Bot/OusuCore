package me.skiincraft.discord.core.utils;

import org.discordbots.api.client.DiscordBotListAPI;

public class DiscordBotList {
	
	private String token;
	private long botId;
	
	private DiscordBotListAPI api;
	
	public DiscordBotList(String token, long botId) {
		this.token = token;
		this.botId = botId;
		api = new DiscordBotListAPI
				.Builder()
				.token(token)
				.botId(botId+"")
				.build();
	}
	
	public void update(int guilds) {
		api.setStats(guilds);
	}
	
	public long getBotId() {
		return botId;
	}
	
	public String getToken() {
		return token;
	}
	
	public DiscordBotListAPI getAPI() {
		return api;
	}
}
