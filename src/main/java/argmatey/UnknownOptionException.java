package argmatey;

/**
 * Thrown when an unknown command line option is encountered.
 */
public final class UnknownOptionException extends RuntimeException {

	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The unknown command line option. */
	private final String option;
	
	/**
	 * Constructs an {@code UnknownOptionException} with the provided unknown 
	 * command line option.
	 * 
	 * @param opt the provided unknown command line option
	 */
	UnknownOptionException(final String opt) {
		super(String.format("unknown option `%s'", opt));
		this.option = opt;
	}
	
	/**
	 * Returns the unknown command line option.
	 * 
	 * @return the unknown command line option
	 */
	public String getOption() {
		return this.option;
	}

}
