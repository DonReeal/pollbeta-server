package chat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	public List<String> getUsers() {
		return new ArrayList<String>(userNames);
	}
}
