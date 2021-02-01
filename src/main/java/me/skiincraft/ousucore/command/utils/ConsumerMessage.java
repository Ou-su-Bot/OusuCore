package me.skiincraft.ousucore.command.utils;

import net.dv8tion.jda.api.entities.Message;

import java.util.function.Consumer;

public class ConsumerMessage implements Consumer<Message> {

    private Message message;

    @Override
    public void accept(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
