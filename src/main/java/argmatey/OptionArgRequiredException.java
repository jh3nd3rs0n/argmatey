package argmatey;

public final class OptionArgRequiredException extends IllegalArgumentException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Option option;
	
	OptionArgRequiredException(final Option opt) {
		super(String.format(
				"option `%s' requires an argument", 
				opt.toString()));
		this.option = opt;
	}
	
	public Option getOption() {
		return this.option;
	}

}
