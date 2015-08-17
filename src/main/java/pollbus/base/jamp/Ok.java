package pollbus.base.jamp;

public class Ok implements Jampson {
	
	private static final long serialVersionUID = 1L;

	private static final Ok _OK = new Ok("OKAY");
	
	private String _displayValue;
	private Ok(String displayValue){_displayValue = displayValue; }
	

	
	public static Ok ok(){
		return _OK;
	}
	
	public String toString(){
		return _displayValue;
	}

}
