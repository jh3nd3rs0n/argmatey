package argmatey;

import java.util.List;

public final class ParseResult {
	
	private final Object object;
	
	ParseResult(final Object obj) {
		this.object = obj;
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
		if (this.object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!this.object.equals(other.object)) {
			return false;
		}
		return true;
	}

	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		if (this.object instanceof EndOfOptionsDelimiter) {
			return (EndOfOptionsDelimiter) this.object;
		}
		return null;
	}
	
	public String getNonparsedArg() {
		if (this.object instanceof String) {
			return (String) this.object;
		}
		return null;
	}
	
	public Object getObject() {
		return this.object;
	}
	
	public Option getOption() {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().getOption();
		}
		return null;
	}

	public OptionArg getOptionArg() {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().getOptionArg();
		}
		return null;
	}

	public OptionOccurrence getOptionOccurrence() {
		if (this.object instanceof OptionOccurrence) {
			return (OptionOccurrence) this.object;
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
		result = prime * result + ((this.object == null) ? 0 : this.object.hashCode());
		return result;
	}
	
	public boolean hasNonparsedArg() {
		return this.getNonparsedArg() != null;
	}
	
	public boolean hasOption() {
		return this.getOption() != null;
	}
	
	public boolean hasOptionArg() {
		return this.getOptionArg() != null;
	}
	
	public boolean hasOptionFrom(final Option opt) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionFrom(opt);
		}
		return false;
	}
	
	public boolean hasOptionOccurrence() {
		return this.getOptionOccurrence() != null;
	}
	
	public boolean hasOptionOf(final String opt) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionOf(opt);
		}
		return false;
	}

	public boolean hasOptionOfAnyOf(final List<String> opts) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionOfAnyOf(opts);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionOfAnyOf(opt1, opt2);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionOfAnyOf(
					opt1, opt2, opt3);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3, final String... additionalOpts) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOptionOfAnyOf(
					opt1, opt2, opt3, additionalOpts);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [object=")
			.append(this.object)
			.append("]");
		return builder.toString();
	}
	
}
