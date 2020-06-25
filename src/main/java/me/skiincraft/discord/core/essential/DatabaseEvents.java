package me.skiincraft.discord.core.essential;

import me.skiincraft.discord.core.database.GuildDB;
import me.skiincraft.discord.core.plugin.Plugin;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DatabaseEvents extends ListenerAdapter {
	
	private Plugin plugin;
	
	public DatabaseEvents(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		GuildDB db = new GuildDB(plugin, event.getGuild());
		db.create();
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		GuildDB db = new GuildDB(plugin, event.getGuild());
		if (db.exists()) {
			db.delete();	
		}
	}

}
