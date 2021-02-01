package me.skiincraft.ousucore.command.utils;

import me.skiincraft.ousucore.OusuCore;
import me.skiincraft.ousucore.language.Language;
import me.skiincraft.ousucore.repository.OusuGuild;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class CommandTools extends ChannelUtils {

    private Message message;

    public CommandTools(Message message){
        super(message.getTextChannel());
        this.message = message;
    }

    public OusuGuild getOusuGuild(){
        return OusuCore.getGuildRepository().getById(getGuild().getIdLong()).orElse(null);
    }

    public Language getLanguage(){
        return Language.getGuildLanguage(getGuild());
    }

    public Guild getGuild(){
        return message.getGuild();
    }

    public Member getMember(){
        return message.getMember();
    }

    public Message getMessage() {
        return message;
    }
}
