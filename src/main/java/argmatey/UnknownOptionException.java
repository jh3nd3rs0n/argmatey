package argmatey;

public final class UnknownOptionException extends
		IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String option;
	
	UnknownOptionException(final String opt) {
		super(String.format("unknown option `%s'", opt));
		this.option = opt;
	}
	
	public String getOption() {
		return this.option;
	}

}
