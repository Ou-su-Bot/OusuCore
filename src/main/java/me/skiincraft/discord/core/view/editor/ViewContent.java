package me.skiincraft.discord.core.view.editor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.view.OusuViewer;
import net.dv8tion.jda.api.entities.Guild;

public final class ViewContent {
	
	// ANCHORPANE 1
	private Text avatarBotName;
	private ImageView avatarImagePreview;
	private Text botPresenceText;
	// ANCHORPANE 2
	private Text botName;
	private Text botDiscriminator;
	private Text botDefaultPrefix;
	private Text botGuildsSize;
	private Text botCommandSize;
	// ANCHORPANE 3
	private Button botRestartButton;
	private Button botStopButton;
	private ComboBox<String> botViewerSelector;
	// ANCHORPANE 4
	private TextField searchField;
	private ListView<String> searchList;
	
	public ViewContent(Text avatarBotName, ImageView avatarImagePreview, Text botPresenceText, Text botName,
			Text botDiscriminator, Text botDefaultPrefix, Text botGuildsSize, Text botCommandSize,
			Button botRestartButton, Button botStopButton, ComboBox<String> botViewerSelector, TextField searchField,
			ListView<String> searchList) {
		this.avatarBotName = avatarBotName;
		this.avatarImagePreview = avatarImagePreview;
		this.botPresenceText = botPresenceText;
		this.botName = botName;
		this.botDiscriminator = botDiscriminator;
		this.botDefaultPrefix = botDefaultPrefix;
		this.botGuildsSize = botGuildsSize;
		this.botCommandSize = botCommandSize;
		this.botRestartButton = botRestartButton;
		this.botStopButton = botStopButton;
		this.botViewerSelector = botViewerSelector;
		this.searchField = searchField;
		this.searchList = searchList;
	}
	
	public AvatarPane getAvatarPane() {
		return new AvatarPane(avatarBotName, avatarImagePreview, botPresenceText);
	}
	
	public InfoPane getInfoPane() {
		return new InfoPane(botName, botDiscriminator, botDefaultPrefix, botGuildsSize, botCommandSize);
	}
	
	public OptionPane getOptionPane() {
		return new OptionPane(botRestartButton, botStopButton, botViewerSelector);
	}
	
	public SearchPane getSearchPane() {
		return new SearchPane(searchField, searchList);
	}

	public static final ViewContent getViewer() {
		return OusuViewer.viewerContent;
	}
	
	static class AvatarPane {

		private Text avatarBotName;
		private ImageView avatarImagePreview;
		private Text botPresenceText;

		public AvatarPane(Text avatarBotName, ImageView avatarImagePreview, Text botPresenceText) {
			this.avatarBotName = avatarBotName;
			this.avatarImagePreview = avatarImagePreview;
			this.botPresenceText = botPresenceText;
		}

		public void setBotName(String string) {
			avatarBotName.setText(string);
		}

		public void setAvatarPreview(Image image) {
			avatarImagePreview.setImage(image);
		}

		public void setAvatarPreview(BufferedImage image) {
			try {
				setAvatarPreview(new Image(ImageUtils.toInputStream(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setAvatarPreview(InputStream image) {
			setAvatarPreview(new Image(image));
		}

		public void setAvatarPreview(URL image) {
			try {
				setAvatarPreview(new Image(ImageUtils.toInputStream(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setPresencePreview(String string) {
			botPresenceText.setText(string);
		}
	}
	
	public static class InfoPane {
		private Text botName;
		private Text botDiscriminator;
		private Text botDefaultPrefix;
		private Text botGuildsSize;
		private Text botCommandSize;
		
		public InfoPane(Text botName, Text botDiscriminator, Text botDefaultPrefix, Text botGuildsSize,
				Text botCommandSize) {
			this.botName = botName;
			this.botDiscriminator = botDiscriminator;
			this.botDefaultPrefix = botDefaultPrefix;
			this.botGuildsSize = botGuildsSize;
			this.botCommandSize = botCommandSize;
		}
		
		public void setBotName(String string) {
			ViewContent.getViewer().getAvatarPane().setBotName(string);
			botName.setText(string);
		}
		public void setBotDiscriminator(String string) {
			botDiscriminator.setText(string);
		}
		public void setDefaultPrefix(String string) {
			botDefaultPrefix.setText(string);
		}
		public void setGuildSize(int size) {
			botGuildsSize.setText(String.valueOf(size));
		}
		public void setCommandSize(int size) {
			botCommandSize.setText(String.valueOf(size));
		}
		
		
	}

	static class OptionPane{
		private Button botRestartButton;
		private Button botStopButton;
		private ComboBox<String> botViewerSelector;
		
		public OptionPane(Button botRestartButton, Button botStopButton, ComboBox<String> botViewerSelector) {
			this.botRestartButton = botRestartButton;
			this.botStopButton = botStopButton;
			this.botViewerSelector = botViewerSelector;
		}
		
		public void setSelector(List<Plugin> pluginlist) {
			botViewerSelector.setItems(FXCollections.observableArrayList(
					pluginlist.stream().map(o -> o.getDiscordInfo().getBotname()).collect(Collectors.toList())));
		}
		
		public Button getBotRestartButton() {
			return botRestartButton;
		}
		
		public Button getBotStopButton() {
			return botStopButton;
		}
	}

	static class SearchPane {
		private TextField searchField;
		private ListView<String> searchList;
		
		public SearchPane(TextField searchField, ListView<String> searchList) {
			this.searchField = searchField;
			this.searchList = searchList;
		}
		
		public TextField getSearchField() {
			return searchField;
		}
		
		public void setSearchListByString(List<String> stringguilds) {
			searchList.setItems(FXCollections.observableArrayList(stringguilds));
		}
		
		public void setSearchList(Stream<Guild> guildstream) {
			searchList.setItems(FXCollections.observableArrayList(guildstream.map(o -> o.getName()).collect(Collectors.toList())));
		}
		
		public void setSearchList(List<Guild> guild) {
			setSearchList(guild.stream());
		}
		
	}
}
