package me.skiincraft.discord.core.repository;

import me.skiincraft.beans.annotation.RepositoryMap;
import me.skiincraft.sql.repository.Repository;

@RepositoryMap
public interface GuildRepository extends Repository<OusuGuild, Long> {
}
