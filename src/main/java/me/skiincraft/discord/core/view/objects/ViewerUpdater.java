package me.skiincraft.discord.core.view.objects;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;

public class ViewerUpdater extends TimerTask {
	
	private Timer timer;
	
	public ViewerUpdater(Timer timer) {
		this.timer = timer;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public void run() {
		Platform.runLater(()->{
			ViewUtils.updateTab();
		});
	}

}
