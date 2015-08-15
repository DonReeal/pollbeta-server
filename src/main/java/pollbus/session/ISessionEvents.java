package pollbus.session;

import pollbus.session.user.UserDt;

public interface ISessionEvents {
	void onLogoff(String userKey);
	void onLogin(String session, UserDt user);
}
