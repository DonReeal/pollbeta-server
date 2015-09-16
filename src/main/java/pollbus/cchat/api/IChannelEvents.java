package pollbus.cchat.api;

import pollbus.cchat.pub.ChannelDt;

public interface IChannelEvents {
	
	public void onMessage(String msgId, String userName, String msg);
	public void onUpdate(ChannelDt dt);
	
	// onUserJoined ..
	// onUserLeft
	// onMessageUpdated(msgId, msg)
}
