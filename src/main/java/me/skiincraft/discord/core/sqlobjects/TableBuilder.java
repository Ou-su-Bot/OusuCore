package me.skiincraft.discord.core.sqlobjects;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TableBuilder {
	
	private String tablename;
	private List<String> keys = new ArrayList<>();
	private List<TableValues> values = new ArrayList<>();
	
	public TableBuilder(String tablename) {
		this.tablename = tablename;
	}
	
	public enum TableValues{
		VARCHAR("VARCHAR"), BOOLEAN("BOOLEAN"), LONG("LONG"), INTEGER("INT"), FLOAT("FLOAT");
		
		private String name;
		private TableValues(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public void setTableName(@NotNull String tablename) {
		this.tablename = tablename;
	}
	
	public void addColumn(String tablename, TableValues value) {
		keys.add(tablename);
		values.add(value);
	}
	
	public Object[] getColumn() {
		return new Object[] {keys, values};
	}
	
	public String getTablename() {
		return tablename;
	}
	
	public Table build() {
		return new Table(this);
	}
}
