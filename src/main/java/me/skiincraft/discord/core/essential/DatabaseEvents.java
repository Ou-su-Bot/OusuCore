package me.skiincraft.discord.core.essential;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.sqlite.GuildsDB;
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
		GuildsDB db = new GuildsDB(plugin, event.getGuild());
		db.create();
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		GuildsDB db = new GuildsDB(plugin, event.getGuild());
		if (db.exists()) {
			db.delete();	
		}
	}

}
