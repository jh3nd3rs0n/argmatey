package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ParseResult {
	
	private final List<Object> objectValues;
	
	private ParseResult(final List<Object> objValues) {
		this.objectValues = new ArrayList<Object>(objValues);
	}
	
	ParseResult(final Object objValue) {
		this(Arrays.asList(objValue));
	}
	
	ParseResult(final Object objValue1, final Object objValue2) {
		this(Arrays.asList(objValue1, objValue2));
	}
	
	public EndOfOptionsDelimiter asEndOfOptionsDelimiter() {
		return EndOfOptionsDelimiter.class.cast(this.objectValues.get(0));
	}
	
	public Option asOption() {
		return Option.class.cast(this.objectValues.get(0));
	}
	
	public String asUnparsedArg() {
		return String.class.cast(this.objectValues.get(0));
	}
	
	public OptionArg getOptionArg() {
		OptionArg optionArg = null;
		if (this.objectValues.size() > 1) {
			optionArg = OptionArg.class.cast(this.objectValues.get(1));
		}
		return optionArg;
	}
	
	public boolean hasOptionArg() {
		if (this.objectValues.size() > 1) {
			return this.objectValues.get(1) instanceof OptionArg;
		}
		return false;
	}
	
	public boolean isEndOfOptionsDelimiter() {
		return this.objectValues.get(0) instanceof EndOfOptionsDelimiter;
	}
	
	public boolean isOption() {
		return this.objectValues.get(0) instanceof Option;
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
		return this.objectValues.get(0) instanceof String;
	}
	
	public Object objectValue() {
		return this.objectValues.get(0);
	}
	
	public List<Object> objectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParseResult [objectValues=")
		.append(this.objectValues).append("]");
		return builder.toString();
	}

}
