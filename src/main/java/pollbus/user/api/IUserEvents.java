package pollbus.user.api;


public interface IUserEvents {	
	void onUpdate(IUser user);
	void onLogin();
	void onLogoff();
}
