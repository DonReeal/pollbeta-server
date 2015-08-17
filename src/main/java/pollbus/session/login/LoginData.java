package pollbus.session.login;

/**
 * public users data
 * 
 * @author donreeal
 *
 */
public class LoginData {
	
	private String loginKey;
	private String loginPass;
	
	public String getLogin() {
		return loginKey;
	}
	
	public String getPass() {
		return loginPass;
	}
	
	void setPass(String pass) {
		loginPass = pass;
	}
	
	void setLogin(String key) {
		loginKey = key;
	}
}
