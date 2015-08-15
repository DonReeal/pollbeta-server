package pollbus.session.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import io.baratine.core.Lookup;
import io.baratine.core.ServiceRef;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.caucho.junit.ConfigurationBaratine;
import com.caucho.junit.RunnerBaratine;

@RunWith(RunnerBaratine.class)
@ConfigurationBaratine(services={PollbusSessionBean.class})
public class PollbusSessionMGRTest {
	
	private static final String SESSIONKEY_INITIALDATA = "initialData";
	
	@Inject
	@Lookup("/pollsession/" + SESSIONKEY_INITIALDATA)
	IPollbusSessionSync _session;
	
	@Test
	public void testInitialData(){
		String sessionKey = _session.getSessionKey();
		assertThat(sessionKey, equalTo(SESSIONKEY_INITIALDATA));
		assertThat(_session.getUserKey(), nullValue());
	}
	
	@Test
	public void settingSessionProps() {
		_session.setProperty("FK", "YOU");
		assertThat(_session.getProperty("FK"), equalTo("YOU"));
	}

}
