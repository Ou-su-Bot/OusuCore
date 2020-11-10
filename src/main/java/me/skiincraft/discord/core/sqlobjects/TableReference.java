package me.skiincraft.discord.core.sqlobjects;

public class TableReference {

	private final String where;
	private final String from;
	
	public TableReference(String where, String from) {
		this.where = where;
		this.from = from;
	}
	
	public String getWhere() {
		return where;
	}
	public String getFrom() {
		return from;
	}
	
	
}
