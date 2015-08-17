package pollbus.session.api;

public enum SessionState {

	ANNONYMOUS("Annonymous"), 
	LOGGIN_IN("Logging in ..."), 
	ONLINE("Online"), 
	LOGGING_OFF("Logging off ...");

	String _displayName;

	private SessionState(String displayName) {
		_displayName = displayName;
	}

	public String displayName() {
		return _displayName;
	}

	public static SessionState ignore() {
		return null;
	}

}
