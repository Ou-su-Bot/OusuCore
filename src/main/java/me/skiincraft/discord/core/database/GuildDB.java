package me.skiincraft.discord.core.database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.sqlite.Database;
import me.skiincraft.discord.core.sqlobjects.DBObject;
import net.dv8tion.jda.api.entities.Guild;

public class GuildDB extends Database {

	private Guild guild;
	
	public GuildDB(Guild guild) {
		super(PluginManager.getPluginManager().getPlugin());
		this.guild = guild;
	}

	@Override
	public DBObject dbObject() {
		return new DBObject("guildid", guild.getId());
	}
	
	@Override
	public String databaseName() {
		return "servidores";
	}

	private String generatelang() {
		if (guild.getRegionRaw().contains("brazil")) {
			return "Portuguese";
		}
		if (guild.getRegionRaw().contains("us")) {
			return "English";
		}
		return "English";
	}
	
	@Override
	public Map<String, String> tableValues() {
		Map<String, String> table = new HashMap<String, String>();
		table.put("guildid", guild.getId());
		table.put("nome", guild.getName()
				.replace("'", "")
		        .replace("`", "")
		        .replace("´", ""));
		
		table.put("membros", "" + guild.getMemberCount());
		table.put("prefix", getPlugin().getDiscordInfo().getDefaultPrefix());
		Date newDate = new Date(TimeUnit.SECONDS.toMillis(guild.getSelfMember().getTimeJoined().toEpochSecond()));
		String simple = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(newDate);
		table.put("adicionado em", simple);
		table.put("language", generatelang());
		
		return table;
	}
}
