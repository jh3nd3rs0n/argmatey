package argmatey;

public enum EndOfOptionsDelimiter implements ParseResult {

	INSTANCE;
	
	private static final String STRING = "--";
	
	@Override
	public String toString() {
		return STRING;
	}

}
