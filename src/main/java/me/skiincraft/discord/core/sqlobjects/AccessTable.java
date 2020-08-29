package me.skiincraft.discord.core.sqlobjects;

public class AccessTable {

	private String where;
	private String from;
	
	public AccessTable(String where, String from) {
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
