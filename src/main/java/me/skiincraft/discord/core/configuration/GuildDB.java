package me.skiincraft.discord.core.configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.sqlobjects.TableReference;
import me.skiincraft.discord.core.sqlobjects.Table;
import me.skiincraft.discord.core.sqlobjects.Table.Column.ColumnType;

import net.dv8tion.jda.api.entities.Guild;

public class GuildDB extends Table {

	private final Guild guild;
	public GuildDB(Guild guild) {
		super("servidores");
		this.guild = guild;
	}

	@Override
	public TableReference reference() {
		return new TableReference("guildid", guild.getId());
	}

	private String generatelang() {
		if (guild.getRegionRaw().contains("brazil")) {
			return "Portuguese";
		}
		return "English";
	}	

	public List<Column> columns() {
		Date newDate = new Date(TimeUnit.SECONDS.toMillis(guild.getSelfMember().getTimeJoined().toEpochSecond()));
		String simple = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(newDate);
		ColumnBuilder builder = new ColumnBuilder();
		builder.addColumn(Column.of("guildid", ColumnType.VARCHAR, 60, guild.getId()))
				.addColumn(Column.of("nome", ColumnType.VARCHAR, 60, guild.getName().replace("'", "").replace("´", "").replace("`", "")))
				.addColumn(Column.of("membros", ColumnType.VARCHAR, 60, guild.getMemberCount()))
				.addColumn(Column.of("prefix", ColumnType.VARCHAR, 10, OusuCore.getInternalSettings().getDefaultPrefix()))
				.addColumn(Column.of("adicionado em", ColumnType.VARCHAR, 15, simple))
				.addColumn(Column.of("language", ColumnType.VARCHAR, 15, generatelang()));
		
		return builder.build();
	}
}
