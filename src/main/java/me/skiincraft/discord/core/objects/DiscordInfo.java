package me.skiincraft.discord.core.objects;

public class DiscordInfo {
	
	private String botname;
	private long botId;
	private String token;
	private long ownerId;
	private int shards;
	private String defaultPrefix;
	
	public DiscordInfo(String botname, long botId, String token, long ownerId, int shards) {
		this.botname = botname;
		this.botId = botId;
		this.token = token;
		this.ownerId = ownerId;
		this.shards = shards;
	}
	public String getBotname() {
		return botname;
	}
	public long getBotId() {
		return botId;
	}
	public String getToken() {
		return token;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public int getTotalShards() {
		return shards;
	}
	
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	@Override
	public String toString() {
		return "DiscordInfo [botname=" + botname + ", botId=" + botId + ", token=" + token + ", ownerId=" + ownerId
				+ ", shards=" + shards + "]";
	}
	
	
}
