package me.skiincraft.discord.core.objects;

public class PluginConfig {

	private DiscordInfo discordInfo;
	private String botName;
	private String botMain;
	private String fileName;

	public PluginConfig(DiscordInfo discordInfo, String botname, String botMain, String filename) {
		this.discordInfo = discordInfo;
		this.botName = botname;
		this.botMain = botMain;
		this.fileName = filename;
	}

	public String getBotName() {
		return botName;
	}

	public String getBotMain() {
		return botMain;
	}

	public String getFileName() {
		return fileName;
	}

	public DiscordInfo getDiscordInfo() {
		return discordInfo;
	}
	
}
