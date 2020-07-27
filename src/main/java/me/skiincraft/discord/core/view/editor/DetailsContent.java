package me.skiincraft.discord.core.view.editor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.view.controllers.GuildDetailsController;
import net.dv8tion.jda.api.entities.Guild;

public class DetailsContent {
	
	private GuildDetailsController controller;
	private Stage stage;
	
	public DetailsContent(GuildDetailsController controller) {
		this.controller = controller;
	}
	
	public void updateGuild(Guild guild) {
		try {
			controller.getGuildImage().setImage(new Image(ImageUtils.toInputStream(ImageUtils.getDiscordAssets(guild.getIconUrl()))));
			controller.getGuildName().setText(guild.getName());
			controller.getGuildIdText().setText(guild.getId());
			controller.getJoinedOnText().setText(guild.getSelfMember().getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
			controller.getMembersText().setText(guild.getMemberCount()+"");
			controller.getOwnerText().setText(guild.getOwner().getUser().getName() + " (".concat(guild.getOwnerId()).concat(")"));
			controller.getRegionText().setText(guild.getRegionRaw());
			controller.getTextCText().setText(guild.getTextChannelCache().size() + " Text");
			controller.getVoiceCText().setText(guild.getVoiceChannelCache().size() + " Voice");
			controller.getTxtChannelList().setItems(FXCollections.observableArrayList(guild.getTextChannelCache().asList()));
			controller.getVoiceChannelList().setItems(FXCollections.observableArrayList(guild.getVoiceChannelCache().asList()));
			if (stage != null)
			getStage().show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Stage getStage() {
		return stage;
	}

}
