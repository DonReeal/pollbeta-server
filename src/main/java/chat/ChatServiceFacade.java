package chat;

import io.baratine.core.OnLookup;
import io.baratine.core.Result;
import io.baratine.core.Service;

import java.util.HashSet;
import java.util.Set;
/**
 * All THE baratine API goes here ... basically its equivalent to a java ee Beanimpl
 * @author blehmann
 *
 */
@Service("public:///chat")
public class ChatServiceFacade {
	
	@OnLookup
	public ChannelFacade onLookup(String ref) {
		Channel ch = new Channel();
		ch.setId(ref.substring(1));
		return new ChannelFacade(ch);
	}

	// that stuff is being handed out by looking up under ///chat/something
	static class ChannelFacade implements IChannel {
		private final Channel _channel;
		public ChannelFacade(Channel channel) {
			_channel = channel;
		}
		/**
		 * http://localhost:8086/s/pod/chat/123?m=connect&p0=FKR
		 */
		@Override
		public void connect(String userName, Result<Boolean> success) {
			_channel.addUser(userName);
			success.complete(true);
		}
		/**
		 * http://localhost:8086/s/pod/chat/123?m=getUsers
		 * @param result
		 */
		@Override
		public void getUsers(Result<String[]> result) {
			result.complete(_channel.getUsers());
		}
		
		/**
		 * http://localhost:8086/s/pod/chat/123?m=get
		 * http://localhost:8086/s/pod/chat/123 
		 */
		@Override
		public void get(Result<Channel> result) {
			result.complete(_channel);
		}
	}
}
