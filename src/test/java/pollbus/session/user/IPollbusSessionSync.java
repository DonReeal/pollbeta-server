package pollbus.session.user;

import pollbus.base.jamp.Ok;
import pollbus.session.api.IPollbusSession;

public interface IPollbusSessionSync extends IPollbusSession {

	public String getProperty(String key);

	public String getSessionKey();

	public String getUserName();

	public Ok setProperty(String key, String value);

	public boolean login(String login, String password);

}
