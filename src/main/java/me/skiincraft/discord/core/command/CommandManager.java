package me.skiincraft.discord.core.command;

import java.util.ArrayList;

public class CommandManager {

	private static CommandManager instance;

	private CommandManager() {
	}

	private final ArrayList<Command> commands = new ArrayList<>();
	
	public boolean registerCommand(Command command) {
		return commands.add(command);
	}
	
	public boolean unregisterCommand(Command command) {
		return commands.remove(command);
	}
	
	public boolean unregisterCommand(String command) {
		return commands.remove(commands.stream().filter(c -> c.getCommandName().equalsIgnoreCase(command)).findAny().orElse(null));
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}

	public static CommandManager getInstance() {
		return (instance == null) ? instance = new CommandManager() : instance;
	}
}
