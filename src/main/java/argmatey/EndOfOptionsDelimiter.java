package argmatey;

public enum EndOfOptionsDelimiter {

	INSTANCE;
	
	private static final String STRING = "--";
	
	@Override
	public String toString() {
		return STRING;
	}

}
