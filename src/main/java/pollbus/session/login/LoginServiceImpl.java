package pollbus.session.login;

import static pollbus.base.util.Preconditions.requireNotNull;
import io.baratine.core.Result;
import io.baratine.core.Service;
import pollbus.session.api.SessionState;

/**
 * Stateless impl
 * @author blehmann
 *
 */
@Service("/session-svc/login")
public class LoginServiceImpl implements ILoginService {
	
	private ILoginReaderService _reader = new ILoginReaderService() {
		@Override
		public void getByKey(String key, Result<LoginData> user) {
			LoginData loginData = new LoginData();
			loginData.setLogin("don");
			loginData.setPass("don");

			user.complete(loginData);
		}
	};

	public void setUserReaderService(ILoginReaderService reader) {
		_reader = reader;
	}
	
	@Override
	public void login(String login, String password, Result<SessionState> result) {	
		requireNotNull(login, password);
		authenticate(login, password, success -> {
			if(success) 
				result.complete(SessionState.ONLINE);
			else
				result.complete(SessionState.ignore());
		});
	}
	
	@Override
	public void authenticate(String login, String password,
			Result<Boolean> isSuccessful) {
		requireNotNull(login, password);
		_reader.getByKey(login, foundUser -> {
			if(foundUser == null)
				isSuccessful.complete(Boolean.FALSE);
			else
				isSuccessful.complete(password.equals(foundUser.getPass()));		
		});
	}	
	
	@Override public void logoff(Result<SessionState> result){
		result.complete(SessionState.ANNONYMOUS);
	}
}