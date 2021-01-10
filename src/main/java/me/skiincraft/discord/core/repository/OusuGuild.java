package me.skiincraft.discord.core.repository;

import me.skiincraft.discord.core.configuration.Language;
import me.skiincraft.sql.annotation.Id;
import me.skiincraft.sql.annotation.Table;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Table("tb_guilds")
public class OusuGuild {

    @Id
    private long id;
    private String guildName;
    private String language;
    private String prefix;
    private OffsetDateTime addedIn;

    public OusuGuild() {
    }

    public OusuGuild(Guild guild) {
        this.id = guild.getIdLong();
        this.guildName = guild.getName();
        this.addedIn = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public OusuGuild(long id, String guildName, String language, @Nonnull String prefix, OffsetDateTime addedIn) {
        this.id = id;
        this.guildName = guildName;
        this.language = language;
        this.prefix = prefix;
        this.addedIn = addedIn;
    }

    public long getId() {
        return id;
    }

    public OusuGuild setId(long id) {
        this.id = id;
        return this;
    }

    public String getGuildName() {
        return guildName;
    }

    public OusuGuild setGuildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public OusuGuild setLanguage(Language language) {
        if (language == null)
            return this;

        this.language = language.getName();
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public OusuGuild setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public OffsetDateTime getAddedIn() {
        return addedIn;
    }

    public OusuGuild setAddedIn(OffsetDateTime addedIn) {
        this.addedIn = addedIn;
        return this;
    }
}
