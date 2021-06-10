package me.skiincraft.ousucore.events;

import me.skiincraft.ousucore.command.objecs.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import org.jetbrains.annotations.NotNull;

public class FindCommandEvent extends Event {

    private final Command command;
    private boolean cancel;

    public FindCommandEvent(Command command) {
        super(command.getMessage().getJDA(), 200);
        this.command = command;
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
}
