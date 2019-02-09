package argmatey;

import java.util.List;

public final class ParseResult {
	
	private final Object result;
	
	ParseResult(final Object rslt) {
		this.result = rslt;
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
		if (this.result == null) {
			if (other.result != null) {
				return false;
			}
		} else if (!this.result.equals(other.result)) {
			return false;
		}
		return true;
	}

	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		if (this.result instanceof EndOfOptionsDelimiter) {
			return (EndOfOptionsDelimiter) this.result;
		}
		return null;
	}
	
	public String getNonparsedArg() {
		if (this.result instanceof String) {
			return (String) this.result;
		}
		return null;
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
		if (this.result instanceof OptionOccurrence) {
			return (OptionOccurrence) this.result;
		}
		return null;
	}

	public Object getResult() {
		return this.result;
	}
	
	public boolean hasEndOfOptionsDelimiter() {
		return this.getEndOfOptionsDelimiter() != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
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
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [result=")
			.append(this.result)
			.append("]");
		return sb.toString();
	}
	
}
