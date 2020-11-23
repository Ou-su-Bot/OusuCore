package me.skiincraft.discord.core.command;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.events.bot.BotSendMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class InteractChannel {

	private final TextChannel messageChannel;

	public InteractChannel(TextChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

	public TextChannel getTextChannel() {
		return messageChannel;
	}

	private Consumer<Message> eventConsumer(){return eventConsumer(null);}

	private Consumer<Message> eventConsumer(AtomicReference<Message> message){
		return con ->{
			OusuCore.getEventManager().callEvent(new BotSendMessage((TextChannel) con.getChannel(), con, con.getJDA().getSelfUser()));
			if (message != null && message.get() == null) message.set(con);
		};
	}

	private Thread queueThread(AtomicReference<Message> message, Consumer<Message> consumer) {
		return new Thread(()-> {
			final long currentTime = System.currentTimeMillis();
			while (message.get() == null) {
				if ((System.currentTimeMillis() - currentTime) >= 3200){
					OusuCore.getLogger().warn("Não foi possivel completar uma mensagem (consumer)");
					return;
				}
			}
			
			Message con = message.get();
			consumer.accept(con);
		}, "Bot-Replying");
	}
	
	public void reply(Message message) {
		getTextChannel().sendMessage(message).queue(eventConsumer());
	}
	
	public void reply(ContentMessage message) {
		String name = message.getInputName() == null ? "c0r3-" + new Random().nextInt(400) : message.getInputName();
		if (message.isEmbedMessage()) {
			EmbedBuilder embedMessage = new EmbedBuilder(message.getMessageEmbed());
			embedMessage.setImage("attachment://" + name + message.getInputExtension());
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
		AtomicReference<Message> messageAfter = new AtomicReference<>();
		MessageAction action = getTextChannel()
				.sendMessage(message);
		action.queue(eventConsumer(messageAfter));
		
		Thread queue = queueThread(messageAfter, afterSucefull);
		queue.start();
	}
	
	public void reply(String message, Consumer<Message> afterSucefull) {
		this.reply(new MessageBuilder(message).build(), afterSucefull);
	}
	
	public void reply(MessageEmbed message, Consumer<Message> afterSucefull) {
		this.reply(new MessageBuilder(message).build(), afterSucefull);
	}
	
	public void reply(ContentMessage message, Consumer<Message> afterSucefull) {
		AtomicReference<Message> messageAfter = new AtomicReference<>();
		String name = message.getInputName() == null ? "c0r3-" + new Random().nextInt(400) : message.getInputName();
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
		
		queueThread(messageAfter, afterSucefull).start();
	}
}
