package me.skiincraft.discord.core.plugin;

import me.skiincraft.discord.core.OusuCore;
import net.dv8tion.jda.api.sharding.ShardManager;

public abstract class OusuPlugin {

	public void onLoad() {}
	public void onEnable() {}
	public void onDisable() {}

	public final ShardManager getShardManager() {
		return OusuCore.getShardManager();
	}
}
