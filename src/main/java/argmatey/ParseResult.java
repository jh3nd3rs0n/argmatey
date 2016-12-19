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
	
	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		EndOfOptionsDelimiter endOfOptionsDelimiter = null;
		if (this.objectValues.size() > 0) {
			Object objectValue = this.objectValues.get(0);
			if (objectValue instanceof EndOfOptionsDelimiter) {
				endOfOptionsDelimiter = (EndOfOptionsDelimiter) objectValue;
			}
		}
		return endOfOptionsDelimiter;
	}
	
	public String getNonparsedArg() {
		String nonparsedArg = null;
		if (this.objectValues.size() > 0) {
			Object objectValue = this.objectValues.get(0);
			if (objectValue instanceof String) {
				nonparsedArg = (String) objectValue;
			}
		}
		return nonparsedArg;
	}
	
	public Object getObjectValue() {
		Object objectValue = null;
		if (this.objectValues.size() > 0) {
			objectValue = this.objectValues.get(0); 
		}
		return objectValue;
	}
	
	public List<Object> getObjectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}
	
	public Option getOption() {
		Option option = null;
		if (this.objectValues.size() > 0) {
			Object objectValue = this.objectValues.get(0);
			if (objectValue instanceof Option) {
				option = (Option) objectValue;
			}
		}
		return option;
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
	
	public boolean hasEndOfOptionsDelimiter() {
		return this.getEndOfOptionsDelimiter() != null;
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
		return this.hasOption() && this.getOption().getAllOptions().contains(opt);
	}
	
	public boolean hasOptionOf(final String opt) {
		return this.hasOption() && this.getOption().toString().equals(opt);
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
			.append(" [objectValues=")
			.append(this.objectValues)
			.append("]");
		return builder.toString();
	}
	
	
}
