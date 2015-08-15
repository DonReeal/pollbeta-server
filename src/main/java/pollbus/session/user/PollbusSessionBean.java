package pollbus.session.user;

import io.baratine.core.OnLookup;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceRef;

import java.util.HashMap;
import java.util.Map;

@Service("local:///pollsession")
public class PollbusSessionBean {

	private Map<String, ServiceRef> _managedRefs = new HashMap<>();

	@OnLookup
	public IPollbusSession onLookup(String relativeChildPath) {
		String sessionKey = relativeChildPath.substring(relativeChildPath.indexOf("/") + 1);
		
		if (_managedRefs.containsKey(sessionKey)) {
			return _managedRefs.get(sessionKey).as(IPollbusSession.class);
		}
		else {
			SessionData initialData = new SessionData(sessionKey);
			return new PollbusSessionManagedResource(initialData);
		}
	}
	
	void onSessionClose(String sessionKey) {
		ServiceRef serviceRef = _managedRefs.get(sessionKey);
		_managedRefs.remove(sessionKey);
		serviceRef.close();
		serviceRef = null;
	}

	// public SessionData get(String sessionId, String userId){
	// return new SessionData(userId, sessionId);
	// }

	static class PollbusSessionManagedResource implements IPollbusSession {
		
		private SessionData _data;

		// if this isnt pure in Memory here (data provided by a service) then use instead:
		// private IPollbusSession _session;
		// public PollbusSessionManagedResource (IPollbusSession session) {
		// _session = session;
		// }

		private PollbusSessionManagedResource(SessionData initialData) {
			_data = initialData;
		}

		@Override
		public void getSessionKey(Result<String> sessionKey) {
			sessionKey.complete(_data.getSessionKey());
		}

		@Override
		public void getUserKey(Result<String> userKey) {
			userKey.complete(_data.getUserKey());
		}

		@Override
		public void getProperty(String key, Result<String> valueResult) {
			valueResult.complete(_data.getProperty(key));
		}

		@Override
		public void setProperty(String key, String value, Result<Void> isSet) {
			isSet.complete(_data.put(key, value));
		}
		
		public void close(){
			_data = null;
		}
		
	}

}
