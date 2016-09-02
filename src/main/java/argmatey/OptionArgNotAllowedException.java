package argmatey;

public final class OptionArgNotAllowedException extends
		IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Option option;
	
	OptionArgNotAllowedException(final Option opt) {
		super(String.format(
				"option `%s' does not allow an argument", 
				opt.toString()));
		this.option = opt;
	}
	
	public Option getOption() {
		return this.option;
	}

}
