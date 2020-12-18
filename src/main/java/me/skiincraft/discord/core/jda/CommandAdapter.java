package me.skiincraft.discord.core.jda;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.Command;
import me.skiincraft.discord.core.command.InteractChannel;
import me.skiincraft.discord.core.configuration.GuildDB;
import me.skiincraft.discord.core.events.member.PreCommandEvent;
import me.skiincraft.discord.core.utils.StringUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandAdapter extends ListenerAdapter {

	private final ExecutorService commandExecutor;

	public CommandAdapter() {
		this.commandExecutor = Executors.newFixedThreadPool(10,
				new ThreadFactoryBuilder()
						.setNameFormat("Ou!su Commands ").build());
	}

	public Command getCommand(final ArrayList<Command> list, final String name){
	    return list.stream().filter(o -> o.getCommandName().equalsIgnoreCase(name))
				.findAny()
				.orElse(getCommandByAliase(list, name));
	}
	
	public Command getCommandByAliase(final ArrayList<Command> list, final String name){
		return list.stream().filter(c -> c.getAliases() != null && c.getAliases().stream().anyMatch(o -> o.equalsIgnoreCase(name)))
				.findFirst()
				.orElse(null);
	}
	
	private Command validateCommand(GuildMessageReceivedEvent e, String[] args) {
		if (e.getAuthor().isBot() || !e.getChannel().canTalk() || args.length == 0) {
			return null;
		}

		ArrayList<Command> commands = OusuCore.getCommandManager().getCommands();
		String prefix = new GuildDB(e.getGuild()).get("prefix");
		
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
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");
		Command command = validateCommand(event, args);
		if (Objects.isNull(command)){
			return;
		}
		PreCommandEvent preCommandEvent = new PreCommandEvent(event.getMessage(), event.getMember(), event.getChannel(), command);
		OusuCore.callEvent(preCommandEvent);
		if (preCommandEvent.isCancelled()){
			return;
		}
		commandExecutor.execute(() -> {
			event.getChannel().sendTyping().queue();
			OusuCore.getLogger().log(Level.getLevel("COMMAND"), String.format("%s executou o comando %s. [%s]",
					event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(),
					command.getCommandName(), String.join(" ", args)));

			command.execute(event.getMember(), StringUtils.removeString(args, 0), new InteractChannel(event.getChannel()));
	});
	}
}
