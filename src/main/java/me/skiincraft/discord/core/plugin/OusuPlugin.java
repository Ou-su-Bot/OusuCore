package me.skiincraft.discord.core.plugin;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.configuration.InicializeOptions;
import net.dv8tion.jda.api.sharding.ShardManager;

public abstract class OusuPlugin {

	public void onLoad(InicializeOptions options) {}
	public void onEnable() {}
	public void onDisable() {}

	public final ShardManager getShardManager() {
		return OusuCore.getShardManager();
	}
}
