package me.skiincraft.discord.core.view.objects;

import java.util.List;
import java.util.Timer;

import javafx.concurrent.Task;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.view.editor.ViewContent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.sharding.ShardManager;

public class ViewUtils {
	
	public static void updateTab(Timer timer) {
		Task<Void> task = new Task<Void>() {

			protected Void call() throws Exception {
				Plugin plugin = PluginManager.getPluginManager().getPlugin();
				if (plugin == null) {
					return null;
				}
				
				if (!plugin.isRunning()) {
					return null;
				}
				
				if (plugin.getShardManager().getShardCache().size() == 0) {
					return null;
				}
				ShardManager sm = plugin.getShardManager();
				ViewContent view = OusuCore.getOusuViewer().getViewerContent();
				JDA bot = plugin.getShardManager().getShardCache().getElementById(0);
				SelfUser self = bot.getSelfUser();
				
				view.getTab1().setDisable(false);
				view.getInfoPane().setBotName(self.getName());
				view.getInfoPane().setBotDiscriminator("#"+ self.getDiscriminator());
				
				view.getInfoPane().setDefaultPrefix(plugin.getDiscordInfo().getDefaultPrefix());
				view.getAvatarPane().setAvatarPreview(ImageUtils.getDiscordAssets(self.getAvatarUrl()));
				
				List<Guild> str = sm.getGuildCache().asList();
				view.getSearchPane().setSearchList(str);
				view.getOptionPane().setSelector(plugin.getShardManager().getShards());
				
				timer.cancel();
				return null;
			}
		};
		task.run();
	}
	
	public static void updateTab() {
		Task<Void> task = new Task<Void>() {

			protected Void call() throws Exception {
				Plugin plugin = PluginManager.getPluginManager().getPlugin();
				if (plugin == null) {
					return null;
				}
				
				if (!plugin.isRunning()) {
					return null;
				}
				
				if (plugin.getShardManager().getShardCache().size() == 0) {
					return null;
				}
				ShardManager sm = plugin.getShardManager();
				ViewContent view = OusuCore.getOusuViewer().getViewerContent();
				JDA bot = plugin.getShardManager().getShardCache().getElementById(0);
				SelfUser self = bot.getSelfUser();
				
				view.getTab1().setDisable(false);
				view.getInfoPane().setBotName(self.getName());
				view.getInfoPane().setBotDiscriminator("#"+ self.getDiscriminator());
				
				view.getInfoPane().setDefaultPrefix(plugin.getDiscordInfo().getDefaultPrefix());
				view.getAvatarPane().setAvatarPreview(ImageUtils.getDiscordAssets(self.getAvatarUrl()));
				
				List<Guild> str = sm.getGuildCache().asList();
				view.getSearchPane().setSearchList(str);
				view.getOptionPane().setSelector(plugin.getShardManager().getShards());
				return null;
			}
		};
		task.run();
	}

}
