package me.skiincraft.discord.core.prompt;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ConsoleAction {

    private String command;
    private List<String> aliases;
    private String usage;

    public ConsoleAction(@Nonnull String command, @Nullable List<String> aliases, @Nonnull String usage) {
        this.command = command;
        this.aliases = aliases;
        this.usage = usage;
    }

    public String getCommand() {
        return command;
    }

    @Nullable
    public List<String> getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    abstract boolean execute(String label, String[] args, ConsoleApplication console);

    public static ConsoleAction generateOf(@Nonnull String command, Action action){
        return generateOf(command,null, command, action);
    }

    public static ConsoleAction generateOf(@Nonnull String command, String usage, Action action){
        return generateOf(command,null, usage, action);
    }

    public static ConsoleAction generateOf(@Nonnull String command, List<String> aliases, Action action){
        return generateOf(command,aliases, command, action);
    }

    public static ConsoleAction generateOf(@Nonnull String command, @Nullable List<String> aliases, @Nonnull String usage, Action action) {
        return new ConsoleAction(command, aliases, usage) {
            @Override
            boolean execute(String label, String[] args, ConsoleApplication console) {
                return action.execute(label, args, console);
            }
        };
    }

    @FunctionalInterface
    public interface Action {
        boolean execute(String label, String[] args, ConsoleApplication console);
    }
}
