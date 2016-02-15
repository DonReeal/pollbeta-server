package pollbus.cchat.pub.impl;

import static pollbus.base.util.Preconditions.requireNotNull;
import io.baratine.core.CancelHandle;
import io.baratine.core.OnActive;
import io.baratine.core.OnDestroy;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;
import io.baratine.core.SessionId;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import pollbus.base.AppException;
import pollbus.cchat.api.IChannel;
import pollbus.cchat.api.IChannelEvents;
import pollbus.cchat.pub.ChannelDt;
import pollbus.cchat.pub.ChatSessionEventsCallback;
import pollbus.cchat.pub.IChatSession;
import pollbus.session.api.IPollbusSession;
import pollbus.session.api.SessionStatusDt;
/**
 * User visible facade to all channel-chat services.
 * 
 * @author donreeal
 * file:///C:/pollbus/develop/chat/src/main/web/chat.html
 */
@Service("session:///chat-session")
public class CChatSessionFacadeBean implements IChatSession {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@SessionId
	private String _sessionId;
	
	private IPollbusSession _pollbusSession;
	
	private ChatSessionEventsCallback _sessionCallback;
	
	
	private Map<String, IChannel> _channels = new HashMap<>();
	// VS ......
	private Map<String, ChannelEventsSupport> _myChannels = new HashMap<>();
	
	
	/** http://localhost:8086/s/pod/chat-session */
	public void get(Result<SessionStatusDt> session) {
		_pollbusSession.get(session);
	}
	
	@OnActive
	public void onActive(Result<Boolean> finishedSuccessful) {
		logger.info("#onActive?");
		_pollbusSession = ServiceManager.getCurrent().lookup("/pollsession/" + _sessionId).as(IPollbusSession.class);
		finishedSuccessful.complete(_pollbusSession != null);
		_pollbusSession.get( x -> 
			logger.info(String.format(
					"Got this pollsession now:\n" 
				  + "\tstate: \t" +  x.getState()
				  + "\tuser: \t"  +  x.getUser() + "\n"))
		);
		logger.info("#onActive!");
	}
	
	@OnDestroy
	public void shutDown(){
		// TODO: leave all channels ...this.
	}
	
	/** http://localhost:8086/s/pod/chat-session?m=login&p0=don&p1=don */
	@Override
	public void login(String login, String password, Result<Boolean> result) {	
		_pollbusSession.login(login, password, result);
	}
	
	/** http://localhost:8086/s/pod/chat-session?m=logoff */
	@Override
	public void logoff(Result<Boolean> loggedOff) {
		_myChannels.values().forEach( val -> {
			val.unsubscribe();
		});
		_myChannels.clear();
		_pollbusSession.getUserName(userName -> {
			_channels.forEach((key, val) -> 
				val.disconnect(userName, Result.ignore()));
			_channels.clear();	
		});
		_pollbusSession.logoff(returned -> loggedOff.complete(true));
	}	



	/**
	 * 	http://localhost:8086/s/pod/chat-session?m=getChannels
	 */
	@Override
	public void getChannels(Result<List<String>> channelIds) {
		channelIds.complete(new ArrayList<String>(_channels.keySet()));
	}


	/**
	 * http://localhost:8086/s/pod/chat-session?m=joinChannel&p0=123
	 */
	@Override
	public void joinChannel(String channelId, Result<ChannelDt> channelDtResult) {
		
		logger.info("#joinChannel(channelId={}) ???", channelId);
		
		ServiceRef found = ServiceManager.getCurrent().lookup("public:///chat/" + channelId);
		
		if(found != null) {
			IChannel ch = found.as(IChannel.class);
			_pollbusSession.getUserName(userKey -> {
				
				ch.connect(userKey, connected -> {
					
					if(connected) {	
						_channels.put(channelId, ch);
						ch.get(data -> {
							ServiceRef channelsEventsAdress = ServiceManager.getCurrent().lookup("event:///chat/" + data.getId());
							ChannelEventsSupport s = new ChannelEventsSupport(data.getId(), channelsEventsAdress);
							_myChannels.put(channelId, s);
							s.subscribe();
							channelDtResult.complete(data);
						});	
					}
					
					else {
						channelDtResult.fail(new AppException("connecting to channel failed!"));
					}
					// ch.get(channelDtResult);					
				});
			});
		}
							
		else {
			channelDtResult.fail(new IllegalArgumentException("channel not found"));
		}
		logger.info("#joinChannel(channelId={}) !!!", channelId);
	}
	
	@Override
	public void leaveChannel(String channelId, Result<Boolean> result) {
		logger.info("leaving channel {}", channelId);
		requireNotNull(channelId);
		
		ChannelEventsSupport channelEventsSupport = _myChannels.get(channelId);
		if(channelEventsSupport != null){
			channelEventsSupport.unsubscribe();
			_myChannels.remove(channelId);
		}
		
		IChannel chnl = _channels.get(channelId);
		if(chnl != null) {
			_pollbusSession.getUserName(name -> {
				logger.info("logging out user: {}", name);
				chnl.disconnect(name, returned -> {
					logger.info("disconnected result: {}", result);
					result.complete(Boolean.TRUE);
				});
			}
				
			);
		} else {
			logger.error("usersession doenst know that user is in this channel. server did some error though..");
			result.complete(Boolean.TRUE);
		}
		
	}

	@Override
	public void msg(String channelId, String msg, Result<Boolean> result) {

		IChannel channel = _channels.get(channelId);
		if(channel != null) {
			_pollbusSession.getUserName(userName ->
				channel.msg(userName, msg, result)
			);
			
		} else {
			result.fail(new IllegalAccessError(String.format("Your not logged in channel: %s", channelId)));
		}
	};
	
	@Override
	public void setCallback(@Service ChatSessionEventsCallback callback, Result<Boolean> okay) {
		logger.info("<<<<<< #setCallback(sessionCallback={}): <<<<<<", callback);
		_sessionCallback = callback;
		okay.complete(callback != null);
		logger.info(">>>>>> #setCallback! >>>>>>");
	}
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////
	private class ChannelEventsSupport implements IChannelEvents {
		@Override
		public void onMessage(String msgId, String userName, String msg) {
			_sessionCallback.onMessage(_channelId, msgId, userName, msg);
		}
		
		@Override
		public void onUpdate(ChannelDt dt) {
			_sessionCallback.onChannelUpdate(_channelId, dt);	
		}
		
		private ServiceRef _serviceRef;
		private CancelHandle _cancel;
		private String _channelId;
		
		ChannelEventsSupport(String channelId, ServiceRef ref) {
			_channelId = channelId;
			_serviceRef = ref;
		}
		
		void subscribe() {
			logger.info("subscribing session {} to channel: {}", _sessionId, _channelId);
			_cancel = _serviceRef.subscribe(this);
		}
		
		void unsubscribe() {
			logger.info("unsubscribing {} from channel: {}", _sessionId, _channelId);
			_cancel.cancel();
		}
	}
}
	
