package me.skiincraft.discord.core.event;

public enum EventPriority {
	
	LOW(0), NORMAL(1), HIGH(2), REALTIME(3);
	
	private int intensity;
	
	EventPriority(int intensity) {
		this.intensity = intensity;
	}
	
	public int getIntensity() {
		return intensity;
	}
}
