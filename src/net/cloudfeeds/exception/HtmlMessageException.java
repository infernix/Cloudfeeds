package net.cloudfeeds.exception;

public class HtmlMessageException extends Exception {
	
	private final String htmlMessage;
	
	

	public HtmlMessageException(String message, String htmlMessage, Throwable cause) {
		super(message, cause);
		this.htmlMessage = htmlMessage;
	}
	
	public HtmlMessageException(String message, String htmlMessage) {
		super(message);
		this.htmlMessage = htmlMessage;
	}



	public String getHtmlMessage()
	{
		return htmlMessage;
	}
}
