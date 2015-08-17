package pollbus.session.login;

import pollbus.session.api.SessionState;
import io.baratine.core.Result;

public interface ILoginService {	
	
	void login(String login, String password, Result<SessionState> result);
	void logoff(Result<SessionState> result);	
	void authenticate(String userId, String password, Result<Boolean> success);
		
	

}
