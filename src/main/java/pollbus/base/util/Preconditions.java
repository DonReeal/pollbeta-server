package pollbus.base.util;

import java.util.Stack;

public class Preconditions {
	
	static final String ILLEGAL_NULL_MSG = "Null value not allowed here!";
	
	public static <T> T requireNotNull(T t) {
		if(t == null)
			throw new IllegalArgumentException(ILLEGAL_NULL_MSG);
		return t;
	}
	
	public static void requireNotNull(Object... objects) {
		for(Object o : objects) {
			if(o == null)
				throw new IllegalArgumentException(ILLEGAL_NULL_MSG);
		}
	}
	
	public static void requireThat(boolean b){
		requireThat(null, b);
	}
	
	public static void requireThat(Precondition ... pres) {
		
		Stack<IllegalStateException> stack = new Stack<>();
		
		for(Precondition p : pres) {
			try { requireThat(p.text, p.outcome);				
			} catch(IllegalStateException e) {
				stack.push(e);
			}			
		}
		
		if(!stack.isEmpty())
			throw new IllegalStateException(buildString(stack));
	}

	private static void requireThat(String name, boolean b) {
		if(!b) {
			String message = (name == null)? "Condition required to be true!" : String.format("Unsatisfied condition: %s", name); 
			throw new IllegalStateException(message);
		}
	}
	
	

	private static String buildString(Stack<IllegalStateException> stack) {
		StringBuilder sb = new StringBuilder("\n----- Multiple Causes Detected -------\n");
		stack.forEach(ex -> sb.append("\t").append(ex.getMessage()).append("\n"));
		sb.append("--------------------------------------\n");
		return String.format(sb.toString());
	}
	
	public static final class Precondition {
		private final String text;
		private final boolean outcome;
		private final Class<? extends Exception> exCls;
		private Precondition(String s, boolean b, Class<? extends Exception> cls){
			this.text = s;
			this.outcome = b;
			this.exCls = cls;
		}
	}
	
	public static Precondition argCheck(String name, boolean b){
		return new Precondition(name, b, IllegalArgumentException.class);
	}
	
	public static Precondition stateCheck(String name, boolean b) {
		return new Precondition(name, b, IllegalStateException.class);
	}
}
