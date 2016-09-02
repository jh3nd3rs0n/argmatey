package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ParseResult {
	
	private final Object objectValue;
	private OptionArg optionArg;

	ParseResult(final Object objValue) {
		this(objValue, null);
	}
	
	ParseResult(final Object objValue, final OptionArg optArg) {
		this.objectValue = objValue;
		this.optionArg = optArg;
	}
	
	public EndOfOptionsDelimiter asEndOfOptionsDelimiter() {
		return EndOfOptionsDelimiter.class.cast(this.objectValue);
	}
	
	public Object asObjectValue() {
		return this.objectValue;
	}
	
	public Option asOption() {
		return Option.class.cast(this.objectValue);
	}
	
	public String asUnparsedArg() {
		return String.class.cast(this.objectValue);
	}
	
	public OptionArg getOptionArg() {
		return this.optionArg;
	}
	
	public boolean hasOptionArg() {
		return this.optionArg != null;
	}
	
	public boolean isEndOfOptionsDelimiter() {
		return this.objectValue instanceof EndOfOptionsDelimiter;
	}
	
	public boolean isOption() {
		return this.objectValue instanceof Option;
	}
	
	public boolean isOptionFrom(final Option option) {
		return this.isOption() && this.asOption().getAllOptions().contains(option);
	}
	
	public boolean isOptionOf(final String option) {
		return this.isOption() && this.asOption().toString().equals(option);
	}
	
	public boolean isOptionOfAnyOf(final List<String> options) {
		if (this.isOption()) {
			for (String option : options) {
				if (this.asOption().toString().equals(option)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isOptionOfAnyOf(final String option1, final String option2) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		return this.isOptionOfAnyOf(options);
	}
	
	public boolean isOptionOfAnyOf(final String option1, final String option2,
			final String option3) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		options.add(option3);
		return this.isOptionOfAnyOf(options);
	}
	
	public boolean isOptionOfAnyOf(final String option1, final String option2,
			final String option3, final String... additionalOptions) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		options.add(option3);
		options.addAll(Arrays.asList(additionalOptions));
		return this.isOptionOfAnyOf(options);
	}
	
	public boolean isUnparsedArg() {
		return this.objectValue instanceof String;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [objectValue=")
			.append(this.objectValue)
			.append(", optionArg=")
			.append(this.optionArg)
			.append("]");
		return builder.toString();
	}

}
