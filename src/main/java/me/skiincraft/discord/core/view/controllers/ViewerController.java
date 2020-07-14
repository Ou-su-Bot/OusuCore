package me.skiincraft.discord.core.view.controllers;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import me.skiincraft.discord.core.view.OusuViewer;
import me.skiincraft.discord.core.view.editor.ViewContent;

public class ViewerController implements Initializable {

	// ANCHORPANE 1
	@FXML
	private Text avatarBotName;

	@FXML
	private ImageView avatarImagePreview;
	
	@FXML
	private ImageView ousulogo;
	
	@FXML
	private Text botPresenceText;
	// ANCHORPANE 2

	@FXML
	private Text botName;
	
	@FXML
	private Text botDiscriminator;
	
	@FXML
	private Text botDefaultPrefix;

	@FXML
	private Text botGuildsSize;
	
	@FXML
	private Text botCommandSize;
	// ANCHORPANE 3
	
	@FXML
	private Button botRestartButton;
	@FXML
	private Button botStopButton;
	@FXML
	private ComboBox<String> botViewerSelector;
	private List<String> botlist = new ArrayList<>();
	@FXML
	private TextField searchField;
	// ANCHORPANE 4
	
	@FXML
	private ListView<String> searchList;
	private static List<String> guildlist = new ArrayList<String>();
	private List<String> filter;
	
	
	// TAB
	@FXML
	private Tab ousuTab1;
	@FXML
	private Tab ousuTab2;
	@FXML
	private MenuBar optionMenuBar;
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		OusuViewer.viewerContent = new ViewContent(avatarBotName, avatarImagePreview, botPresenceText, botName,
				botDiscriminator, botDefaultPrefix, botGuildsSize, botCommandSize, botRestartButton, botStopButton,
				botViewerSelector, searchField, searchList);
		
		ousulogo.setImage(new Image(OusuViewer.class.getResourceAsStream("ousu-logo128x.png")));
		avatarImagePreview.setImage(new Image(OusuViewer.class.getResourceAsStream("images/no_bot_image.png")));
		
		for (int i = 0; i < 1000; i++) {
			guildlist.add("Selva" + i);
			guildlist.add("Yagate Server" + i);
			guildlist.add("Ou!su Support" + i);
			guildlist.add("HeroStats Support" + i);
			guildlist.add("Discord Bot List" + i);
			guildlist.add("ETC" + i);	
		}
		botlist.add("HeroStats");
		botlist.add("Ou!su Bot");
		
		botViewerSelector.setItems(FXCollections.observableArrayList(botlist));
		searchList.setItems(FXCollections.observableArrayList(guildlist));
	}
	
	public void searchFieldEvent(KeyEvent e) {
		if (searchField.getText().length() == 0) {
			searchList.setItems(FXCollections.observableArrayList(guildlist));
			return;
		}
		filter = guildlist.stream().filter(l -> l.toLowerCase().contains(searchField.getText().toLowerCase()))
				.collect(Collectors.toList());
		if (filter.size() == 0) {
			return;
		}
		searchList.setItems(FXCollections.observableArrayList(filter));
	}
	
	
	
	
	public static double getProcessCpuLoad() throws Exception {
	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty())     return Double.NaN;

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}

	public void optionButton() {
		if (ousuTab1.isSelected()) {
			if (optionMenuBar == null)return;
			optionMenuBar.setVisible(false);
		} else {
			optionMenuBar.setVisible(true);
		}

	}

}