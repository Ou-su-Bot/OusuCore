package me.skiincraft.discord.core.command;

import me.skiincraft.discord.core.command.utils.ChannelUtils;

import java.util.List;

public abstract class CommandExecutor implements CommandOperation {

    protected final String name;
    protected final List<String> aliases;
    protected final String usage;

    public CommandExecutor(String name, List<String> aliases, String usage) {
        this.name = name;
        this.aliases = aliases;
        this.usage = usage;
    }

    public abstract void execute(String label, String[] args, ChannelUtils channels) throws Exception;

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }
}
