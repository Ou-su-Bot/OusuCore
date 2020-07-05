package me.skiincraft.discord.core.reactions;

public class ReactionObject {
	
	private Object object;
	private int ordem;
	
	public ReactionObject(Object object, int ordem) {
		this.object = object;
		this.ordem = ordem;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	
}
