package pollbus.cchat.pub;

import java.util.ArrayList;
import java.util.List;

import pollbus.base.jamp.Jampson;
/**
 * User Visible information about a channel
 * @author blehmann
 *
 */
public class ChannelDt implements Jampson {
	private static final long serialVersionUID = -3226597743208675826L;
	
	private List<String> userNames = new ArrayList<String>();
	private String id;
	private String owner;
	
	
	public String getId() 					{	return id;								}
	public List<String> getUsers() 			{	return new ArrayList<String>(userNames);}
	public String getOwner()				{	return owner;							}
	
	public void setId(String _id) 			{	this.id = _id;							}
	public void addUser(String userName) 	{	userNames.add(userName);				}
	public void removeUser(String userName) {	userNames.remove(userName);				}
	public void setOwner(String owner) 		{	this.owner = owner;						}
}
