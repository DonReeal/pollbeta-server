package pollbus.chat;

import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;
import io.baratine.core.SessionId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("session:///chat-session")
public class SessionFacade implements IChatSession {
	
	@SessionId String _sessionId;	
	private Map<String, IChannel> _channels = new HashMap<>();
	
	
	/**
	 * http://localhost:8086/s/pod/chat-session?m=connect
	 */
	@Override
	public void connect(Result<String> sessionId) {
		sessionId.complete(_sessionId);
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
