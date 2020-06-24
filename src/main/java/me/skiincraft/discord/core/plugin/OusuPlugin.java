package me.skiincraft.discord.core.plugin;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.reflect.FieldUtils;

import me.skiincraft.discord.core.exception.OusuPluginNotLoadedException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public abstract class OusuPlugin {
	
	private Plugin plugin;
	private ShardManager shmanager;
	private DefaultShardManagerBuilder builder;
	
	
	public void onLoad() {
		
	}
	
	public void onEnable() {
		ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("pt", "BR"));
		throw new OusuPluginNotLoadedException(bundle.getString("notloaded"), bundle.getString("notloaded.reason"));
	}
	
	public void onDisable() {
		
	}
	
	public final void startbot() throws LoginException, IllegalArgumentException, IllegalAccessException {
		try {
			this.shmanager = builder.build();
		} catch (LoginException | IllegalArgumentException e) {
			FieldUtils.writeField(plugin, "running", false, true);
			e.printStackTrace();
			return;
		}
		System.out.println("Esperando todas as shards carregarem...");
		for (JDA jda : shmanager.getShards()) {
			try {
				jda.awaitReady();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
			
		System.out.println("Todas as shards foram carregadas.");
		FieldUtils.writeField(plugin, "running", true, true);
	}
	
	public final ShardManager getShardManager() {
		return shmanager;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
