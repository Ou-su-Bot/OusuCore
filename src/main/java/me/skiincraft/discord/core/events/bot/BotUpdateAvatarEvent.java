package me.skiincraft.discord.core.events.bot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import me.skiincraft.discord.core.utils.ImageUtils;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent;

public class BotUpdateAvatarEvent extends BotEvent {

	private SelfUser selfUser;
	private String newAvatarUrl;
	private String newAvatarId;
	
	public BotUpdateAvatarEvent(SelfUpdateAvatarEvent e) {
		selfUser = e.getSelfUser();
		newAvatarUrl = e.getNewAvatarUrl();
		newAvatarId = e.getNewAvatarUrl();
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
	public String getNewAvatarId() {
		return newAvatarId;
	}
	
	public String getNewAvatarUrl() {
		return newAvatarUrl;
	}
	
	public InputStream getAvatarStream() throws IOException {
		return ImageUtils.toInputStream(getNewAvatarUrl());
	}
	
	public BufferedImage getAvatarImage() throws IOException {
		return ImageIO.read(getAvatarStream());
	}

}
