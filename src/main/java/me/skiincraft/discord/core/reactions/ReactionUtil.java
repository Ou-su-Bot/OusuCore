package me.skiincraft.discord.core.reactions;

public class ReactionUtil {
	
	private long userId;
	private long messageId;
	private long guildId;
	private ReactionObject[] reactionObjects;
	
	public ReactionUtil(long userId, long messageId, long guildId, ReactionObject[] reactionObjects) {
		this.userId = userId;
		this.messageId = messageId;
		this.guildId = guildId;
		this.reactionObjects = reactionObjects;
	}

	public ReactionObject[] getReactionObjects() {
		return reactionObjects;
	}

	public void setReactionObjects(ReactionObject[] reactionObjects) {
		this.reactionObjects = reactionObjects;
	}

	public long getUserId() {
		return userId;
	}

	public long getMessageId() {
		return messageId;
	}

	public long getGuildId() {
		return guildId;
	}
	
	
	
}
