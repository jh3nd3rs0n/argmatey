package argmatey;

import java.util.List;

public final class ParseResultHolder {
	
	private final Object parseResult;
	
	ParseResultHolder(final Object result) {
		this.parseResult = result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ParseResultHolder)) {
			return false;
		}
		ParseResultHolder other = (ParseResultHolder) obj;
		if (this.parseResult == null) {
			if (other.parseResult != null) {
				return false;
			}
		} else if (!this.parseResult.equals(other.parseResult)) {
			return false;
		}
		return true;
	}

	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		if (this.parseResult instanceof EndOfOptionsDelimiter) {
			return (EndOfOptionsDelimiter) this.parseResult;
		}
		return null;
	}
	
	public String getNonparsedArg() {
		if (this.parseResult instanceof String) {
			return (String) this.parseResult;
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
		if (this.parseResult instanceof OptionOccurrence) {
			return (OptionOccurrence) this.parseResult;
		}
		return null;
	}

	public Object getParseResult() {
		return this.parseResult;
	}
	
	public boolean has(final Option opt) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().has(opt);
		}
		return false;
	}
	
	public boolean hasEndOfOptionsDelimiter() {
		return this.getEndOfOptionsDelimiter() != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.parseResult == null) ? 
				0 : this.parseResult.hashCode());
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
	
	public boolean hasOrHasOptionFrom(final Option opt) {
		if (this.hasOptionOccurrence()) {
			return this.getOptionOccurrence().hasOrHasOptionFrom(opt);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [parseResult=")
			.append(this.parseResult)
			.append("]");
		return sb.toString();
	}
	
}
