package pollbus.session.service;

import static java.util.Objects.requireNonNull;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.SessionId;

import java.util.Properties;

import pollbus.session.ILoginService;
import pollbus.session.user.UserDt;

@Service("session:///login-session")
public class LoginSessionFacade implements ILoginService {
	
	private UserDt _user;
	
	void onLogin(UserDt user) {
		_user = user;
	}
	
	void onLogoff() {
		_user = null;
	}
	
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
	

	@SessionId
	private String _sessionKey;

	private LoginState _state = LoginState.ANNONYMOUS;

	/**
	 * http://localhost:8086/s/pod/login-session
	 */
	public void get(Result<String> state) {
		state.complete(_state.displayName());
	}

	/**
	 * http://localhost:8086/s/pod/login-session?m=login&p0=don&p1=don
	 */
	@Override
	public void login(String login, String password, Result<Boolean> result) {	
		
		requireNonNull(login);
		requireNonNull(password);
		
		switch (_state) {
		case ANNONYMOUS:
			_state = LoginState.LOGGIN_IN;
			doLogin(login, password, state -> {
				_state = state;				
				result.complete(state.equals(LoginState.ONLINE)); 
			});
			break;
		case LOGGIN_IN:
			result.fail(new IllegalStateException(
					"Cannot login - already logging in."));
		case ONLINE:
			result.complete(true);
		case LOGGING_OFF: result.fail(new IllegalStateException("wait for logoff ..."));
		}
	}
	
	/**
	 * http://localhost:8086/s/pod/login-session?m=logoff
	 */
	@Override
	public void logoff(Result<Void> result){
		_state = LoginState.ANNONYMOUS;
		result.complete(null);
	}
	

	@Override
	public void authenticate(String login, String password,
			Result<Boolean> isSuccessful) {
		
		requireNonNull(login);
		requireNonNull(password);
		
		if (MOCK_USERS.containsKey(login)){
			if(password.equals(MOCK_USERS.getProperty(password))) {
				isSuccessful.complete(true);
			}
		} 
		
		else {
			isSuccessful.complete(false);
		}
	}
	
	// =====================================================================================
	
	private void doLogin(String login, String password, Result<LoginState> newState) {
		authenticate(login, password, success ->{
			if(success) 
				newState.complete(LoginState.ONLINE);
			else
				newState.complete(_state);
		});
	}

	private static final Properties MOCK_USERS = new Properties();
	static {
		MOCK_USERS.put("don", "don");
		MOCK_USERS.put("alice", "alice");
		MOCK_USERS.put("bob", "bob");
	}
	
}
