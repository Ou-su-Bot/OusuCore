package me.skiincraft.discord.core.sqlobjects;

public class DBObject {

	private String where;
	private String from;
	
	public DBObject(String where, String from) {
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
