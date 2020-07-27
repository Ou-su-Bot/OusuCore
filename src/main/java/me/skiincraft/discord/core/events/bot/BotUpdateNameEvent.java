package me.skiincraft.discord.core.events.bot;

import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.self.SelfUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateNameEvent;

public class BotUpdateNameEvent extends BotEvent {

	private SelfUser selfUser;
	private String newName;
	private String oldName;
	private String newDiscriminator;
	private String oldDiscriminator;
	
	private int eventType;
	
	public BotUpdateNameEvent(SelfUpdateNameEvent e) {
		this.selfUser = e.getSelfUser();
		this.newName = e.getNewName();
		this.oldName = e.getOldName();
		this.newDiscriminator = e.getSelfUser().getDiscriminator();
		this.oldDiscriminator = e.getSelfUser().getDiscriminator();
		this.eventType = 0;
	}
	
	public BotUpdateNameEvent(SelfUpdateDiscriminatorEvent e) {
		this.selfUser = e.getSelfUser();
		this.newName = e.getSelfUser().getName();
		this.oldName = e.getSelfUser().getName();
		this.newDiscriminator = e.getNewDiscriminator();
		this.oldDiscriminator = e.getOldDiscriminator();
		this.eventType = 1;
	}
	
	public String getNewDiscriminator() {
		return newDiscriminator;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public String getOldDiscriminator() {
		return oldDiscriminator;
	}
	
	public String getOldName() {
		return oldName;
	}
	
	public boolean isDiscriminatorUpdate(){
		return (eventType == 1) ? true : false;
	}
	
	public boolean isNameUpdate(){
		return (eventType == 0) ? true : false;
	}
	
	public SelfUser getSelfUser() {
		return selfUser;
	}
	
}
