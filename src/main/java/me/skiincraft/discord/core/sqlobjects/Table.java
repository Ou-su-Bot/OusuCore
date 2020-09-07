package me.skiincraft.discord.core.sqlobjects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 
import java.util.stream.Collectors;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.sqlite.SQLite;
import me.skiincraft.discord.core.utils.StringUtils;

public abstract class Table {

	private String tableName;
	
	private Plugin plugin;
	private SQLite sqlite;
	
	private AccessTable dblocal;
	
	public Table(String tablename, Plugin plugin) {
		this.tableName = tablename;
		this.plugin = plugin;
		this.sqlite = plugin.getSQLite();
	}
	
	public AccessTable getAccess() {
		if (dblocal == null) {
			dblocal = accessTable();	
		}
		return dblocal;
	}
	public void setAcessTable(AccessTable obj) {
		dblocal = obj;
		return;
	}
	
	public abstract AccessTable accessTable();
	public abstract List<Column> columns();
	
	public synchronized void createTable() {
		sqlite.createTable(this);
	}
	
	public boolean existsTable() {
		return sqlite.existsTable(tableName);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void create() {
		if (columns() == null || columns().size() == 0) {
			System.out.println("Tabela vazia");
			return;
		}
		
		if (exists()) {
			return;
		}
		List<Column> table = columns();
		
		String value = StringUtils.selectBuild(table.stream().map(m -> m.getObject().toString()).collect(Collectors.toList()));
		String insert = StringUtils.insertBuild(table.stream().map(m -> m.getName()).collect(Collectors.toList()));
		
		sqlite.executeStatementTask(statement -> {
			try {
				statement.execute("INSERT INTO \"" + tableName + "\"" + insert + " VALUES" + value + ";");
			} catch (SQLException e){
				System.out.println("INSERT INTO \"" + tableName + "\"" + insert + " VALUES" + value + ";");
				e.printStackTrace();
				//TODO
			}
		});
	}
	
	public boolean exists() {
		String where = getAccess().getWhere();
		String from = getAccess().getFrom();
		List<Boolean> booleans = new ArrayList<Boolean>();
		
		if (!existsTable()) {
			createTable();
		}
		
		sqlite.executeStatementTask(statement ->{
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT * FROM `" + tableName + "` WHERE ");
				buffer.append("`" + where +"` = '" + from + "';");
				ResultSet result = statement.executeQuery(buffer.toString());
				
				booleans.add((result.next()) ? result.getString(where) != null
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
		String where = getAccess().getWhere();
		String from = getAccess().getFrom();
		plugin.getSQLite().executeStatementTask(statement ->{
			try {
				if (!exists()) {
					return;
				}
				statement.execute("DELETE FROM `" + tableName + "` WHERE `" + where +"` = '" + from + "';");
			} catch (SQLException e) {
				//TODO Lembrete para colocar logs;
			}
		});
	}
	
	public String get(String column) {
		String where = getAccess().getWhere();
		String from = getAccess().getFrom();
		if (!exists()) {
			create();
		}
		List<String> str = new ArrayList<String>();
		sqlite.executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + tableName + "` WHERE `" + where +"` = '" + from + "';");
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
		String where = getAccess().getWhere();
		String from = getAccess().getFrom();
		if (!exists()) {
			create();
		}
		List<Integer> str = new ArrayList<Integer>();
		sqlite.executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + tableName + "` WHERE `" + where +"` = '" + from + "';");
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
		String where = getAccess().getWhere();
		String from = getAccess().getFrom();
		if (!exists()) {
			create();
		}
		
		plugin.getSQLite().executeStatementTask(statement ->{
			try {
				statement.execute("UPDATE `" + tableName + "` SET `" + column + "` = '" + value + "' WHERE `" + where +"` = '" + from + "';");
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
	
	public static class Column {
		
		public enum ColumnType {
			
			VARCHAR("VARCHAR"), BOOLEAN("BOOLEAN"), LONG("LONG"), INTEGER("INT"), FLOAT("FLOAT");
			
			private String name;
			
			private ColumnType(String name) {
				this.name = name;
			}
			
			public String getName() {
				return name;
			}
		}
		
		private String name;
		private ColumnType type;
		private int value;
		
		private Object object;
		
		public Column(String name, ColumnType type, int typevalue, Object value) {
			this.name = name;
			this.type = type;
			this.object = value;
			this.value = (typevalue <= 10) ? 20 : typevalue;
		}
		public String getName() {
			return name;
		}
		public ColumnType getType() {
			return type;
		}
		
		public Object getObject() {
			return object;
		}
		
		public int getValue() {
			return value;
		}
		
		public String toString() {
			return '`' + name +'`' + " " + ((type.getName() == ColumnType.VARCHAR.getName()) ? type.name + "(" + value +") NOT NULL": type.name + " NOT NULL");
		}
	}
}
