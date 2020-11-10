package me.skiincraft.discord.core.plugin;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

@FunctionalInterface
public interface ShardBuilderLoader {
    DefaultShardManagerBuilder inicialize(DefaultShardManagerBuilder shardbuilder);
}
