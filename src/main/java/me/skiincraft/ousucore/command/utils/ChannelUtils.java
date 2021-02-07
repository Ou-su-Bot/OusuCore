package me.skiincraft.ousucore.command.utils;

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
        MessageConsumer messageConsumer = new MessageConsumer(Thread.currentThread());
        try {
            channel.sendMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(Message message, ThrowableConsumer<CommandMessage> consumer) {
        MessageConsumer messageConsumer = new MessageConsumer(Thread.currentThread());
        try {
            channel.sendMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(MessageEmbed message, ThrowableConsumer<CommandMessage> consumer) {
        MessageConsumer messageConsumer = new MessageConsumer(Thread.currentThread());
        try {
            channel.sendMessage(message).queue(messageConsumer, messageConsumer.ifException());
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }
    }

    public void reply(ContentMessage message, ThrowableConsumer<CommandMessage> consumer) {
        MessageConsumer messageConsumer = new MessageConsumer(Thread.currentThread());
        try {
            if (message.isEmbedMessage()) {
                EmbedBuilder embedBuilder = new EmbedBuilder(message.getMessageEmbed());
                embedBuilder.setImage("attachment://" + message.getInputName() + message.getInputExtension());
                channel.sendMessage(embedBuilder.build()).addFile(message.getInputStream(), message.getInputName() + message.getInputExtension())
                        .queue(messageConsumer, messageConsumer.ifException());
            } else {
                channel.sendMessage(message.getMessage()).addFile(message.getInputStream(), message.getInputName() + message.getInputExtension())
                        .queue(messageConsumer, messageConsumer.ifException());
            }
            if (consumer == null)
                return;

            messageConsumer.waitConsumer();
            if (messageConsumer.error())
                throw messageConsumer.getThrowable();

            consumer.accept(new CommandMessage(messageConsumer.get()));
        } catch (Throwable e) {
            throw new ThrowableConsumerException(e);
        }


    }

    public TextChannel getChannel() {
        return channel;
    }

    public static class MessageConsumer extends ThreadConsumer<Message>{

        private Throwable throwable;

        public MessageConsumer(Thread mainThread) {
            super(mainThread);
        }

        public boolean error(){
            return throwable != null;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public Consumer<Throwable> ifException() {
            return (err)-> this.throwable = err;
        }

        @Override
        public void waitConsumer() {
            while (object.get() == null){
                if (object.get() != null || error()) break;
            }
        }
    }
}
