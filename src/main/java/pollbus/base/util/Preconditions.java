package pollbus.base.util;

public class Preconditions {
	
	static final String ILLEGAL_NULL_MSG = "Null value not allowed here!";
	
	public static <T> T requireNotNull(T t) {
		if(t == null)
			throw new IllegalArgumentException(ILLEGAL_NULL_MSG);
		return t;
	}
	
	public static void requireNotNull(Object ... objects) {
		for(Object o : objects) {
			if(o == null)
				throw new IllegalArgumentException(ILLEGAL_NULL_MSG);
		}
	}
}
