package me.skiincraft.discord.core.exception;

public class LoadingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String reason;
	
	public LoadingException(String message, String reason) {
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
