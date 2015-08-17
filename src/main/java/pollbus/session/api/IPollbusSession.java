package pollbus.session.api;

import pollbus.base.jamp.Ok;

import io.baratine.core.Result;

public interface IPollbusSession {

	void login(String login, String password, Result<Boolean> result);
	void logoff(Result<Void> loggedOff);
	
	void getSessionState(Result<SessionState> result);
	void getSessionKey(Result<String> sessionKey);
	void getUserName(Result<String> userKey);
	void getProperty(String key, Result<String> value);
	void setProperty(String key, String value, Result<Ok> isSet);

}
