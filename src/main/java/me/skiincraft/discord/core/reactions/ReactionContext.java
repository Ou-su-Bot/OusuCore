package me.skiincraft.discord.core.reactions;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionContext {
	
	private TextChannel channel;
	private long messageId;

	public ReactionContext(TextChannel channel, long messageId) {
		this.channel = channel;
		this.messageId = messageId;
	}

	public TextChannel getChannel() {
		return channel;
	}



	public long getMessageId() {
		return messageId;
	}



	public void changeEmbedNext(ReactionObject obj) {
		@SuppressWarnings("unchecked")
		List<EmbedBuilder> embeds = (obj.getObject() instanceof EmbedBuilder[])
				? Arrays.asList((EmbedBuilder[]) obj.getObject())
				: ((List<EmbedBuilder>) obj.getObject());
		
		int v = obj.getOrdem()+1;
		if (v >= embeds.size()) {
			return;
		}
		obj.setOrdem(v);
		channel.editMessageById(messageId, embeds.get(v).build()).queue();
	}
	
	public void changeEmbedBack(ReactionObject obj) {
		@SuppressWarnings("unchecked")
		List<EmbedBuilder> embeds = (obj.getObject() instanceof EmbedBuilder[])
				? Arrays.asList((EmbedBuilder[]) obj.getObject())
				: ((List<EmbedBuilder>) obj.getObject());

		int v = obj.getOrdem();
		if (v <= 0) {
			v = 0;
			return;
		} else {
			v -= 1;
		}
		obj.setOrdem(v);
		channel.editMessageById(messageId, embeds.get(v).build()).queue();
	}
}
