package pollbus.session.user;

public interface ISessionEvents {
	void onLogoff(String userKey);
	void onLogin(String userKey);
	void onClose(String sessionId);
}
