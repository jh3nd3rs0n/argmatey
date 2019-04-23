package argmatey;

/**
 * Enum singleton that represents the delimiter signaling the end of the 
 * command line options.
 */
public enum EndOfOptionsDelimiter {

	/** The single instance. */
	INSTANCE;
	
	/** 
	 * The {@code String} representation of this {@code EndOfOptionsDelimiter} 
	 * as it would appear on the command line.
	 */
	private static final String STRING = "--";
	
	/**
	 * Returns the {@code String} representation of this 
	 * {@code EndOfOptionsDelimiter} as it would appear on the command line 
	 * ("--").
	 * 
	 * @return the {@code String} representation of this 
	 * {@code EndOfOptionsDelimiter} as it would appear on the command line 
	 * ("--")
	 */
	@Override
	public String toString() {
		return STRING;
	}

}
