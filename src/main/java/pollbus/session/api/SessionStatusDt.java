package pollbus.session.api;

import pollbus.base.jamp.Jampson;

public class SessionStatusDt implements Jampson {
	private static final long serialVersionUID = 1L;
	
	private String state;
	private boolean loggedIn;
	private String user;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}
