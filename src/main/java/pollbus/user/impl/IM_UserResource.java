package pollbus.user.impl;

import io.baratine.core.Result;
import pollbus.user.api.IUser;
import pollbus.user.api.IUserEvents;

public class IM_UserResource {
	
	private static final class User implements IUser {
		private String _id;
		private String _displayName;
		@Override public String getId() {	return _id;				}
		@Override public String getDisplayname() {	return _displayName; 	}
	}
	private IUserEvents _userEvents;
	
	// inMEMORY
	private User _user = new User();
	
	public void get(Result<IUser> result) {
		result.complete(_user);
	}
	
	public void put(IUser user, Result<Boolean> result) {
		update(user);
		result.complete(true);
	}
	
	private void update(IUser user) {
		_user._id = user.getId();
		_user._displayName = user.getDisplayname();
	}

	public void login() {
		_userEvents.onLogin();
	}
	
	public void onLogoff() {
		_userEvents.onLogoff();
	}
}
