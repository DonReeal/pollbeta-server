package pollbus.session.login;

import static pollbus.base.util.Preconditions.requireNotNull;
import io.baratine.core.Result;
import io.baratine.core.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pollbus.base.jamp.AppException;
import pollbus.session.impl.SessionState;

/**
 * Stateless impl
 * @author blehmann
 *
 */
@Service("/session-svc/login")
public class LoginServiceImpl implements ILoginService {
	
	private final Logger lg = LoggerFactory.getLogger(getClass());
	
	private ILoginReaderService _reader = new ILoginReaderService() {
		@Override
		public void getByKey(String key, Result<LoginData> user) {
			
			LoginData loginData = new LoginData();
			
			switch (key) {
			
			case "don":
				loginData.setLogin("don");
				loginData.setPass("don");
				user.complete(loginData);
				break;
				
			case "Alice":
				loginData.setLogin("Alice");
				loginData.setPass("Alice");
				user.complete(loginData);
				break;
				
			case "Bob":
				loginData.setLogin("Bob");
				loginData.setPass("Bob");
				user.complete(loginData);
				break;
				
			default:
				user.complete((LoginData) null);
				// String err = String.format("Userkey %s not found!", key);
				//lg.error(err + "Failing call!");
				//user.fail(new IllegalArgumentException(err));
			}			
		}
	};

	public void setUserReaderService(ILoginReaderService reader) {
		_reader = reader;
	}
	
	@Override
	public void login(String login, String password, Result<SessionState> result) {
		lg.info("<<<< #login(login={}, password=xxxxx)", login);
		
		requireNotNull(login, password);
		
		authenticate(login, password, result.from((authSuccess, sessionStateResult) -> { 
			if(authSuccess) {
				lg.info("authenticated successfully!");
				sessionStateResult.complete(SessionState.ONLINE);
			}
			else {
				lg.info("authentication unsuccessful! failing result ...");
				sessionStateResult.fail(new AppException("Login attempt unsuccessful"));
			}
		}));
		
		lg.info(">>>> #login!");
	}
	
	@Override
	public void authenticate(String login, String password, Result<Boolean> authResult) {
		
		requireNotNull(login, password);
		
		/**
		 * void myRouter(Result result){
		 * 	  MyLeafService leaf = ...;
		 *    leaf.myLeaf(result.from((v,r)->r.complete("Leaf: " + v)));
		 * }
		 *
		 */
		
		_reader.getByKey(login,
				authResult.from((loginData, doAuthResult)-> {
					if(loginData == null) {
						// no data for user
						doAuthResult.complete(false);
					}
					else
						doAuthResult.complete(password.equals(loginData.getPass()));
				})
		);
		
//		isSuccessful.from((x, success) -> {
//			_reader.getByKey(login, userInfoResult -> {
//				if(userInfoResult == null)
//					success.complete(false);
//			});
//		});
//		
//		_reader.getByKey(login, foundUser -> {
//			if(foundUser == null)
//				isSuccessful.complete(Boolean.FALSE);
//			else
//				isSuccessful.complete(password.equals(foundUser.getPass()));		
//		});
	}	
	
	@Override public void logoff(Result<SessionState> result){
		result.complete(SessionState.ANNONYMOUS);
	}
}