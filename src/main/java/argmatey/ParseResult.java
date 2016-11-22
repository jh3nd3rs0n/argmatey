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
	
	public EndOfOptionsDelimiter getEndOfOptionsDelimiter() {
		EndOfOptionsDelimiter endOfOptionsDelimiter = null;
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof EndOfOptionsDelimiter) {
			endOfOptionsDelimiter =	(EndOfOptionsDelimiter) objectValue;
		}
		return endOfOptionsDelimiter;
	}
	
	public Object getObjectValue() {
		return this.objectValues.get(0);
	}
	
	public List<Object> getObjectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}
	
	public Option getOption() {
		Option option = null;
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof Option) {
			option = (Option) objectValue;
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
	
	public String getUnparsedArg() {
		String unparsedArg = null;
		Object objectValue = this.objectValues.get(0);
		if (objectValue instanceof String) {
			unparsedArg = (String) objectValue;
		}
		return unparsedArg;
	}
	
	public boolean hasEndOfOptionsDelimiter() {
		return this.objectValues.get(0) instanceof EndOfOptionsDelimiter;
	}
	
	public boolean hasOption() {
		return this.objectValues.get(0) instanceof Option;
	}
	
	public boolean hasOptionArg() {
		if (this.objectValues.size() > 1) {
			return this.objectValues.get(1) instanceof OptionArg;
		}
		return false;
	}
	
	public boolean hasOptionFrom(final Option option) {
		return this.hasOption() && this.getOption().getAllOptions().contains(option);
	}
	
	public boolean hasOptionOf(final String option) {
		return this.hasOption() && this.getOption().toString().equals(option);
	}
	
	public boolean hasOptionOfAnyOf(final List<String> options) {
		if (this.hasOption()) {
			for (String option : options) {
				if (this.getOption().toString().equals(option)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasOptionOfAnyOf(final String option1, final String option2) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		return this.hasOptionOfAnyOf(options);
	}
	
	public boolean hasOptionOfAnyOf(final String option1, final String option2, 
			final String option3) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		options.add(option3);
		return this.hasOptionOfAnyOf(options);
	}
	
	public boolean hasOptionOfAnyOf(final String option1, final String option2, 
			final String option3, final String... additionalOptions) {
		List<String> options = new ArrayList<String>();
		options.add(option1);
		options.add(option2);
		options.add(option3);
		options.addAll(Arrays.asList(additionalOptions));
		return this.hasOptionOfAnyOf(options);
	}
	
	public boolean hasUnparsedArg() {
		return this.objectValues.get(0) instanceof String;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ParseResult [objectValues=")
		.append(this.objectValues).append("]");
		return builder.toString();
	}

}
