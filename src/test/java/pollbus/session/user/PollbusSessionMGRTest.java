package pollbus.session.user;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import io.baratine.core.Lookup;
import io.baratine.core.ServiceManager;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import pollbus.session.impl.PollbusSessionFactory;
import pollbus.session.impl.PollbusSessionImpl;
import pollbus.session.login.*;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services={PollbusSessionFactory.class, LoginServiceImpl.class})
public class PollbusSessionMGRTest {
	
	private static final String SESSIONKEY_INITIALDATA = "initialData";
	
	@Inject
	@Lookup("/pollsession/" + SESSIONKEY_INITIALDATA)
	IPollbusSessionSync _session;
	
	@Test
	public void testInitialData(){
		String sessionKey = _session.getSessionKey();
		assertThat(sessionKey, equalTo(SESSIONKEY_INITIALDATA));
		assertThat(_session.getUserName(), containsString(sessionKey));
	}
	
	@Test
	public void settingSessionProps() {
		_session.setProperty("FK", "YOU");
		assertThat(_session.getProperty("FK"), equalTo("YOU"));
	}
	
	@Inject
	private ServiceManager _services;

	
	
	@Test
	public void findable(){
		
		ILoginService mockLogin = LoginServiceImplTest.getMockLoginService();
		PollbusSessionImpl impl = new PollbusSessionImpl("yoyooyoyoyoyo", mockLogin);
		IPollbusSessionSync yoyo = _services.newService().service(impl).build().as(IPollbusSessionSync.class);
		
		
		
		//IPollbusSessionSync yoyo =  _services.lookup("/pollsession/ayayayayay").as(IPollbusSessionSync.class);
		boolean success = yoyo.login("don", "don");
		assertTrue(success);
	}

}
