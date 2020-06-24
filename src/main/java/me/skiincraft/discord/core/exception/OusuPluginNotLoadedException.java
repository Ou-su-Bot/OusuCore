package me.skiincraft.discord.core.exception;

public class OusuPluginNotLoadedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String reason;
	
	public OusuPluginNotLoadedException(String message, String reason) {
		this.message = message;
		this.reason = reason;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
	public String getReason() {
		return reason;
	}
}
