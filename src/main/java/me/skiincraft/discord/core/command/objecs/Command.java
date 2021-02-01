package me.skiincraft.discord.core.command.objecs;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.CommandExecutor;
import me.skiincraft.discord.core.command.ICommandManager;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;

public class Command {

    private final String name;
    private final String[] args;
    private final Message message;
    private final ICommandManager commandManager;
    private CommandExecutor executor;

    public Command(String name, String[] args, Message message, ICommandManager commandManager) {
        this.name = name;
        this.args = args;
        this.message = message;
        this.commandManager = commandManager;
    }

    public Command(String[] allargs, Message message, ICommandManager commandManager) {
        this.name = allargs[0];
        this.args = Arrays.copyOfRange(allargs, 1, allargs.length);
        this.message = message;
        this.commandManager = commandManager;
    }


    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }

    public Message getMessage() {
        return message;
    }

    public CommandExecutor getCommandExecutor() {
        return searchCommandExecutor();
    }

    private CommandExecutor searchCommandExecutor() {
        if (executor != null)
            return executor;
        List<CommandExecutor> executors = commandManager.getCommands((cmd) -> cmd.getName().equalsIgnoreCase(getName()) || cmd.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(cmd.getName())));
        if (executors.size() == 0){
            return null;
        }
        if (executors.size() > 1){
            OusuCore.getLogger().warn(String.format("Está havendo um conflito entre comandos com name/aliases iguais. [%s]", executors));
        }
        return executor = executors.get(0);
    }
}
