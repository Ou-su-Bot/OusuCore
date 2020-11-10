package me.skiincraft.discord.core.plugin;

import net.dv8tion.jda.api.sharding.ShardManager;

public abstract class OusuPlugin {

	private ShardManager shardManager;

	public void onLoad() {}
	public void onEnable() {}
	public void onDisable() {}

	public final ShardManager getShardManager() {
		return shardManager;
	}
}
