package argmatey;

public final class ParseResult {
	
	private final Object objectValue;
	
	ParseResult(final Object objValue) {
		this.objectValue = objValue;
	}
	
	public EndOfOptionsDelimiter asEndOfOptionsDelimiter() {
		return EndOfOptionsDelimiter.class.cast(this.objectValue);
	}
	
	public Object asObjectValue() {
		return this.objectValue;
	}
	
	public OptionOccurrence asOptionOccurrence() {
		return OptionOccurrence.class.cast(this.objectValue);
	}
	
	public String asUnparsedArg() {
		return String.class.cast(this.objectValue);
	}
	
	public boolean isEndOfOptionsDelimiter() {
		return this.objectValue instanceof EndOfOptionsDelimiter;
	}
	
	public boolean isOptionOccurrence() {
		return this.objectValue instanceof OptionOccurrence;
	}
	
	public boolean isUnparsedArg() {
		return this.objectValue instanceof String;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [objectValue=")
			.append(this.objectValue)
			.append("]");
		return builder.toString();
	}

}
