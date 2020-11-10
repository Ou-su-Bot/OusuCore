package me.skiincraft.discord.core.event;

public interface Updateable<T> {
	
	T getUpdate();
	default T getOutDated(){ return getUpdate(); }

}
