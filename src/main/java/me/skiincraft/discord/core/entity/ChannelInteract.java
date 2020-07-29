package me.skiincraft.discord.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import me.skiincraft.discord.core.events.bot.BotSendMessage;
import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class ChannelInteract {
	
	protected abstract MessageChannel getTextChannel();
	
	private Consumer<Message> eventConsumer(){return eventConsumer(null);};
	private Consumer<Message> eventConsumer(List<Message> messageList){
		return con ->{
			PluginManager.getPluginManager().getEventManager()
			.callEvent(new BotSendMessage((TextChannel) con.getChannel(), con, con.getJDA().getSelfUser()));
			if (messageList != null)messageList.add(con);
		};
	}
	private Thread queueThread(List<Message> messages, Consumer<Message> consumer) {
		return new Thread(()-> {
			int loop = 0;
			while (messages.size() == 0) {
				if (loop >= 15) {
					return;
				}
				try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loop++;
			}
			
			Message con = messages.get(0);
			consumer.accept(con);
		}, "Bot-QueueReply");
	}
	
	public void reply(Message message) {
		getTextChannel().sendMessage(message).queue(eventConsumer());
	}
	
	public void reply(ContentMessage message) {
		String name = "c0r3-" + new Random().nextInt(400);
		if (message.isEmbedMessage()) {
			EmbedBuilder embedMessage = new EmbedBuilder(message.getMessageEmbed());
			embedMessage.setImage("attachment://" + name + message.getInputExtension());
			System.out.println("Messagem embed");
			getTextChannel().sendMessage(embedMessage.build())
					.addFile(message.getInputStream(), name + message.getInputExtension())
					.queue(eventConsumer());
		} else {
			getTextChannel().sendMessage(message.getMessage())
					.addFile(message.getInputStream(), name + message.getInputExtension())
					.queue(eventConsumer());
		}
	}
	
	public void reply(String message) {
		this.reply(new MessageBuilder(message).build());
	}
	
	public void reply(MessageEmbed message) {
		this.reply(new MessageBuilder(message).build());
	}
	
	public void reply(Message message, Consumer<Message> afterSucefull) {
		List<Message> messageAfter = new ArrayList<>();
		getTextChannel().sendMessage(message).queue(eventConsumer(messageAfter));
		queueThread(messageAfter, afterSucefull);
	}
	
	public void reply(String message, Consumer<Message> afterSucefull) {
		this.reply(new MessageBuilder(message).build(), afterSucefull);
	}
	
	public void reply(MessageEmbed message, Consumer<Message> afterSucefull) {
		this.reply(new MessageBuilder(message).build(), afterSucefull);
	}
	
	public void reply(ContentMessage message, Consumer<Message> afterSucefull) {
		List<Message> messageAfter = new ArrayList<>();
		String name = "c0r3-" + new Random().nextInt(400);
		if (message.isEmbedMessage()) {
			EmbedBuilder embedMessage = new EmbedBuilder(message.getMessageEmbed());
			embedMessage.setImage("attachment://" + name + message.getInputExtension());
			getTextChannel().sendMessage(embedMessage.build())
					.addFile(message.getInputStream(), name + message.getInputExtension())
					.queue(eventConsumer(messageAfter));
		} else {
			getTextChannel().sendMessage(message.getMessage())
					.addFile(message.getInputStream(), name + message.getInputExtension())
					.queue(eventConsumer(messageAfter));
		}
		
		queueThread(messageAfter, afterSucefull);
	}

}
