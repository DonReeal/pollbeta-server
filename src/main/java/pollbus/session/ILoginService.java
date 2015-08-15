package pollbus.session;

import io.baratine.core.Result;

public interface ILoginService {
	
	static enum LoginState {
		ANNONYMOUS("Annonymous"), LOGGIN_IN("Logging in ..."), 
		ONLINE("Online"), LOGGING_OFF("Logging off ...");
		
		String _displayName;
		LoginState(String displayName) {
			_displayName = displayName;
		}
		String displayName(){
			return _displayName;
		}
	}
	
	
	void login(String userId, String password, Result<Boolean> success);
	void logoff(Result<Void> result);
	void authenticate(String userId, String password, Result<Boolean> success);	
	

}
