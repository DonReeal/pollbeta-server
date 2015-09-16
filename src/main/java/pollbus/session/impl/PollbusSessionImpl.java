package pollbus.session.impl;

import static pollbus.base.util.Preconditions.argCheck;
import static pollbus.base.util.Preconditions.requireNotNull;
import static pollbus.base.util.Preconditions.requireThat;
import static pollbus.base.util.Preconditions.stateCheck;
import io.baratine.core.Result;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pollbus.base.jamp.AppException;
import pollbus.base.jamp.Ok;
import pollbus.session.api.IPollbusSession;
import pollbus.session.api.SessionStatusDt;
import pollbus.session.login.ILoginService;

public class PollbusSessionImpl implements IPollbusSession {
	
	private final static Logger LOG = LoggerFactory.getLogger(PollbusSessionImpl.class);
	
	private final String _sessionKey;
	
	public PollbusSessionImpl(String sessionKey, ILoginService loginService)			{
		_sessionKey = sessionKey;
		_userName = "annonymousUser{"+ _sessionKey +"}";
		_loginService = loginService;
	}
	
	private SessionState _sessionState = SessionState.ANNONYMOUS;
	private Properties _sessionProps = new Properties();
	private ILoginService _loginService;
	private String _userName;
	
	public void setLoginService(ILoginService loginService) {	_loginService = loginService;		}
	

	@Override
	public void get(Result<SessionStatusDt> result) {
		SessionStatusDt status = new SessionStatusDt();
		status.setState(_sessionState.toString());
		status.setLoggedIn(_sessionState == SessionState.ONLINE);
		status.setUser(_userName);
		result.complete(status);
	}
	
	@Override public void login(String login, String password, Result<Boolean> result) {
		LOG.info("<<<< login({}, xxxxxx)", login);
		
		requireThat(
				stateCheck("sessionStateIsTransitioningAlready", _sessionState != SessionState.LOGGIN_IN),
				stateCheck("sessionStateIsTransitioningAlready", _sessionState != SessionState.LOGGING_OFF),				
				argCheck("Login-Must-Not-Be-Null", login != null),
				argCheck("Login-May-Not-Be-Empty", !"".equals(login)),
				argCheck("Password-Must-Not-Be-Null", password != null),
				argCheck("Password-May-Not-Be-Empty", !"".equals(password))
		);
		
		_loginService.authenticate(login, password, result.from((authSuccess, innerResult) -> {
			if(authSuccess) {
				LOG.info("authenticated successfully!");		
				_sessionState = SessionState.ONLINE;
				_userName = login;
				LOG.info("new State: {}", _sessionState.displayName());
				innerResult.complete(true);
			}
			else {
				LOG.info("authentication unsuccessful! failing result ...");
				_sessionState = SessionState.ANNONYMOUS;
				innerResult.fail(new AppException("Login attempt unsuccessful"));
			}
		}));
		
		LOG.info(">>>> login!", login);
	}

	@Override
	public void logoff(Result<Void> loggedOff) {
		_sessionProps = new Properties();
		_sessionState = SessionState.LOGGING_OFF;
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
	
}
