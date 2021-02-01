package me.skiincraft.discord.core.plugin.objects;

import com.google.gson.annotations.SerializedName;

public class BotFile {

    @SerializedName(value = "BotName", alternate = {"Botname", "botname"})
    private final String botName;
    @SerializedName(value = "Main", alternate = {"BotMain", "main"})
    private final String botMain;
    @SerializedName(value = "Prefix", alternate = {"prefix"})
    private final String prefix;
    @SerializedName(value = "OwnerId", alternate = {"Ownerid","ownerid"})
    private final long ownerId;

    public BotFile(String botName, String botMain, String prefix, long ownerId) {
        this.botName = botName;
        this.botMain = botMain;
        this.prefix = prefix;
        this.ownerId = ownerId;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotMain() {
        return botMain;
    }

    public String getPrefix() {
        return prefix;
    }

    public long getOwnerId() {
        return ownerId;
    }
}
