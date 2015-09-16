package pollbus.cchat.pub;

/**
 * 
 * User session callback functions to handle events that occur from all channels that are observed within a users session.
 * 
 * from all channels that he joined
 * -> session bound reactive view on channels
 * 
 * @author donreeal
 *
 */
public interface ChatSessionEventsCallback {
	
	void onMessage(String channelId, String msgId,  String userId, String message);
	void onChannelUpdate(String channelId, ChannelDt channelDefinition);
	
	void onClose(String channelId);
	
	
}