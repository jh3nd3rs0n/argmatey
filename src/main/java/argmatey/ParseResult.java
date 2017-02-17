package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ParseResult {
	
	static ParseResult newInstance(final EndOfOptionsDelimiter endOfOptsDelimiter) {
		List<Object> objectValues = new ArrayList<Object>();
		objectValues.add(endOfOptsDelimiter);
		return new ParseResult(objectValues);
	}
	
	static ParseResult newInstance(final Option opt, final OptionArg optArg) {
		List<Object> objectValues = new ArrayList<Object>();
		objectValues.add(opt);
		if (optArg != null) {
			objectValues.add(optArg);
		}
		return new ParseResult(objectValues);
	}
	
	static ParseResult newInstance(final String nonparsedArg) {
		List<Object> objectValues = new ArrayList<Object>();
		objectValues.add(nonparsedArg);
		return new ParseResult(objectValues);
	}
	
	private final List<Object> objectValues;
	
	private ParseResult(final List<Object> objValues) {
		this.objectValues = new ArrayList<Object>(objValues);
	}
	
	public EndOfOptionsDelimiter asEndOfOptionsDelimiter() {
		return (EndOfOptionsDelimiter) this.objectValues.get(0);
	}
	
	public String asNonparsedArg() {
		return (String) this.objectValues.get(0);
	}
	
	public Object asObjectValue() {
		return this.objectValues.get(0);
	}
	
	public List<Object> asObjectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}
	
	public Option asOption() {
		return (Option) this.objectValues.get(0);
	}
	
	public OptionArg getOptionArg() {
		OptionArg optionArg = null;
		if (this.objectValues.size() > 1) {
			Object objectValue = this.objectValues.get(1);
			if (objectValue instanceof OptionArg) {
				optionArg = (OptionArg) objectValue;
			}
		}
		return optionArg;
	}
	
	public boolean hasOptionArg() {
		return this.getOptionArg() != null;
	}
	
	public boolean isEndOfOptionsDelimiter() {
		return this.objectValues.get(0) instanceof EndOfOptionsDelimiter;
	}
	
	public boolean isNonparsedArg() {
		return this.objectValues.get(0) instanceof String;
	}
	
	public boolean isOption() {
		return this.objectValues.get(0) instanceof Option;
	}
	
	public boolean isOptionFrom(final Option opt) {
		return this.isOption() && this.asOption().getAllOptions().contains(opt);
	}
	
	public boolean isOptionOf(final String opt) {
		return this.isOption() && this.asOption().toString().equals(opt);
	}
	
	public boolean isOptionOfAnyOf(final List<String> opts) {
		for (String opt : opts) {
			if (this.isOptionOf(opt)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOptionOfAnyOf(final String opt1, final String opt2) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		return this.isOptionOfAnyOf(opts);
	}
	
	public boolean isOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		opts.add(opt3);
		return this.isOptionOfAnyOf(opts);
	}
	
	public boolean isOptionOfAnyOf(final String opt1, final String opt2,
			final String opt3, final String... additionalOpts) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		opts.add(opt3);
		opts.addAll(Arrays.asList(additionalOpts));
		return this.isOptionOfAnyOf(opts);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [objectValues=")
			.append(this.objectValues)
			.append("]");
		return builder.toString();
	}
	
	
}
