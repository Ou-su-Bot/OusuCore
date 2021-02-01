package me.skiincraft.ousucore.plugin;

import me.skiincraft.ousucore.OusuCore;
import me.skiincraft.ousucore.configuration.InicializeOptions;
import net.dv8tion.jda.api.sharding.ShardManager;

public abstract class OusuPlugin {

	public void onLoad(InicializeOptions options) {}
	public void onEnable() {}
	public void onDisable() {}

	public final ShardManager getShardManager() {
		return OusuCore.getShardManager();
	}
}
