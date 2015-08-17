package pollbus.session.pub;

import io.baratine.core.OnActive;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.SessionId;

import javax.naming.ConfigurationException;

import pollbus.base.jamp.Ok;
import pollbus.session.api.IPollbusSession;
import pollbus.session.api.SessionState;


/**
 * Direct User-Facade to the Session api
 * 
 * An example how to use the session api from other modules ... // bind into user
 * 
 * @author donreeal
 *
 */
@Service("session:///login-session")
public class PollbusSessionFacade implements IPollbusSession  {  
	// just for testing purposes: expose all of the PollbusSession API directly to webbrowser
	
	// adding Logger breaks the SERVICE!
	//private final Logger LOG = Logger.getLogger("login-session");
	
	
	// Hooking in on Baratine interal session
	@SessionId
	private String _sessionId;
	private IPollbusSession _pollbusSession;
	
	
	@OnActive
	public void onActive(Result<Boolean> finishedSuccessful) {
		// LOG.log(Level.WARNING, "onActive Called !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		_pollbusSession = ServiceManager.getCurrent().lookup("/pollsession/" + _sessionId).as(IPollbusSession.class);
		if(_pollbusSession != null) {
			finishedSuccessful.complete(true);
		}
		else {
			finishedSuccessful.fail(new ConfigurationException("Damn its null boy"));
		}
	}
	
	public void setPollbusSession(IPollbusSession pollbusSession) {
		_pollbusSession = pollbusSession;
	}
	
	public void setSessionId(String sessionId){
		_sessionId = sessionId;
	}
	
	// ==============================================================
	/** http://localhost:8086/s/pod/login-session */
	public void get(Result<PollbusSessionFacade> sessionFacade) {sessionFacade.complete(this);} 
	// ==============================================================
	
	
	/** http://localhost:8086/s/pod/login-session?m=login&p0=don&p1=don */
	@Override
	public void login(String login, String password, Result<Boolean> result) {
		_pollbusSession.login(login, password, result);
	}

	/** http://localhost:8086/s/pod/login-session?m=logoff */
	@Override
	public void logoff(Result<Void> loggedOff) {
		_pollbusSession.logoff(loggedOff);
		
	}	


	// ==============================================================
	// SHARING STATE API
	/** http://localhost:8086/s/pod/login-session?m=getSessionKey */
	@Override
	public void getSessionKey(Result<String> sessionKeyResult) {
		_pollbusSession.getSessionKey(sessionKeyResult);
	}

	// BROKEN!!!!!
	/** http://localhost:8086/s/pod/login-session?m=getUserKey */
	@Override
	public void getUserName(Result<String> userKey) {
		_pollbusSession.getUserName(userKey);
	}

	/** http://localhost:8086/s/pod/login-session?m=getProperty&p0=fooKey */
	@Override
	public void getProperty(String key, Result<String> value) {
		_pollbusSession.getProperty(key, value);
	}

	/** http://localhost:8086/s/pod/login-session?m=setProperty&p0=fooKey&p1=fooValue */
	@Override
	public void setProperty(String key, String value, Result<Ok> isSet) { // ugly i get 
		_pollbusSession.setProperty(key, value, isSet);
	}
	
	/** http://localhost:8086/s/pod/login-session?m=getSessionState */
	@Override
	public void getSessionState(Result<SessionState> result) {
		_pollbusSession.getSessionState(result);
		
	}

}
