package pollbus.cchat.pub;

import java.util.List;

import io.baratine.core.Result;

public interface IChatSession {
	
	void setCallback(ChatSessionEventsCallback callback, Result<Boolean> okay);
	
	void login(String login, String password, Result<Boolean> okay);
	void logoff(Result<Boolean> loggedOff);

	
	void joinChannel(String channelId, Result<ChannelDt> result);
	void leaveChannel(String channelId, Result<Boolean> result);
		
	void getChannels(Result<List<String>> channelIds);
	
	void msg(String channelId, String msg, Result<Boolean> result);

}
