package me.skiincraft.discord.core.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.skiincraft.discord.core.plugin.Plugin;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PresenceUpdater {

	private Timer timer;
	private ShardManager shardManager;
	private Updater updater;

	public PresenceUpdater(Plugin plugin, List<Activity> activities) {
		shardManager = plugin.getShardManager();
		timer = new Timer("Presence Timer");
		timer.scheduleAtFixedRate(updater = new Updater(this, activities), 0, TimeUnit.SECONDS.toMillis(90));
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public Timer getTimer() {
		return timer;
	}

	public Updater getUpdater() {
		return updater;
	}

	public static class Updater extends TimerTask {

		private List<Activity> messages;
		private PresenceUpdater presenceUpdater;
		int ordem;

		public Updater(PresenceUpdater main, List<Activity> activities) {
			this.presenceUpdater = main;
			this.messages = activities;
		}

		public void run() {
			if (ordem + 1 > messages.size()) {
				ordem = 0;
			}
			presenceUpdater.shardManager
					.setActivity(Activity.of(messages.get(ordem).getType(), keyReplace(messages.get(ordem).getName())));
			ordem++;
		}

		public PresenceUpdater getPresenceUpdater() {
			return presenceUpdater;
		}

		public List<Activity> getMessages() {
			return messages;
		}

		private String keyReplace(String message) {
			return message.replace("{guildsize}", "" + presenceUpdater.shardManager.getGuildCache().size())
					.replace("{prefix}", "ou!")
					.replace("{usersize}", "" + presenceUpdater.shardManager.getUserCache().size());
		}

	}

}
