package pollbus.session.login;

import io.baratine.core.Result;

public interface ILoginReaderService {
	void getByKey(String key, Result<LoginData> user);
}
