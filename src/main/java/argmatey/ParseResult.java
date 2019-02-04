package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ParseResult {
	
	static ParseResult newInstance(
			final EndOfOptionsDelimiter endOfOptionsDelimiter) {
		List<Object> objectValues = new ArrayList<Object>();
		objectValues.add(endOfOptionsDelimiter);
		return new ParseResult(objectValues);
	}
	
	static ParseResult newInstance(
			final Option option, final OptionArg optionArg) {
		List<Object> objectValues = new ArrayList<Object>();
		objectValues.add(option);
		if (optionArg != null) {
			objectValues.add(optionArg);
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
		if (this.objectValues == null) {
			if (other.objectValues != null) {
				return false;
			}
		} else if (!this.objectValues.equals(other.objectValues)) {
			return false;
		}
		return true;
	}

	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof EndOfOptionsDelimiter) {
			return (EndOfOptionsDelimiter) objectValue;
		}
		return null;
	}

	public String getNonparsedArg() {
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof String) {
			return (String) objectValue;
		}
		return null;
	}
	
	public List<Object> getObjectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}
	
	public Option getOption() {
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof Option) {
			return (Option) objectValue;
		}
		return null;
	}
	
	public OptionArg getOptionArg() {
		if (this.hasOption()) {
			if (this.objectValues.size() > 1) {
				Object objectValue = this.objectValues.get(1);
				if (objectValue instanceof OptionArg) {
					return (OptionArg) objectValue;
				}
			}
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
		result = prime * result + ((this.objectValues == null) ? 0 : this.objectValues.hashCode());
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
		Option option = this.getOption();
		if (option != null) {
			return opt.getAllOptions().contains(option);
		}
		return false;
	}
	
	public boolean hasOptionOf(final String opt) {
		Option option = this.getOption();
		if (option != null) {
			return opt.equals(option.toString());
		}
		return false;
	}

	public boolean hasOptionOfAnyOf(final List<String> opts) {
		Option option = this.getOption();
		if (option != null) {
			for (String opt : opts) {
				if (opt.equals(option.toString())) {
					return true;
				}
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
