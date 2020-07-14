package me.skiincraft.discord.core.objects;

public class DiscordInfo {
	
	private String botname;
	private String defaultPrefix;
	private String token;
	private long botId;
	private long ownerId;
	private int shards;
	
	public DiscordInfo(String botname, String defaultprefix, String token, long botId, long ownerId, int shards) {
		this.botname = botname;
		this.botId = botId;
		this.token = token;
		this.ownerId = ownerId;
		this.shards = shards;
		this.defaultPrefix = defaultprefix;
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
		new OubedBuilder(null, "");
		return defaultPrefix;
	}

	@Override
	public String toString() {
		return "DiscordInfo [botname=" + botname + ", botId=" + botId + ", token=" + token + ", ownerId=" + ownerId
				+ ", shards=" + shards + "]";
	}
	
	
}
