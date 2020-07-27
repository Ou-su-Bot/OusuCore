package me.skiincraft.discord.core.view.controllers;

import java.awt.Dimension;
import java.io.IOException;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.apis.ImageBuilder;
import me.skiincraft.discord.core.view.OusuViewer;
import me.skiincraft.discord.core.view.editor.ViewContent;
import me.skiincraft.discord.core.view.images.OusuImages;
import me.skiincraft.discord.core.view.objects.ViewUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class ViewerController implements Initializable {

	// ANCHORPANE 1
	@FXML
	private Text avatarBotName;

	@FXML
	private Button guildSearchDetails;
	
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
	private ComboBox<JDA> botViewerSelector;
	private List<JDA> botlist = new ArrayList<>();
	@FXML
	private TextField searchField;
	// ANCHORPANE 4
	
	@FXML
	private ListView<Guild> searchList;
	private static List<Guild> guildlist = new ArrayList<Guild>();
	private List<Guild> filter;
	
	
	// TAB
	@FXML
	private Tab ousuTab1;
	@FXML
	private Tab ousuTab2;
	@FXML
	private MenuBar optionMenuBar;
	
	@FXML
	private Button refreshButton;
	
	@FXML
	private Text listSize;
	
	private ImageView reloadImage;
	
	public Text getAvatarBotName() {
		return avatarBotName;
	}

	public ImageView getAvatarImagePreview() {
		return avatarImagePreview;
	}

	public ImageView getOusulogo() {
		return ousulogo;
	}

	public Text getBotPresenceText() {
		return botPresenceText;
	}

	public Text getBotName() {
		return botName;
	}

	public Text getBotDiscriminator() {
		return botDiscriminator;
	}

	public Text getBotDefaultPrefix() {
		return botDefaultPrefix;
	}

	public Text getBotGuildsSize() {
		return botGuildsSize;
	}

	public Text getBotCommandSize() {
		return botCommandSize;
	}

	public Button getBotRestartButton() {
		return botRestartButton;
	}

	public Button getBotStopButton() {
		return botStopButton;
	}
	
	public Button getGuildSearchDetails() {
		return guildSearchDetails;
	}

	public ComboBox<JDA> getBotViewerSelector() {
		return botViewerSelector;
	}

	public List<JDA> getBotlist() {
		return botlist;
	}

	public TextField getSearchField() {
		return searchField;
	}

	public ListView<Guild> getSearchList() {
		return searchList;
	}

	public List<Guild> getGuildlist() {
		return guildlist;
	}

	public List<Guild> getFilter() {
		return filter;
	}

	public Tab getOusuTab1() {
		return ousuTab1;
	}

	public Tab getOusuTab2() {
		return ousuTab2;
	}

	public MenuBar getOptionMenuBar() {
		return optionMenuBar;
	}
	
	
	private ImageView refreshImage() {
		ImageView imageView = new ImageView(new OusuImages().getImage("refresh.png", new Dimension(20, 20)));
		imageView.maxWidth(20);
		imageView.maxHeight(20);
		
		return imageView;
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			FieldUtils.writeField(OusuCore.getOusuViewer(), "viewerContent", new ViewContent(this), true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		refreshButton.setVisible(false);
		refreshButton.setGraphic(refreshImage());
		reloadImage = new ImageView();
		try {
			ImageBuilder image = new ImageBuilder("", 16, 16);
			image.drawImage(OusuImages.class.getResourceAsStream("small_blue_square.png"), 8, 8, new Dimension(16, 16));
			reloadImage.setImage(new Image(image.buildInput()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		reloadImage.resize(16, 16);
		ousuTab1.setGraphic(reloadImage);
		
		ousulogo.setImage(new Image(OusuViewer.class.getResourceAsStream("ousu-logo128x.png")));
		avatarImagePreview.setImage(new Image(OusuViewer.class.getResourceAsStream("images/no_bot_image.png")));
		botViewerSelector.setItems(FXCollections.observableArrayList(botlist));
		searchList.setItems(FXCollections.observableArrayList(guildlist));
		searchCellFactory();
		selectorCellFactory();
	}
	
	private void selectorCellFactory() {
		botViewerSelector.setCellFactory(new Callback<ListView<JDA>, ListCell<JDA>>() {
		    public ListCell<JDA> call(ListView<JDA> param) {
		        final ListCell<JDA> cell = new ListCell<JDA>() {
		            @Override
		            public void updateItem(JDA item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item != null) {
		                    setText("Shard - " + item.getShardInfo().getShardString());

		                }
		            }
		        };
		        return cell;
		    }
		});
	}
	
	public void detailsAction() {
		if (!searchList.getSelectionModel().isEmpty()) {
			OusuCore.getOusuViewer().getDetailsContent().updateGuild(searchList.getSelectionModel().getSelectedItem());
		}
	}
	
	private void searchCellFactory() {
		searchList.setCellFactory(new Callback<ListView<Guild>, ListCell<Guild>>() {
			public ListCell<Guild> call(ListView<Guild> param) {
				final ListCell<Guild> cell = new ListCell<Guild>() {


					@Override
					public void updateItem(Guild item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
							listSize.setText(searchList.getItems().size() + "");
						}
					}
				};
				return cell;
			}
		});
	}
	
	public void refreshViewer() {
		ViewUtils.updateTab();
	}
	
	public void searchFieldEvent(KeyEvent e) {
		if (searchField.getText().length() == 0) {
			searchList.setItems(FXCollections.observableArrayList(guildlist));
			return;
		}
		
		filter = guildlist.stream().filter(l -> StringUtils.containsIgnoreCase(l.getName(), searchField.getText()))
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
			refreshButton.setVisible(true);
		} else {
			optionMenuBar.setVisible(true);
			refreshButton.setVisible(false);
		}
	}

}