package pollbus.chat;

import java.util.List;

import io.baratine.core.Result;

public interface IChatSession {

	void connect(Result<String> sessionId);
	void getChannels(Result<List<String>> channelIds);
	void joinChannel(String channelId, Result<Boolean> result);

}
