package argmatey;

import java.util.List;

public final class OptionOccurrence {

	private final Option option;
	private final OptionArg optionArg;
	
	OptionOccurrence(final Option opt, final OptionArg optArg) {
		this.option = opt;
		this.optionArg = optArg;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OptionOccurrence)) {
			return false;
		}
		OptionOccurrence other = (OptionOccurrence) obj;
		if (this.option == null) {
			if (other.option != null) {
				return false;
			}
		} else if (!this.option.equals(other.option)) {
			return false;
		}
		if (this.optionArg == null) {
			if (other.optionArg != null) {
				return false;
			}
		} else if (!this.optionArg.equals(other.optionArg)) {
			return false;
		}
		return true;
	}

	public Option getOption() {
		return this.option;
	}

	public OptionArg getOptionArg() {
		return this.optionArg;
	}
	
	public boolean has(final Option opt) {
		return this.option.equals(opt);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.option == null) ? 0 : this.option.hashCode());
		result = prime * result + ((this.optionArg == null) ? 0 : this.optionArg.hashCode());
		return result;
	}
	
	public boolean hasOptionArg() {
		return this.optionArg != null;
	}
	
	public boolean hasOptionOf(final String opt) {
		return this.option.isOf(opt);
	}
	
	public boolean hasOptionOfAnyOf(final List<String> opts) {
		return this.option.isOfAnyOf(opts);
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
		return this.option.isOfAnyOf(opt1, opt2);
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3) {
		return this.option.isOfAnyOf(opt1, opt2, opt3);
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3, final String... additionalOpts) {
		return this.option.isOfAnyOf(opt1, opt2, opt3, additionalOpts);
	}
	
	public boolean hasOrHasOptionFrom(final Option opt) {
		return this.option.equalsOrIsFrom(opt);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [option=")
			.append(this.option)
			.append(", optionArg=")
			.append(this.optionArg)
			.append("]");
		return sb.toString();
	}
	
}
