package me.skiincraft.discord.core.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreSettings {

    private final String token;
    private final int shards;
    private final ChunkingFilter filter;
    private final List<CacheFlag> cacheFlags;
    private final List<GatewayIntent> gatewayIntents;

    private static final CacheFlag[] DEFAULT_FLAG = new CacheFlag[]{ CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES};
    private static final GatewayIntent[] DEFAULT_INTENT = new GatewayIntent[]{GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS};
    CoreSettings(String token, int shards, ChunkingFilter filter, List<CacheFlag> cacheFlags, List<GatewayIntent> gatewayIntents) {
        this.token = token;
        this.shards = shards;
        this.filter = filter;
        this.cacheFlags = cacheFlags;
        this.gatewayIntents = gatewayIntents;
    }

    public String getToken() {
        return token;
    }

    public int getShards() {
        if (shards == 0) return 1;
        return shards;
    }

    public ChunkingFilter getFilter() {
        return filter;
    }

    public List<CacheFlag> getCacheFlags() {
        return new ArrayList<>(cacheFlags);
    }

    public List<GatewayIntent> getGatewayIntents() {
        return new ArrayList<>(gatewayIntents);
    }

    public static CoreSettings of(JsonObject json){
        if (containsJsonNull(json)){
            throw new JsonParseException("Algo de errado não está certo no arquivo: settings.json");
        }
        Builder builder = new Builder()
                .setToken(json.get("Token").getAsString())
                .setShards(json.get("Shards").getAsInt())
                .setFilter((json.has("ChunkFilter")) || json.get("ChunkFilter").getAsBoolean()? ChunkingFilter.ALL: ChunkingFilter.NONE);

        List<CacheFlag> flags = new ArrayList<>();
        if (json.has("CacheFlag") && !json.get("CacheFlag").isJsonNull()){
            JsonObject cf = json.get("CacheFlag").getAsJsonObject();
            if (cf.has("DEFAULT")){
                flags.addAll(Arrays.asList(DEFAULT_FLAG));
            } else {
                flags.addAll(Arrays.stream(CacheFlag.values())
                        .filter(flag -> cf.has(flag.name()) && cf.get(flag.name()).getAsBoolean())
                        .collect(Collectors.toList()));
            }
        }

        List<GatewayIntent> intents = new ArrayList<>();
        if (json.has("GatewayIntents") && !json.get("GatewayIntents").isJsonNull()){
            JsonObject cf = json.get("GatewayIntents").getAsJsonObject();
            if (cf.has("DEFAULT")) {
                intents.addAll(Arrays.asList(DEFAULT_INTENT));
            } else {
                intents.addAll(Arrays.stream(GatewayIntent.values())
                        .filter(flag -> cf.has(flag.name()) && cf.get(flag.name()).getAsBoolean())
                        .collect(Collectors.toList()));
            }
        }

        builder.setCacheFlags(flags);
        builder.setGatewayIntents(intents);
        return builder.createCoreSettings();
    }

    private static boolean containsJsonNull(JsonObject object){
        if (object.has("Token") && object.has("Shards")
                && object.has("ChunkFilter")){
            return object.get("Token").isJsonNull() || (object.get("Shards").isJsonNull() || object.get("Shards").getAsInt() == 0)
                    || object.get("ChunkFilter").isJsonNull();
        } else {
            return false;
        }
    }

    public static class Builder {
        private String token;
        private int shards;
        private ChunkingFilter filter;
        private List<CacheFlag> cacheFlags;
        private List<GatewayIntent> gatewayIntents;

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setShards(int shards) {
            this.shards = shards;
            return this;
        }

        public Builder setFilter(ChunkingFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder setCacheFlags(List<CacheFlag> cacheFlags) {
            this.cacheFlags = cacheFlags;
            return this;
        }

        public Builder setGatewayIntents(List<GatewayIntent> gatewayIntents) {
            this.gatewayIntents = gatewayIntents;
            return this;
        }

        public CoreSettings createCoreSettings() {
            return new CoreSettings(token, shards, filter, cacheFlags, gatewayIntents);
        }
    }

}
