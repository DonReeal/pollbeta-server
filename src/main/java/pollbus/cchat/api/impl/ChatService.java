package pollbus.cchat.api.impl;

import static java.util.Objects.requireNonNull;
import io.baratine.core.Modify;
import io.baratine.core.OnActive;
import io.baratine.core.OnLookup;
import io.baratine.core.OnSave;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;

import java.util.List;

import javax.naming.ConfigurationException;

import org.omg.CORBA._PolicyStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pollbus.base.jamp.AppException;
import pollbus.cchat.api.IChannel;
import pollbus.cchat.api.IChannelEvents;
import pollbus.cchat.pub.ChannelDt;
/**
 * All THE baratine API goes here ... basically its equivalent to a java ee Beanimpl
 * @author blehmann
 *
 */
@Service("public:///chat")
public class ChatService {	
	
	@OnLookup
	public ChannelFacade onLookup(String ref) {
		ChannelDt ch = new ChannelDt();
		ch.setId(ref.substring(1));
		return new ChannelFacade(ch);
	}

	// that stuff is being handed out by looking up under ///chat/something
	
	// publisher ...
	static class ChannelFacade implements IChannel {
		
		private final Logger logger = LoggerFactory.getLogger(ChannelFacade.class.getName());
		
		
		Long messageCount = Long.valueOf(0);
		
		private final ChannelDt _channel;
		
		public ChannelFacade(ChannelDt channel) {
			_channel = channel;
		}
		
		private IChannelEvents _eventsPublisher;
		private ServiceRef _eventsBindingRef;
		
		
		@OnActive
		public void onActive(Result<Boolean> finishedResult){
			logger.info("<<<<<< onActive: <<<<<<");
			_eventsBindingRef = ServiceManager.getCurrent().lookup("event:///chat/" + _channel.getId());
			_eventsPublisher =  _eventsBindingRef.as(IChannelEvents.class);
			
			if(_eventsBindingRef == null || _eventsPublisher == null ) {
				finishedResult.fail(new ConfigurationException(
						String.format("something went wrong configurint chat channel @onActive: %s, %s", _eventsBindingRef, _eventsPublisher)));
			}
			
			else
				finishedResult.complete(true);
			
			logger.info(">>>>>> onActive! >>>>>>");
		}
		
		
		/**
		 * http://localhost:8086/s/pod/chat/123?m=connect&p0=FKR
		 */
		@Override
		public void connect(String userName, Result<Boolean> success) {
			requireNonNull(userName);
			
			if(_channel.getUsers().contains(userName)) {
				success.complete(false);
			}
			
			else {
				_channel.addUser(userName);
				_eventsPublisher.onUpdate(_channel);
				success.complete(true);
			}
		}
		
		@Override
		public void disconnect(String userName, io.baratine.core.Result<Void> success) {
			requireNonNull(userName);
			_channel.removeUser(userName);
			_eventsPublisher.onUpdate(_channel);
			success.complete(null);
		};
		
		
		/**
		 * http://localhost:8086/s/pod/chat/123?m=getUsers
		 * @param result
		 */
		@Override
		public void getUsers(Result<List<String>> result) {
			result.complete(_channel.getUsers());
		}
		
		/**
		 * http://localhost:8086/s/pod/chat/123?m=get
		 * http://localhost:8086/s/pod/chat/123 
		 */
		@Override
		public void get(Result<ChannelDt> result) {
			result.complete(_channel);
		}

		/** http://localhost:8086/s/pod/chat/123?m=msg&p0=don&p1=Heyo */
		@Override
		public void msg(String userName, String msg, Result<Boolean> result) {
			logger.info("<<<<<< msg(userName={}, msg={})", userName, msg);
			_eventsPublisher.onMessage((messageCount++).toString(), userName, msg);
			result.complete(true);
			logger.info(">>>>>> msg()!");
		}
	}
}
