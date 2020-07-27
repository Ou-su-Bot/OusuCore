package me.skiincraft.discord.core.view.console.commands;

import java.util.Arrays;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.view.console.ConsoleCommand;
import me.skiincraft.discord.core.view.objects.ViewUtils;

public class ExitCommand extends ConsoleCommand{

	public ExitCommand() {
		super("i", Arrays.asList("i"), "Close the OusuCore.", false);
	}

	public void execute(String[] args, Plugin interacted) {
		ViewUtils.updateTab();
	}

}
