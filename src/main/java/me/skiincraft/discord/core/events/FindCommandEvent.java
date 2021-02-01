package me.skiincraft.discord.core.events;

import me.skiincraft.discord.core.command.objecs.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

public class FindCommandEvent implements GenericEvent {

    private final JDA jda;
    private final Command command;
    private boolean cancel;

    public FindCommandEvent(Command command, JDA jda) {
        this.command = command;
        this.jda = jda;
        this.cancel = false;
    }

    public void setCancelExecutor(boolean value){
        cancel = value;
    }

    public Command getCommand() {
        return command;
    }

    public boolean isCancel() {
        return cancel;
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public long getResponseNumber() {
        return 200;
    }
}
