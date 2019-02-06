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
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.getOption();
		}
		return null;
	}

	public OptionArg getOptionArg() {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.getOptionArg();
		}
		return null;
	}

	public OptionOccurrence getOptionOccurrence() {
		if (this.object instanceof OptionOccurrence) {
			return (OptionOccurrence) this.object;
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.object == null) ? 0 : this.object.hashCode());
		return result;
	}
	
	public boolean hasOptionFrom(final Option opt) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionFrom(opt);
		}
		return false;
	}
	
	public boolean hasOptionOf(final String opt) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionOf(opt);
		}
		return false;
	}

	public boolean hasOptionOfAnyOf(final List<String> opts) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionOfAnyOf(opts);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionOfAnyOf(opt1, opt2);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionOfAnyOf(opt1, opt2, opt3);
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3, final String... additionalOpts) {
		OptionOccurrence optionOccurrence = this.getOptionOccurrence();
		if (optionOccurrence != null) {
			return optionOccurrence.hasOptionOfAnyOf(
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
