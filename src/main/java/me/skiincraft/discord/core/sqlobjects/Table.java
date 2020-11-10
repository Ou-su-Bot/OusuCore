package me.skiincraft.discord.core.sqlobjects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.utils.StringUtils;

public abstract class Table {

	private final String tableName;
	private TableReference dblocal;
	public abstract TableReference reference();
	public abstract List<Column> columns();
	
	public Table(String tablename) {
		this.tableName = tablename;
	}
	
	public TableReference getAccess() {
		if (dblocal == null) {
			dblocal = reference();
		}
		return dblocal;
	}
	public void setAcessTable(TableReference obj) {
		dblocal = obj;
	}
	
	public synchronized void createTable() {
		OusuCore.getSQLiteDatabase().createTable(this);
	}
	
	public boolean existsTable() {
		return OusuCore.getSQLiteDatabase().existsTable(tableName);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void create() {
		if (columns() == null || columns().size() == 0) {
			throw new RuntimeException("Table is null or empty");
		}
		if (exists()) {
			return;
		}

		List<Column> table = columns();
		String value = StringUtils.selectBuild(table.stream().map(m -> m.getObject().toString()).collect(Collectors.toList()));
		String insert = StringUtils.insertBuild(table.stream().map(Column::getName).collect(Collectors.toList()));

		OusuCore.getSQLiteDatabase().executeStatementTask(statement -> {
			try {
				statement.execute("INSERT INTO \"" + tableName + "\"" + insert + " VALUES" + value + ";");
			} catch (SQLException e){
				OusuCore.getLogger().error("Não foi criar uma valor em uma tabela: " + "INSERT INTO \"" + tableName + "\"" + insert + " VALUES" + value + ";");
				OusuCore.getLogger().throwing(e);
			}
		});
	}
	
	public boolean exists() {
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		if (!existsTable()) {
			createTable();
		}
		
		OusuCore.getSQLiteDatabase().executeStatementTask(statement ->{
			try {
				String buffer = "SELECT * FROM `" + tableName + "` WHERE " +
						"`" + getAccess().getWhere() + "` = '" + getAccess().getFrom() + "';";
				ResultSet result = statement.executeQuery(buffer);
				atomicBoolean.set(result.next() && result.getString(getAccess().getWhere()) != null);
			} catch (SQLException e) {
				OusuCore.getLogger().throwing(e);
			}
		});
		return atomicBoolean.get();
	}
	
	public void delete() {
		OusuCore.getSQLiteDatabase().executeStatementTask(statement ->{
			try {
				if (!exists()) {
					return;
				}
				statement.execute("DELETE FROM `" + tableName + "` WHERE `" + getAccess().getWhere() +"` = '" + getAccess().getFrom() + "';");
			} catch (SQLException e) {
				OusuCore.getLogger().throwing(e);
			}
		});
	}
	
	public String get(String column) {
		if (!exists()) {
			create();
		}
		AtomicReference<String> atomicReference = new AtomicReference<>();
		OusuCore.getSQLiteDatabase().executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + tableName + "` WHERE `" + getAccess().getWhere() +"` = '" + getAccess().getFrom() + "';");
				atomicReference.set((result.next())? result.getString(column) : null);
			} catch (SQLException e) {
				OusuCore.getLogger().throwing(e);
			}
		});
		return atomicReference.get();
	}
	
	public int getInt(String column) {
		if (!exists()) {
			create();
		}
		AtomicInteger atomicInteger = new AtomicInteger(0);
		OusuCore.getSQLiteDatabase().executeStatementTask(statement ->{
			try {
				ResultSet result = statement.executeQuery("SELECT * FROM `" + tableName + "` WHERE `" + getAccess().getWhere() +"` = '" + getAccess().getFrom() + "';");
				atomicInteger.set((result.next())? result.getInt(column) : 0);
			} catch (SQLException e) {
				OusuCore.getLogger().throwing(e);
			}
		});
		return atomicInteger.get();
	}
	
	public void set(String column, String value) {
		if (!exists()) {
			create();
		}

		OusuCore.getSQLiteDatabase().executeStatementTask(statement ->{
			try {
				statement.execute("UPDATE `" + tableName + "` SET `" + column + "` = '" + value + "' WHERE `" + getAccess().getWhere() +"` = '" + getAccess().getFrom() + "';");
			} catch (SQLException e) {
				OusuCore.getLogger().throwing(e);
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
	
	public static class Column {
		
		public enum ColumnType {
			
			VARCHAR("VARCHAR"), BOOLEAN("BOOLEAN"), LONG("LONG"), INTEGER("INT"), FLOAT("FLOAT");
			
			private final String name;
			
			ColumnType(String name) {
				this.name = name;
			}
			
			public String getName() {
				return name;
			}
		}
		
		private final String name;
		private final ColumnType type;
		private final int value;
		
		private final Object object;
		
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

		public static Column of(String name, ColumnType type, int typevalue, Object value){
			return new Column(name,type, typevalue, value);
		}
		
		public String toString() {
			return '`' + name +'`' + " " + ((type.getName().equals(ColumnType.VARCHAR.getName())) ? type.name + "(" + value +") NOT NULL": type.name + " NOT NULL");
		}
	}

	public static class ColumnBuilder {

		private final List<Column> columns;

		public ColumnBuilder() {
			this.columns = new ArrayList<>();
		}

		public ColumnBuilder addColumn(Column column){
			columns.add(column);
			return this;
		}

		public List<Column> build(){
			return columns;
		}

	}
}
