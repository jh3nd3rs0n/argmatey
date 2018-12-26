package argmatey;

public enum EndOfOptionsDelimiter {

	INSTANCE;
	
	public static final String STRING = "--";
	
	@Override
	public String toString() {
		return STRING;
	}

}
