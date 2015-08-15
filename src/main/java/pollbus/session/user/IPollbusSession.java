package pollbus.session.user;

import io.baratine.core.Result;

public interface IPollbusSession {
	
	void getSessionKey(Result<String> sessionKey);
	void getUserKey(Result<String> userKey);
	void getProperty(String key, Result<String> value);
	void setProperty(String key, String value, Result<Void> isSet);

}
