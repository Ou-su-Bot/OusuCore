package me.skiincraft.discord.core.command;

import java.io.InputStream;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ContentMessage {
	
	private final Message message;
	
	private final InputStream inputStream;
	private String inputName;
	private final String inputExtension;
	
	public ContentMessage(String string, InputStream inputStream, String inputExtension) {
		this.message = new MessageBuilder(string).build();
		this.inputStream = inputStream;
		this.inputExtension = "." + inputExtension.replace(".", "");
	}
	
	public ContentMessage(MessageEmbed string, InputStream inputStream, String inputExtension) {
		this.message = new MessageBuilder(string).build();
		this.inputStream = inputStream;
		this.inputExtension = "." + inputExtension.replace(".", "");
	}
	
	public boolean isEmbedMessage() {
		return (message.getEmbeds().size() != 0);
	}
	
	public MessageEmbed getMessageEmbed() {
		return (isEmbedMessage()) ? message.getEmbeds().get(0) : null;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public String getInputExtension() {
		return inputExtension;
	}
	
	public String getInputName() {
		return inputName;
	}
	
	public ContentMessage setInputName(String inputName) {
		this.inputName = inputName;
		return this;
	}
}
