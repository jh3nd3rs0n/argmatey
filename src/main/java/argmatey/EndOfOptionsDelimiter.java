package argmatey;

public final class EndOfOptionsDelimiter {

	public static final EndOfOptionsDelimiter INSTANCE = new EndOfOptionsDelimiter();
	
	public static final String STRING = "--";
	
	private EndOfOptionsDelimiter() { 
		if (INSTANCE != null) { 
			throw new AssertionError("There can only be one"); 
		} 
	}
	
	@Override
	public String toString() {
		return STRING;
	}

}
