package pollbus.session.user;

import pollbus.session.user.IPollbusSession;

public interface IPollbusSessionSync extends IPollbusSession {

	public String getProperty(String key);

	public String getSessionKey();

	public String getUserKey();

	public Void setProperty(String key, String value);

}
