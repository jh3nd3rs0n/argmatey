package argmatey;

public abstract class ParseResultHandler {

	protected ParseResultHandler() { }

	public void handle(final EndOfOptionsDelimiter endOfOptionsDelimiter) { }
	
	public void handle(final Object parseResult) { }
	
	public void handle(final Option option, final OptionArg optionArg) { }
	
	public void handle(final OptionOccurrence optionOccurrence) { }
	
	public void handle(final ParseResultHolder parseResultHolder) { }
	
	public void handle(final String nonparsedArg) { }
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
