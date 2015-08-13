package pollbus.chat.service;

import java.util.ArrayList;
import java.util.List;

import pollbus.jamp.Jampson;

public class ChannelDt implements Jampson {
	private static final long serialVersionUID = -3226597743208675826L;
	
	private List<String> userNames = new ArrayList<String>();
	private String id;
	private String owner;
	
	public String get_id() 			{	return id;								}
	public List<String> getUsers() 	{	return new ArrayList<String>(userNames);}
	public String getOwner()		{	return owner;							}
	
	void setId(String _id) 			{	this.id = _id;							}
	void addUser(String userName) 	{	userNames.add(userName);				}
	void removeUser(String userName){	userNames.remove(userName);				}
}
