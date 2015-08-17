package pollbus.cchat.service;

import io.baratine.core.OnLookup;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;
import io.baratine.core.SessionId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pollbus.cchat.IChannel;
import pollbus.cchat.IChatSession;
import pollbus.session.api.IPollbusSession;
import pollbus.session.login.ILoginService;
import pollbus.session.login.LoginData;
import pollbus.session.pub.PollbusSessionFacade;
/**
 * User visible facade to all channel-chat services.
 * 
 * @author donreeal
 *
 */
@Service("session:///chat-session")
public class CChatSessionFacadeBean implements IChatSession {
	
	private Map<String, IChannel> _channels = new HashMap<>();
	
	@SessionId
	private String _sessionId;
	private IPollbusSession _pollbusSession;
	
	
	
	@OnLookup
	public void onLookup() {
		_pollbusSession = ServiceManager.getCurrent().lookup("/pollsession/" + _sessionId).as(IPollbusSession.class);
	}
	
	/**
	 * http://localhost:8086/s/pod/chat-session?m=connect
	 */
	@Override
	public void connect(Result<String> sessionId) {	
		// _pollbusSession.login(login, password);
	}


	/**
	 * 	http://localhost:8086/s/pod/chat-session?m=getChannels
	 */
	@Override
	public void getChannels(Result<List<String>> channelIds){
		channelIds.complete(new ArrayList<String>(_channels.keySet()));
	}


	/**
	 * http://localhost:8086/s/pod/chat-session?m=joinChannel&p0=123
	 */
	@Override
	public void joinChannel(String channelId, Result<Boolean> channelConnection) {
		
		ServiceRef found = ServiceManager.getCurrent().lookup("public:///chat/" + channelId);
		
		if(found != null) {
			IChannel ch = found.as(IChannel.class);
			_pollbusSession.getUserName(userKey -> 
				ch.connect(userKey, channelConnection)
			);
		}
							
		else {
			channelConnection.fail(new IllegalArgumentException("channel not found"));
		}
	}
}
	
