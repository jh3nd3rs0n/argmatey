package argmatey;

public enum EndOfOptionsDelimiter implements ParseResult {

	INSTANCE;
	
	public static final String STRING = "--";
	
	@Override
	public String toString() {
		return STRING;
	}

}
