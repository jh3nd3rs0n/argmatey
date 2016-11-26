package argmatey;

public final class EndOfOptionsDelimiter extends ParseResult {

	public static final EndOfOptionsDelimiter INSTANCE = new EndOfOptionsDelimiter();
	
	public static final String STRING_VALUE = "--";
	
	private EndOfOptionsDelimiter() { 
		if (INSTANCE != null) { 
			throw new AssertionError("There can only be one"); 
		} 
	}
	
	@Override
	public String toString() {
		return STRING_VALUE;
	}

}
