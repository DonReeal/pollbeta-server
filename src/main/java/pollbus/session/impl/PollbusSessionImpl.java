package pollbus.session.impl;

import io.baratine.core.Result;

import java.util.Properties;

import pollbus.base.jamp.Ok;
import pollbus.session.api.IPollbusSession;
import pollbus.session.api.SessionState;
import pollbus.session.login.ILoginService;

public class PollbusSessionImpl implements IPollbusSession {
	
	private final String _sessionKey;
	
	public PollbusSessionImpl(String sessionKey, ILoginService loginService)			{
		_sessionKey = sessionKey;
		_userName = "annonymousUser{"+ _sessionKey +"}";
		_loginService = loginService;
	}
	
	private SessionState _sessionState;
	private Properties _sessionProps = new Properties();
	
	private ILoginService _loginService;
	
	private String _userName;
	
	public void setLoginService(ILoginService loginService) {	_loginService = loginService;		}
	
	
	@Override public void login(String login, String password, Result<Boolean> result) {
		_loginService.login(login, password, newState -> {
			if(!(newState == SessionState.ignore())) {
				_sessionState = newState;
				_userName = login;
				result.complete(true);
			}			
			else {	
				result.complete(false);
			}
		});
	}

	@Override
	public void logoff(Result<Void> loggedOff) {
		_sessionProps = new Properties();
		_userName = "annonymousUser{"+ _sessionKey +"}";
		_loginService.logoff( newState -> {
			_sessionState = newState;
			loggedOff.complete(null);
		});
	}

	@Override
	public void getSessionKey(Result<String> sessionKey) {
		sessionKey.complete(_sessionKey);		
	}

	@Override
	public void getUserName(Result<String> userKey) {
		userKey.complete(_userName);
		
	}

	@Override
	public void getProperty(String key, Result<String> value) {
		value.complete(_sessionProps.getProperty(key));		
	}

	@Override
	public void setProperty(String key, String value, Result<Ok> isSet) {
		_sessionProps.put(key, value);
		isSet.complete(Ok.ok());
	}


	@Override
	public void getSessionState(Result<SessionState> result) {
		result.complete(_sessionState);
	}

}
