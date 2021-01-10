package me.skiincraft.discord.core.jda;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.repository.OusuGuild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CommandAdapter {

	private final ExecutorService commandExecutor;

	public CommandAdapter() {
		this.commandExecutor = Executors.newFixedThreadPool(10,
				new ThreadFactoryBuilder()
						.setNameFormat("Ou!su Commands").build());
	}

	public Command getCommand(final ArrayList<Command> list, final String name){
	    return list.stream().filter(o -> o.getCommandName().equalsIgnoreCase(name))
				.findAny()
				.orElse(getCommandByAlias(list, name));
	}
	
	public Command getCommandByAlias(final ArrayList<Command> list, final String name){
		return list.stream().filter(c -> c.getAliases() != null && c.getAliases().stream().anyMatch(o -> o.equalsIgnoreCase(name)))
				.findFirst()
				.orElse(null);
	}
	
	private Command validateCommand(GuildMessageReceivedEvent e, String[] args) {
		if (e.getAuthor().isBot() || !e.getChannel().canTalk() || args.length == 0) {
			return null;
		}

		ArrayList<Command> commands = OusuCore.getCommandManager().getCommands();
		OusuGuild guild = OusuCore.getGuildRepository().getById(e.getGuild().getIdLong()).get();
		String prefix = guild.getPrefix();
		
		if (!args[0].toLowerCase().startsWith(prefix) || args[0].length() <= prefix.length()) {
			return null;
		}
		
		Command command = getCommand(commands, args[0].substring(prefix.length()));
		if (command == null) {
			return null;
		}
		
		if (!args[0].toLowerCase().startsWith(prefix.toLowerCase())) {
			return null;
		}
		
		if (commands.size() == 0) {
			return null;
		}
		return command;
	}

	@SubscribeEvent
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");
		Command command = validateCommand(event, args);
		if (Objects.isNull(command)){
			return;
		}
		commandExecutor.execute(() -> {
			event.getChannel().sendTyping().queue();
			OusuCore.getLogger().log(Level.getLevel("COMMAND"), String.format("%s executou o comando %s. [%s]",
					event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(),
					command.getCommandName(), String.join(" ", args)));

			List<String> args2 = Arrays.stream(args).collect(Collectors.toList());
			args2.remove(0);
			command.execute(event.getMember(), args2.toArray(new String[0]), new InteractChannel(event.getChannel()));
	});
	}
}
