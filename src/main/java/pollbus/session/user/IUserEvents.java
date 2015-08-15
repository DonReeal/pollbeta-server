package pollbus.session.user;

public interface IUserEvents {
	
	void onLogin(String userId);
	void onLogoff(String userId);

}
