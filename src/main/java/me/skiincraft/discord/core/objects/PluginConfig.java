package me.skiincraft.discord.core.objects;

public class PluginConfig {

	private DiscordInfo discordInfo;
	private String botname;
	private String bot_main;
	private String filename;

	public PluginConfig(DiscordInfo discordInfo, String botname, String bot_main, String filename) {
		this.discordInfo = discordInfo;
		this.botname = botname;
		this.bot_main = bot_main;
		this.filename = filename;
	}

	public String getBotname() {
		return botname;
	}

	public String getBot_main() {
		return bot_main;
	}

	public String getFilename() {
		return filename;
	}

	public DiscordInfo getDiscordInfo() {
		return discordInfo;
	}
	
}
