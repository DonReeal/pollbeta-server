package pollbus.session.user;

import java.util.Properties;

/**
 * NOT THREADSAFE - for single threaded service use only
 * @author blehmann
 */
public class SessionData  {
	
	private final String _sessionKey;
	private String _userKey;
	
	SessionData(String sessionKey) {
		_sessionKey = sessionKey;
	}
	
	void setUserKey(String userKey)				{   _userKey = userKey;						}
	
	public String getSessionKey()				{	return _sessionKey;						}
	public String getUserKey()					{	return _userKey;						}
	public String getProperty(String key)		{ 	return _props.getProperty(key, null);	}
	
	
	// =================== data =====================
	private Properties _props = new Properties();

	public Void put(String key, String value) {
		_props.setProperty(key, value);
		return null;
	}
	
	public Void remove(String key) {
		_props.remove(key);
		return null;
	}
	
}
