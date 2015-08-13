package chat;

import java.util.HashSet;
import java.util.Set;

public class Channel {

	private Set<String> userNames = new HashSet<String>();
	private String id;
	
	
	public String get_id() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	void addUser(String userName) {
		userNames.add(userName);
	}
	
	void removeUser(String userName){
		userNames.remove(userName);
	}

	public String[] getUsers() {
		return userNames.toArray(new String[userNames.size()]);	
	}
}
