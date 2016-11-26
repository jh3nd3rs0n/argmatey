package argmatey;

public final class NonparsedArg extends ParseResult {

	private final String argument;
	
	NonparsedArg(final String arg) {
		this.argument = arg;
	}
	
	@Override
	public String toString() {
		return this.argument;
	}
	
}
