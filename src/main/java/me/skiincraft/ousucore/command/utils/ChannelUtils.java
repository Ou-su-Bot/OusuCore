package me.skiincraft.ousucore.command.utils;

import me.skiincraft.ousucore.OusuCore;
import me.skiincraft.ousucore.command.objecs.CommandMessage;
import me.skiincraft.ousucore.command.objecs.ContentMessage;
import me.skiincraft.ousucore.exception.ThrowableConsumerException;
import me.skiincraft.ousucore.utils.ThrowableConsumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;

public class ChannelUtils {

    private TextChannel channel;

    public ChannelUtils(TextChannel channel){
        this.channel = channel;
    }

    public void reply(CharSequence message){
        reply(message, null);
    }

    public void reply(Message message){
        reply(message, null);
    }

    public void reply(MessageEmbed message){
        reply(message, null);
    }

    public void reply(ContentMessage message){
        reply(message, null);
    }

    public void reply(CharSequence message, ThrowableConsumer<CommandMessage> consumer) {
        ConsumerMessage consumerMessage = new ConsumerMessage();
        channel.sendMessage(message).queue(consumerMessage);
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(new CommandMessage(consumerMessage.getMessage()));
        } catch (Exception e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(Message message, ThrowableConsumer<CommandMessage> consumer) {
        ConsumerMessage consumerMessage = new ConsumerMessage();
        channel.sendMessage(message).queue(consumerMessage);
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(new CommandMessage(consumerMessage.getMessage()));
        } catch (Exception e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(MessageEmbed message, ThrowableConsumer<CommandMessage> consumer) {
        ConsumerMessage consumerMessage = new ConsumerMessage();
        channel.sendMessage(message).queue(consumerMessage);

        if (consumer == null){
            return;
        }
        try {
            consumer.accept(new CommandMessage(consumerMessage.getMessage()));
        } catch (Exception e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(ContentMessage message, ThrowableConsumer<CommandMessage> consumer) {
        ConsumerMessage consumerMessage = new ConsumerMessage();
        if (message.isEmbedMessage()){
            EmbedBuilder embedBuilder = new EmbedBuilder(message.getMessageEmbed());
            embedBuilder.setImage("attachment://" + message.getInputName() + message.getInputExtension());
            channel.sendMessage(embedBuilder.build()).addFile(message.getInputStream(), message.getInputName() + message.getInputExtension())
                    .queue(consumerMessage);
        } else {
            channel.sendMessage(message.getMessage()).addFile(message.getInputStream(), message.getInputName() + message.getInputExtension())
                    .queue(consumerMessage);
        }
        if (consumer == null){
            return;
        }
        try {
            consumer.accept(new CommandMessage(consumerMessage.getMessage()));
        } catch (Exception e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public TextChannel getChannel() {
        return channel;
    }

    public static class ConsumerMessage implements Consumer<Message> {

        private Message message;

        @Override
        public void accept(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            final long currentTime = System.currentTimeMillis();
            while (message == null) {
                if ((System.currentTimeMillis() - currentTime) >= 3200) {
                    OusuCore.getLogger().warn("Não foi possivel completar uma mensagem (consumer)");
                    break;
                }
            }
            return message;
        }
    }
}
