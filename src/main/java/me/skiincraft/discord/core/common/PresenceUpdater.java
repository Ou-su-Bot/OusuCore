package me.skiincraft.discord.core.common;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.skiincraft.discord.core.OusuCore;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PresenceUpdater {

	private final Timer timer;
	private final ShardManager shardManager;
	private final Updater updater;

	public PresenceUpdater(List<Activity> activities) {
		shardManager = OusuCore.getShardManager();
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

		private final List<Activity> messages;
		private final PresenceUpdater presenceUpdater;
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
