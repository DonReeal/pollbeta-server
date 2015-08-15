package pollbus.cchat;

import static java.util.Objects.requireNonNull;
import io.baratine.core.OnLookup;
import io.baratine.core.Result;
import io.baratine.core.Service;

import java.util.List;

import pollbus.cchat.service.ChannelDt;
/**
 * All THE baratine API goes here ... basically its equivalent to a java ee Beanimpl
 * @author blehmann
 *
 */
@Service("public:///chat")
public class ChatServiceFacade {

	
	@OnLookup
	public ChannelFacade onLookup(String ref) {
		ChannelDt ch = new ChannelDt();
		ch.setId(ref.substring(1));
		return new ChannelFacade(ch);
	}

	// that stuff is being handed out by looking up under ///chat/something
	static class ChannelFacade implements IChannel {
		private final ChannelDt _channel;
		public ChannelFacade(ChannelDt channel) {
			_channel = channel;
		}
		/**
		 * http://localhost:8086/s/pod/chat/123?m=connect&p0=FKR
		 */
		@Override
		public void connect(String userName, Result<Boolean> success) {
			requireNonNull(userName);
			_channel.addUser(userName);
			success.complete(true);
		}
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
	}
}
