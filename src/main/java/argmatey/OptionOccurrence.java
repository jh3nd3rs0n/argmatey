package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.option == null) ? 0 : this.option.hashCode());
		result = prime * result + ((this.optionArg == null) ? 0 : this.optionArg.hashCode());
		return result;
	}
	
	public boolean hasOptionFrom(final Option opt) {
		return opt.getAllOptions().contains(this.option);
	}
	
	public boolean hasOptionOf(final String opt) {
		return opt.equals(this.option.toString());
	}
	
	public boolean hasOptionOfAnyOf(final List<String> opts) {
		for (String opt : opts) {
			if (opt.equals(this.option.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		return this.hasOptionOfAnyOf(opts);
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		opts.add(opt3);
		return this.hasOptionOfAnyOf(opts);
	}
	
	public boolean hasOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3, final String... additionalOpts) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		opts.add(opt3);
		opts.addAll(Arrays.asList(additionalOpts));
		return this.hasOptionOfAnyOf(opts);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [option=")
			.append(this.option)
			.append(", optionArg=")
			.append(this.optionArg)
			.append("]");
		return builder.toString();
	}
	
}
