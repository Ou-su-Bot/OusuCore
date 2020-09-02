package me.skiincraft.discord.core.configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.sqlobjects.AccessTable;
import me.skiincraft.discord.core.sqlobjects.Table;
import me.skiincraft.discord.core.sqlobjects.Table.Column.ColumnType;

import net.dv8tion.jda.api.entities.Guild;

public class GuildDB extends Table {

	private Guild guild;
	public GuildDB(Guild guild) {
		super("servidores", OusuCore.getPluginManager().getPlugin());
		this.guild = guild;
	}

	@Override
	public AccessTable accessTable() {
		return new AccessTable("guildid", guild.getId());
	}

	private String generatelang() {
		if (guild.getRegionRaw().contains("brazil")) {
			return "Portuguese";
		}
		if (guild.getRegionRaw().contains("us")) {
			return "English";
		}
		return "English";
	}	

	public List<Column> columns() {
		List<Column> columns = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> object = (Map<String, Object>) getPlugin().getPluginConfiguration().get("discord");
		Date newDate = new Date(TimeUnit.SECONDS.toMillis(guild.getSelfMember().getTimeJoined().toEpochSecond()));
		String simple = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(newDate);
		
		columns.add(new Column("guildid", ColumnType.VARCHAR, 60, guild.getId()));
		columns.add(new Column("nome", ColumnType.VARCHAR, 60, guild.getName().replace("'", "").replace("´", "").replace("`", "")));
		columns.add(new Column("membro", ColumnType.VARCHAR, 60, guild.getMemberCount()));
		columns.add(new Column("prefix", ColumnType.VARCHAR, 10, object.get("defaultprefix")));
		columns.add(new Column("adicionado em", ColumnType.VARCHAR, 15, simple));
		columns.add(new Column("language", ColumnType.VARCHAR, 15, generatelang()));
		
		return columns;
	}
}
