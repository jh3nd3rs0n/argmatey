package argmatey;

/**
 * Thrown when an {@code Option} is provided with a command line option argument 
 * that is not allowed.
 */
public final class OptionArgNotAllowedException extends RuntimeException {
	
	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * The {@code Option} that was provided with a command line option argument 
	 * that is not allowed.
	 */
	private final Option option;
	
	/**
	 * Constructs an {@code OptionArgNotAllowedException} with the provided 
	 * {@code Option} that was provided with a command line option argument that 
	 * is not allowed.
	 *  
	 * @param opt the provided {@code Option} that was provided with a command 
	 * line option argument that is not allowed
	 */
	OptionArgNotAllowedException(final Option opt) {
		super(String.format(
				"option `%s' does not allow an argument", 
				opt.toString()));
		this.option = opt;
	}
	
	/**
	 * Returns the {@code Option} that was provided with a command line option 
	 * argument that is not allowed.
	 * 
	 * @return the {@code Option} that was provided with a command line option 
	 * argument that is not allowed
	 */
	public Option getOption() {
		return this.option;
	}

}
