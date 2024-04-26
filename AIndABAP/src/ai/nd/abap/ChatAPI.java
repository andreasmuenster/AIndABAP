package ai.nd.abap;

public abstract class ChatAPI {
	
	public ChatAPI() { }
	
	public void processRequest(String requestString, Settings settingsHandler) { };
	
	public String getRequestResponse() { return null; }		
	
}