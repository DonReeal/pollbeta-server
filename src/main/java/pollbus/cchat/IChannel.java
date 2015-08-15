package pollbus.cchat;

import java.util.List;

import pollbus.cchat.service.ChannelDt;
import io.baratine.core.Result;

public interface IChannel {

	void get(Result<ChannelDt> result);
	void connect(String userName, Result<Boolean> success);
	void getUsers(Result<List<String>> result);

}
