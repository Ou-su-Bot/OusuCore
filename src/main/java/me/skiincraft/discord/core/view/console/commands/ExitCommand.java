package me.skiincraft.discord.core.view.console.commands;

import java.util.Arrays;
import javafx.application.Platform;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.view.console.ConsoleCommand;

public class ExitCommand extends ConsoleCommand{

	public ExitCommand() {
		super("exit", Arrays.asList("sair"), "Close the OusuCore.", false);
	}

	public void execute(String[] args, Plugin interacted) {
		System.out.println("Closing...");
		Platform.exit();
		System.exit(OusuCore.EXIT_FORCEEXIT);
	}

}
