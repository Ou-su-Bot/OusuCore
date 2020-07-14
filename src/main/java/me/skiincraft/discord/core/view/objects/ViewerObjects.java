package me.skiincraft.discord.core.view.objects;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.ImageUtils;
import net.dv8tion.jda.api.entities.User;

public class ViewerObjects {
	
	private Text botName;
	private ImageView botimage;
	private Text botId;
	private Text botPresence;
	
	private ListView<String> guildlist;
	private List<String> guilds;
	
	public ViewerObjects(Text botName, ImageView botimage, Text botId, Text botPresence, List<String> guilds) {
		this.botName = botName;
		this.botimage = botimage;
		this.botId = botId;
		this.botPresence = botPresence;
		this.guilds = guilds;
	}
	
	public void updateViewValues(Plugin plugin) {
		User selfuser = plugin.getShardManager().getShards().get(0).getSelfUser();
		setBotName(selfuser.getName()
				.concat("#")
				.concat(selfuser.getDiscriminator()));
		setBotId(selfuser.getIdLong());
		setBotPresence(plugin.getShardManager().getShards().get(0).getPresence().getActivity().toString());
		try {
			setBotimage(new Image(ImageUtils.toInputStream(selfuser.getAvatarUrl())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		guilds = plugin.getShardManager().getGuildCache().asList().stream().map(l -> l.getName())
				.collect(Collectors.toList());
		guildlist.setItems(FXCollections.observableArrayList(guilds));
	}
	
	public void setGuilds(List<String> guilds) {
		this.guilds = guilds;
	}
	
	public void setBotName(String name) {
		botName.setText(name);
	}
	
	public void setBotId(long id) {
		botId.setText(String.valueOf(id));
	}
	
	public void setBotimage(Image image) {
		botimage.setImage(image);
	}
	
	public void setBotPresence(String presence) {
		botPresence.setText(presence);
	}

}
