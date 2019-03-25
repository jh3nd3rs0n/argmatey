package argmatey;

/**
 * Thrown when an {@code Option} is provided with a command line option 
 * argument that is illegal or inappropriate.
 */
public final class IllegalOptionArgException extends RuntimeException {

	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the detail message based on the provided {@code Option} of the 
	 * illegal or inappropriate command line option argument, the provided 
	 * illegal or inappropriate command line option argument, the specified 
	 * detail message, and the provided cause.
	 * 
	 * If the specified detail message is not {@code null}, the specified 
	 * detail message is returned. Otherwise, a detail message is created. If 
	 * the provided cause is not {@code null}, its {@code String} 
	 * representation is appended to that created detail message.  
	 * 
	 * @param option the provided {@code Option} of the illegal or 
	 * inappropriate command line option argument
	 * @param optionArg the provided illegal or inappropriate command line 
	 * option argument
	 * @param message the specified detail message (can be {@code null})
	 * @param cause the provided cause (can be {@code null})
	 * @return the specified detail message or a created detail message 
	 * appended with or without a {@code String} representation of the provided 
	 * cause 
	 */
	private static String getMessage(
			final Option option, 
			final String optionArg, 
			final String message, 
			final Throwable cause) {
		if (message != null) {
			return message;
		}
		StringBuilder sb = new StringBuilder(String.format(
				"illegal option argument `%s' for option `%s'", 
				optionArg, 
				option));
		if (cause != null) {
			sb.append(": ");
			sb.append(cause.toString());
		}
		return sb.toString();
	}
	
	/** 
	 * The {@code Option} of the illegal or inappropriate command line option 
	 * argument.
	 */
	private final Option option;
	
	/** The illegal or inappropriate command line option argument. */
	private final String optionArg;
	
	/**
	 * Constructs an {@code IllegalOptionArgException} with the provided 
	 * {@code Option} of the illegal or inappropriate command line option 
	 * argument and the provided illegal or inappropriate command line option 
	 * argument
	 * 
	 * @param opt the provided {@code Option} of the illegal or inappropriate 
	 * command line option argument
	 * @param optArg the provided illegal or inappropriate command line option 
	 * argument
	 */
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg) {
		this(opt, optArg, null, null);
	}
	
	/**
	 * Constructs an {@code IllegalOptionArgException} with the provided 
	 * {@code Option} of the illegal or inappropriate command line option 
	 * argument, the provided illegal or inappropriate command line option 
	 * argument, and the specified detail message. 
	 * 
	 * @param opt the provided {@code Option} of the illegal or inappropriate 
	 * command line option argument
	 * @param optArg the provided illegal or inappropriate command line option 
	 * argument
	 * @param message the specified detail message (can be {@code null})
	 */
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final String message) {
		this(opt, optArg, message, null);
	}
	
	/**
	 * Constructs an {@code IllegalOptionArgException} with the provided 
	 * {@code Option} of the illegal or inappropriate command line option 
	 * argument, the provided illegal or inappropriate command line option 
	 * argument, the specified detail message, and the provided cause.
	 * 
	 * @param opt the provided {@code Option} of the illegal or inappropriate 
	 * command line option argument
	 * @param optArg the provided illegal or inappropriate command line option 
	 * argument
	 * @param message the specified detail message (can be {@code null})
	 * @param cause the provided cause (can be {@code null})
	 */
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final String message, 
			final Throwable cause) {
		super(getMessage(opt, optArg, message, cause), cause);
		this.option = opt;
		this.optionArg = optArg;
	}
	
	/**
	 * Constructs an {@code IllegalOptionArgException} with the provided 
	 * {@code Option} of the illegal or inappropriate command line option 
	 * argument, the provided illegal or inappropriate command line option 
	 * argument, and the provided cause.
	 * 
	 * @param opt the provided {@code Option} of the illegal or inappropriate 
	 * command line option argument
	 * @param optArg the provided illegal or inappropriate command line option 
	 * argument
	 * @param cause the provided cause (can be {@code null})
	 */
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final Throwable cause) {
		this(opt, optArg, null, cause);
	}

	/**
	 * Returns the {@code Option} of the illegal or inappropriate command line 
	 * option argument.
	 *  
	 * @return the {@code Option} of the illegal or inappropriate command line 
	 * option argument
	 */
	public Option getOption() {
		return this.option;
	}
	
	/**
	 * Returns the illegal or inappropriate command line option argument.
	 * 
	 * @return the illegal or inappropriate command line option argument
	 */
	public String getOptionArg() {
		return this.optionArg;
	}
	
}
