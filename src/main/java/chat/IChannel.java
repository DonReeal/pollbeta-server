package chat;

import io.baratine.core.Result;

public interface IChannel {

	void get(Result<Channel> result);
	void connect(String userName, Result<Boolean> success);
	void getUsers(Result<String[]> result);

}
