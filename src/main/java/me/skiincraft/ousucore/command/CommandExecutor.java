package me.skiincraft.ousucore.command;

import me.skiincraft.ousucore.command.utils.ChannelUtils;
import me.skiincraft.ousucore.command.utils.CommandTools;

import java.util.Collections;
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

    public abstract void execute(String label, String[] args, CommandTools channels) throws Exception;

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return (aliases == null) ? Collections.emptyList() : aliases;
    }

    public String getUsage() {
        return usage;
    }
}
