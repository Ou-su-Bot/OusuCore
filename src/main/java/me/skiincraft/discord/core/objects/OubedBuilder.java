package me.skiincraft.discord.core.objects;

import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;


public class OubedBuilder extends EmbedBuilder {
	
	public OubedBuilder(String title, String description) {
		setTitle(title);
		setDescription(description);
	}
	
	public OubedBuilder(String title, String... description) {
		setTitle(title);
		int i = 0;
		for (String desc : description) {
			if (i == 0) { setDescription(desc); continue;}
			appendDescription("\n" + desc);
		}
	}
	
	public void setDescription(String... strings){
		int i = 0;
		for (String desc : strings) {
			if (i == 0) { setDescription(desc); continue;}
			appendDescription("\n" + desc);
		}
		return;
	}
	
	public void setDescription(List<String> strings){
		int i = 0;
		for (String desc : strings) {
			if (i == 0) { setDescription(desc); continue;}
			appendDescription("\n" + desc);
		}
		return;
	}

	public void applyWarning(RandomThumbnail randomthumbnail) {
		if (randomthumbnail != null)
			setThumbnail(randomthumbnail.getRandomThumbnail());

		setColor(Color.RED);
	}
	
	public void applyWarning() {
		applyWarning(null);
	}
	
	public void applySoftWarning(RandomThumbnail randomthumbnail) {
		if (randomthumbnail != null)
			setThumbnail(randomthumbnail.getRandomThumbnail());
		setColor(new Color(222, 74, 0));// Orange+-
	}
	
	public void applySoftWarning() {
		applySoftWarning(null);
	}
	
	public void applyHelp(RandomThumbnail randomthumbnail) {
		if (randomthumbnail != null)
			setThumbnail(randomthumbnail.getRandomThumbnail());
		setColor(Color.YELLOW);
	}
	
	public void applyHelp() {
		applyHelp(null);
	}

	public void applyInfo(RandomThumbnail randomthumbnail) {
		if (randomthumbnail != null)
			setThumbnail(randomthumbnail.getRandomThumbnail());
		setColor(new Color(158, 158, 158));// Cinza
	}
	
	
	public void applyInfo() {
		applyInfo(null);
	}

	public void applyReply(RandomThumbnail randomthumbnail) {
		if (randomthumbnail != null)
			setThumbnail(randomthumbnail.getRandomThumbnail());
		setColor(Color.YELLOW);
	}
	
	public void applyReply() {
		applyReply(null);
	}
	
}
