package pollbus.cchat.util;

import static java.util.Objects.requireNonNull;


public class KeyVal<KEY, VALUE> {
	
	private final KEY _key;
	private final VALUE _value;

	public KeyVal(KEY key, VALUE val) {
		
		requireNonNull(key); 
		requireNonNull(val);
		
		_key = key;
		_value = val;
	}	
	
	public KEY key(){
		return _key;
	}
	public VALUE value(){
		return _value;
	}
}
