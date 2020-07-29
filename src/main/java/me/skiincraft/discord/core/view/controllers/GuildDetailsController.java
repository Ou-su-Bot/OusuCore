package me.skiincraft.discord.core.view.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.reflect.FieldUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.view.editor.DetailsContent;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class GuildDetailsController implements Initializable {
	
    @FXML
    private ImageView guildImage;

    public ImageView getGuildImage() {
		return guildImage;
	}

	public Button getInviteButton() {
		return inviteButton;
	}

	public Button getLeaveButton() {
		return leaveButton;
	}

	public Button getOkButton() {
		return okButton;
	}

	public Text getGuildName() {
		return guildName;
	}

	public Text getOwnerText() {
		return ownerText;
	}

	public Text getRegionText() {
		return regionText;
	}

	public Text getCreatedOnText() {
		return createdOnText;
	}

	public Text getJoinedOnText() {
		return joinedOnText;
	}

	public Text getGuildIdText() {
		return guildIdText;
	}

	public Text getTextCText() {
		return textCText;
	}

	public Text getVoiceCText() {
		return voiceCText;
	}

	public Text getMembersText() {
		return membersText;
	}

	public ListView<TextChannel> getTxtChannelList() {
		return txtChannelList;
	}

	public ListView<VoiceChannel> getVoiceChannelList() {
		return voiceChannelList;
	}

	@FXML
    private Button inviteButton;

    @FXML
    private Button leaveButton;

    @FXML
    private Button okButton;

    @FXML
    private Text guildName;

    @FXML
    private Text ownerText;

    @FXML
    private Text regionText;

    @FXML
    private Text createdOnText;

    @FXML
    private Text joinedOnText;

    @FXML
    private Text guildIdText;

    @FXML
    private Text textCText;

    @FXML
    private Text voiceCText;

    @FXML
    private Text membersText;

    @FXML
    private ListView<TextChannel> txtChannelList;

    @FXML
    private ListView<VoiceChannel> voiceChannelList;

	public void initialize(URL location, ResourceBundle resources) {
		try {
			FieldUtils.writeField(OusuCore.getOusuViewer(), "detailsContent", new DetailsContent(this), true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		voiceFactory();
		textFactory();
		okButton.setOnAction(action -> {
			
		});
	}
	
	private void voiceFactory() {
		voiceChannelList.setCellFactory(new Callback<ListView<VoiceChannel>, ListCell<VoiceChannel>>() {
			
			public ListCell<VoiceChannel> call(ListView<VoiceChannel> param) {
				final ListCell<VoiceChannel> cell = new ListCell<VoiceChannel>() {
					
					protected void updateItem(VoiceChannel item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
						} else {
							setText(null);
						}
						
					}
				};
				return cell;
			}
		});
	}
	
	private void textFactory() {
		txtChannelList.setCellFactory(new Callback<ListView<TextChannel>, ListCell<TextChannel>>() {
			
			public ListCell<TextChannel> call(ListView<TextChannel> param) {
				final ListCell<TextChannel> cell = new ListCell<TextChannel>() {
					
					protected void updateItem(TextChannel item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
						} else {
							setText(null);
						}
						
					}
				};
				return cell;
			}
		});
	}


}
