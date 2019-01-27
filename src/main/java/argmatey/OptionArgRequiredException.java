package argmatey;

/**
 * Thrown when an {@code Option} is not provided with a required 
 * command line option argument.
 */
public final class OptionArgRequiredException extends RuntimeException {
	
	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * The {@code Option} that was not provided with a required command line 
	 * option argument. 
	 */
	private final Option option;
	
	/**
	 * Constructs an {@code OptionArgRequiredException} with the provided 
	 * {@code Option} that was not provided with a required command line option 
	 * argument.
	 * 
	 * @param opt the provided {@code Option} that was not provided with a 
	 * required command line option argument
	 */
	OptionArgRequiredException(final Option opt) {
		super(String.format(
				"option `%s' requires an argument", 
				opt.toString()));
		this.option = opt;
	}
	
	/**
	 * Returns the {@code Option} that was not provided with a required 
	 * command line option argument.
	 * 
	 * @return the {@code Option} that was not provided with a required 
	 * command line option argument
	 */
	public Option getOption() {
		return this.option;
	}

}
