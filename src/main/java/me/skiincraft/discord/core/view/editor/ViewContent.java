package me.skiincraft.discord.core.view.editor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.utils.ImageUtils;
import me.skiincraft.discord.core.view.controllers.ViewerController;
import net.dv8tion.jda.api.JDA;
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
	private ComboBox<JDA> botViewerSelector;
	// ANCHORPANE 4
	private TextField searchField;
	private ListView<Guild> searchList;
	
	private Tab tab1;
	private Tab tab2;
	
	private List<Guild> guildList;
	
	public ViewContent(Text avatarBotName, ImageView avatarImagePreview, Text botPresenceText, Text botName,
			Text botDiscriminator, Text botDefaultPrefix, Text botGuildsSize, Text botCommandSize,
			Button botRestartButton, Button botStopButton, ComboBox<JDA> botViewerSelector, TextField searchField,
			ListView<Guild> searchList, Tab tab1, Tab tab2, List<Guild> guildlist) {
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
		this.tab1 = tab1;
		this.tab2 = tab2;
		this.guildList = guildlist;
	}
	
	public ViewContent(ViewerController v) {
		this.avatarBotName = v.getAvatarBotName();
		this.avatarImagePreview = v.getAvatarImagePreview();
		this.botPresenceText = v.getBotPresenceText();
		this.botName = v.getBotName();
		this.botDiscriminator = v.getBotDiscriminator();
		this.botDefaultPrefix = v.getBotDefaultPrefix();
		this.botGuildsSize = v.getBotGuildsSize();
		this.botCommandSize = v.getBotCommandSize();
		this.botRestartButton = v.getBotRestartButton();
		this.botStopButton = v.getBotStopButton();
		this.botViewerSelector = v.getBotViewerSelector();
		this.searchField = v.getSearchField();
		this.searchList = v.getSearchList();
		this.tab1 = v.getOusuTab1();
		this.tab2 = v.getOusuTab2();
		this.guildList = v.getGuildlist();
	}
	
	public Tab getTab1() {
		return tab1;
	}
	
	public Tab getTab2() {
		return tab2;
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
		return new SearchPane(searchField, searchList, guildList);
	}

	public static final ViewContent getViewer() {
		return OusuCore.getOusuViewer().getViewerContent();
	}
	
	public static class AvatarPane {

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

	public static class OptionPane{
		private Button botRestartButton;
		private Button botStopButton;
		private ComboBox<JDA> botViewerSelector;
		
		public OptionPane(Button botRestartButton, Button botStopButton, ComboBox<JDA> botViewerSelector) {
			this.botRestartButton = botRestartButton;
			this.botStopButton = botStopButton;
			this.botViewerSelector = botViewerSelector;
		}
		
		public void setSelector(List<JDA> shards) {
			botViewerSelector.setItems(FXCollections.observableArrayList(shards));
		}
		
		public ComboBox<JDA> getBotViewerSelector() {
			return botViewerSelector;
		}
		
		public Button getBotRestartButton() {
			return botRestartButton;
		}
		
		public Button getBotStopButton() {
			return botStopButton;
		}
	}

	public static class SearchPane {
		private TextField searchField;
		private ListView<Guild> searchList;
		private List<Guild> guildList;
		
		public SearchPane(TextField searchField, ListView<Guild> searchList, List<Guild> guildList) {
			this.searchField = searchField;
			this.searchList = searchList;
			this.guildList = guildList;
		}
		
		public TextField getSearchField() {
			return searchField;
		}
		
		public void setSearchList(List<Guild> guild) {
			guildList.clear();
			guildList.addAll(guild);
			searchList.setItems(FXCollections.observableArrayList(guild));
		}
		
	}
}
