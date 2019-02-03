package argmatey;

public final class ParseResult {
	
	private final Object objectValue;
	
	ParseResult(final Object objValue) {
		this.objectValue = objValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ParseResult)) {
			return false;
		}
		ParseResult other = (ParseResult) obj;
		if (this.objectValue == null) {
			if (other.objectValue != null) {
				return false;
			}
		} else if (!this.objectValue.equals(other.objectValue)) {
			return false;
		}
		return true;
	}
	
	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		if (this.objectValue instanceof EndOfOptionsDelimiter) {
			return (EndOfOptionsDelimiter) this.objectValue;
		}
		return null;
	}
	
	public String getNonparsedArg() {
		if (this.objectValue instanceof String) {
			return (String) this.objectValue;
		}
		return null;
	}
	
	public Object getObjectValue() {
		return this.objectValue;
	}

	public OptionOccurrence getOptionOccurrence() {
		if (this.objectValue instanceof OptionOccurrence) {
			return (OptionOccurrence) this.objectValue;
		}
		return null;
	}

	public boolean hasEndOfOptionsDelimiter() {
		return this.getEndOfOptionsDelimiter() != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.objectValue == null) ? 0 : this.objectValue.hashCode());
		return result;
	}
	
	public boolean hasNonparsedArg() {
		return this.getNonparsedArg() != null;
	}
	
	public boolean hasOptionOccurrence() {
		return this.getOptionOccurrence() != null;
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
