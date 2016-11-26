package argmatey;

public final class UnparsedArg extends ParseResult {

	private final String argument;
	
	UnparsedArg(final String arg) {
		this.argument = arg;
	}
	
	@Override
	public String toString() {
		return this.argument;
	}
	
}
