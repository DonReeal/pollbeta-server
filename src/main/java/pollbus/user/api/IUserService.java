package pollbus.user.api;

import io.baratine.core.Result;

public interface IUserService {
	public void authenticate(String userId, String digest, Result<IUser> result);
}
