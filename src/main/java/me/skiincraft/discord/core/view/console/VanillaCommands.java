package me.skiincraft.discord.core.view.console;

import me.skiincraft.discord.core.view.console.commands.ExitCommand;

public final class VanillaCommands {
	
	public static void registerVanillaCommands() {
		ConsoleListener.registerCommand(new ExitCommand());
	}
	
}
