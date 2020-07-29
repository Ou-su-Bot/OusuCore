package me.skiincraft.discord.core.sqlobjects;

import java.util.ArrayList;
import java.util.List;

import me.skiincraft.discord.core.sqlobjects.TableBuilder.TableValues;

public class Table {
	
	private String tablename;
	private List<String> keys = new ArrayList<String>();
	private List<TableValues> values = new ArrayList<TableValues>();
	private boolean increment;
	
	@SuppressWarnings("unchecked")
	public Table(TableBuilder builder) {
		tablename = builder.getTablename();
		keys = (List<String>)builder.getColumn()[0];
		values = (List<TableValues>)builder.getColumn()[1];
		increment = builder.isIdAutoIncrement();
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE IF NOT EXISTS `");
		buffer.append(tablename + "`");
		if (increment) {
			buffer.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
		} else {
			buffer.append("(");
		}
		
		for (int i = 0; i < keys.size(); i++) {
			String complement = values.get(i).getName();
			complement = (complement != "INT") ? complement + " NOT NULL" : "INT";
			
			buffer.append("`"+ keys.get(i) + "` " + complement);
			if (i != keys.size() -1) {
				buffer.append(", ");
			}
		}
		buffer.append(");");
		
		return buffer.toString();
	}
}
