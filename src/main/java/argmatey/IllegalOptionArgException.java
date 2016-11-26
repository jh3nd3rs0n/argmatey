package argmatey;

public final class IllegalOptionArgException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String getMessage(
			final Option option, 
			final String optionArg, 
			final String message, 
			final Throwable cause) {
		if (message != null && !message.isEmpty()) {
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
	
	private final Option option;
	private final String optionArg;
	
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg) {
		this(opt, optArg, null, null);
	}
	
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final String message) {
		this(opt, optArg, message, null);
	}
	
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final String message, 
			final Throwable cause) {
		super(getMessage(opt, optArg, message, cause), cause);
		this.option = opt;
		this.optionArg = optArg;
	}
	
	public IllegalOptionArgException(
			final Option opt, 
			final String optArg, 
			final Throwable cause) {
		this(opt, optArg, null, cause);
	}

	public Option getOption() {
		return this.option;
	}
	
	public String getOptionArg() {
		return this.optionArg;
	}
	
}
