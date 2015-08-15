package pollbus.cchat.service;

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
import pollbus.session.ILoginService;
import pollbus.session.user.UserDt;
/**
 * User visible facade to all channel-chat services.
 * 
 * @author donreeal
 *
 */
@Service("session:///chat-session")
public class CChatSessionFacadeBean implements IChatSession {
	
	static enum State {
		ANNONYMOUS, LOGGIN_IN, ONLINE;
	}
	
	private UserDt _user;
	private ILoginService _loginService;
	
	private State _state;
	
	@SessionId String _sessionId;
	private Map<String, IChannel> _channels = new HashMap<>();
	
	
	public void get(Result<String> sessionId){
		sessionId.complete(_sessionId);
	}
	/**
	 * http://localhost:8086/s/pod/chat-session?m=connect
	 */
	@Override
	public void connect(Result<String> sessionId) {		
		switch (_state) {
			case ANNONYMOUS: doConnect();
			case LOGGIN_IN: sessionId.fail(new IllegalStateException("cant connect while connecting!"));
			case ONLINE: sessionId.complete(_sessionId);
		}
		sessionId.complete(_sessionId);
	}
	
	
	private void doConnect() {
		_state = State.LOGGIN_IN;
		
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
	public void joinChannel(String channelId, Result<Boolean> result){
		
		ServiceRef found = ServiceManager.getCurrent().lookup("public:///chat/" + channelId);
		
		if(found != null) {
			IChannel ch = found.as(IChannel.class);
			ch.connect(_sessionId, connected-> {
				
				if(connected == true)
					_channels.put(channelId, ch);
						
				result.complete(connected);		
			});
		}
		
		else {
			result.fail(new IllegalArgumentException("channel not found"));
		}
	}
	
}
