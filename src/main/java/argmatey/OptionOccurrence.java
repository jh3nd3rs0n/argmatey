package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OptionOccurrence extends ParseResult {

	private final Option option;
	private final OptionArg optionArg;
	
	OptionOccurrence(final Option opt, final OptionArg optArg) {
		this.option = opt;
		this.optionArg = optArg;
	}
	
	public Option getOption() {
		return this.option;
	}
	
	public OptionArg getOptionArg() {
		return this.optionArg;
	}
	
	public boolean hasOptionArg() {
		return this.optionArg != null;
	}
	
	public boolean hasOptionFrom(final Option opt) {
		return this.option.getAllOptions().contains(opt);
	}
	
	public boolean hasOptionOf(final String opt) {
		return this.option.toString().equals(opt);
	}
	
	public boolean hasOptionOfAnyOf(final List<String> opts) {
		for (String opt : opts) {
			if (this.hasOptionOf(opt)) {
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
