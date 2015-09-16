package pollbus.session.impl;


public enum SessionState {

	ANNONYMOUS("Annonymous"), 
	LOGGIN_IN("Logging in ..."), 
	ONLINE("Online"),
	LOGGING_OFF("Logging off ...");

	final String _displayName;
	boolean _isTransitioning;
	SessionState _toState = null;

	private SessionState(String displayName) {
		_displayName = displayName;
	}

	public String displayName() {
		return _displayName;
	}
}
