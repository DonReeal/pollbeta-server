package pollbus.session.impl;

import io.baratine.core.OnLookup;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;

import java.util.HashMap;
import java.util.Map;

import pollbus.session.api.IPollbusSession;
import pollbus.session.login.ILoginService;

@Service("local:///pollsession")
public class PollbusSessionFactory {

	private Map<String, ServiceRef> _managedRefs = new HashMap<>();

	@OnLookup
	public IPollbusSession onLookup(String relativeChildPath) {
		String sessionKey = relativeChildPath.substring(relativeChildPath
				.indexOf("/") + 1);

		if (_managedRefs.containsKey(sessionKey)) {
			return _managedRefs.get(sessionKey).as(IPollbusSession.class);
		} 
		
		else {
			
			PollbusSessionImpl childSession = new PollbusSessionImpl(
					sessionKey, ServiceManager.getCurrent()
							.lookup("/session-svc/login")
							.as(ILoginService.class));
			return childSession;
		}
	}

	public void onClose(String sessionKey) {
		ServiceRef serviceRef = _managedRefs.get(sessionKey);
		_managedRefs.remove(sessionKey);
		serviceRef.close();
		serviceRef = null;
	}

}
