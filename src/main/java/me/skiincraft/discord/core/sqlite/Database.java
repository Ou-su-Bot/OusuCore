package me.skiincraft.discord.core.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.utils.StringUtils;

public abstract class Database {

	private Plugin plugin;
	private SQLite sqlite;
	
	private DBObject dblocal;
	public abstract String databaseName();
	
	public Database(Plugin plugin) {
		super();
		this.plugin = plugin;
		this.sqlite = plugin.getSQLite();
	}
	
	public DBObject getDbLocal() {
		if (dblocal == null) {
			dblocal = dbObject();	
		}
		return dblocal;
	}
	public void setDbLocal(DBObject obj) {
		dblocal = obj;
		return;
	}
	
	public abstract DBObject dbObject();
	public abstract Map<String, String> tableValues();
	
	public void create() {
		if (tableValues() == null || tableValues().size() == 0) {
			//TODO
			System.out.println("Tabela vazia");
			return;
		}
		if (exists()) {
			return;
		}
		List<String> table = tableValues().keySet().stream().collect(Collectors.toList());
		String[] keys = new String[table.size()];
		String[] values = new String[table.size()];
		int i = 0;
		for (String s : table) {
			keys[i] = s;
			values[i] = tableValues().get(s);
			i++;
		}
		
		String insert = StringUtils.insertBuild(keys);
		String value = StringUtils.selectBuild(values);
		
		sqlite.executeStatementTask(statement -> {
			try {
				statement.execute("INSERT INTO `" + databaseName() + "`" + insert + " VALUES" + value + ";");
			} catch (SQLException e){
				//TODO
			}
		});
	}
	
	public boolean exists() {
		String where = getDbLocal().getWhere();
		String from = getDbLocal().getFrom();
		List<Boolean> booleans = new ArrayList<Boolean>();
		sqlite.executeStatementTask(statement ->{
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT * FROM `" + databaseName() + "` WHERE ");
				buffer.append("`" + where +"` = '" + from + "';");
				ResultSet result = statement.executeQuery(buffer.toString());
				
				booleans.add((result.next()) ? result.getString("guildid") != null
						: false);
			} catch (SQLException e) {
				booleans.add(false);
				//TODO Lembrete para colocar logs;
			}
		});
		int i = 0;
		while (booleans.size() == 0) {
			try {
				Thread.sleep(200L);
				i++;
				if (i == 10) booleans.add(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return booleans.get(0);
	}
	
	public void delete() {
		String where = getDbLocal().getWhere();
		String from = getDbLocal().getFrom();
		plugin.getSQLite().executeStatementTask(statement ->{
			try {
				if (!exists()) {
					return;
				}
				statement.execute("DELETE FROM `" + databaseName() + "` WHERE `" + where +"` = '" + from + "';");
			} catch (SQLException e) {
				//TODO Lembrete para colocar logs;
			}
		});
	}
	
	public String get(String column) {
		String where = getDbLocal().getWhere();
		String from = getDbLocal().getFrom();
		if (!exists()) {
			create();
		}
		List<String> str = new ArrayList<String>();
		sqlite.executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + databaseName() + "` WHERE `" + where +"` = '" + from + "';");
				str.add((result.next())? result.getString(column) : null);
			} catch (SQLException e) {
				str.add("error");
				//TODO Lembrete para colocar logs;
			}
		});
		int i = 0;
		while (str.size() == 0) {
			try {
				Thread.sleep(200L);
				i++;
				if (i == 10) str.add(null);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return str.get(0);
	}
	
	public int getInt(String column) {
		String where = getDbLocal().getWhere();
		String from = getDbLocal().getFrom();
		if (!exists()) {
			create();
		}
		List<Integer> str = new ArrayList<Integer>();
		sqlite.executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + databaseName() + "` WHERE `" + where +"` = '" + from + "';");
				str.add((result.next())? result.getInt(column) : null);
			} catch (SQLException e) {
				str.add(0); //Eu queria colocar 404 :/
				//TODO Lembrete para colocar logs;
			}
		});
		int i = 0;
		while (str.size() == 0) {
			try {
				Thread.sleep(200L);
				i++;
				if (i == 10) str.add(null);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return str.get(0);
	}
	
	public void set(String column, String value) {
		String where = getDbLocal().getWhere();
		String from = getDbLocal().getFrom();
		if (!exists()) {
			create();
		}
		plugin.getSQLite().executeStatementTask(statement ->{
			try {
				statement.execute("UPDATE `" + databaseName() + "` SET `" + column + "` = '" + value + "' WHERE `" + where +"` = '" + from + "';");
				return;
			} catch (SQLException e) {
				//TODO Lembrete para colocar logs;
			}
		});
	}
	
	public void set(String column, int value) {
		set(column, String.valueOf(value));
	}
	
	public void set(String column, long value) {
		set(column, String.valueOf(value));
	}
	
	public void set(String column, double value) {
		set(column, String.valueOf(value));
	}
	
	public void set(String column, float value) {
		set(column, String.valueOf(value));
	}
	
	public void set(String column, boolean value) {
		set(column, String.valueOf(value));
	}
	public void set(String column, Object value) {
		set(column, value.toString());
	}
	
	public final Plugin getPlugin() {
		return plugin;
	}
	
	public final SQLite getSqlite() {
		return sqlite;
	}
}
