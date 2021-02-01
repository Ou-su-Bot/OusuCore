package me.skiincraft.discord.core.command.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.command.objecs.Command;
import me.skiincraft.discord.core.command.ICommandManager;
import me.skiincraft.discord.core.events.FindCommandEvent;
import me.skiincraft.discord.core.repository.OusuGuild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandParser {

    private final ICommandManager commandManager;
    private final ExecutorService service;

    public CommandParser(ICommandManager commandManager) {
        this.commandManager = commandManager;
        this.service = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("CommandParser")
                .build());
    }

    @SubscribeEvent
    public void onCommandMessage(GuildMessageReceivedEvent event){
        if (event.isWebhookMessage()){
            return;
        }
        User member = Objects.requireNonNull(event.getMember(), "user").getUser();
        if (member.isBot() || member.isFake() || event.getChannel().canTalk()){
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");
        String prefix = OusuCore.getGuildRepository().getById(event.getGuild().getIdLong())
                .map(OusuGuild::getPrefix)
                .orElse(OusuCore.getInternalSettings().getDefaultPrefix());

        if (args[0].startsWith(prefix) && args[0].length() != prefix.length()){
            service.execute(() -> {
                Command command = new Command(removePrefix(prefix, args), event.getMessage(), commandManager);
                FindCommandEvent cmdEvent = new FindCommandEvent(command, event.getJDA());
                event.getJDA().getEventManager().handle(cmdEvent);
                if (!cmdEvent.isCancel()){
                    commandManager.call(command);
                }
            });
        }
    }

    private String[] removePrefix(String prefix, String[] args){
        String replaced = args[0].replace(prefix, "");
        return Arrays.stream(args).map(str -> {
            if (!(prefix + replaced).equals(str)){
                return str;
            }
            return replaced;
        }).toArray(String[]::new);
    }
}
