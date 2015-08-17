package pollbus.session.login;

import java.util.concurrent.TimeUnit;

import io.baratine.core.Result;
import io.baratine.core.ResultFuture;

import org.junit.Test;

import pollbus.session.login.ILoginReaderService;
import pollbus.session.login.LoginData;
import pollbus.session.login.LoginServiceImpl;

public class LoginServiceImplTest {

	@Test
	public void loginTest() {
		LoginServiceImpl impl = new LoginServiceImpl();
		
		impl.setUserReaderService(new ILoginReaderService() {
			@Override
			public void getByKey(String key, Result<LoginData> user) {
				LoginData loginData = new LoginData();
				loginData.setLogin("don");
				loginData.setPass("don");

				user.complete(loginData);
			}
		});
		
		ResultFuture<Boolean> isSuccess = new ResultFuture<Boolean>();
		impl.authenticate("don", "don", isSuccess);
		isSuccess.get(1, TimeUnit.SECONDS);
	}
	
	public static ILoginService getMockLoginService(){
	LoginServiceImpl impl = new LoginServiceImpl();
		
		impl.setUserReaderService(new ILoginReaderService() {
			@Override
			public void getByKey(String key, Result<LoginData> user) {
				LoginData loginData = new LoginData();
				loginData.setLogin("don");
				loginData.setPass("don");

				user.complete(loginData);
			}
		});
		
		return impl;
		
	}
}
