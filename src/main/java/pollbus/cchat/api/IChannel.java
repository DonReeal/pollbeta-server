package pollbus.cchat.api;

import java.util.List;

import pollbus.cchat.pub.ChannelDt;
import io.baratine.core.Result;

public interface IChannel {

	void get(Result<ChannelDt> result);
	void connect(String userName, Result<Boolean> success);
	void disconnect(String userName, Result<Void> success);
	void getUsers(Result<List<String>> result);
	
	void msg(String userName, String msg, Result<Boolean> result);

}
