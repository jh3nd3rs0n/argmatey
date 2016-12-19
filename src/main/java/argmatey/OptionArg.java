package argmatey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OptionArg {
	
	private final List<Object> objectValues;
	private final List<OptionArg> optionArgs;
	private final OptionArgSpec optionArgSpec;
	private final String string;
		
	OptionArg(
			final List<Object> objValues, 
			final List<OptionArg> optArgs, 
			final OptionArgSpec optArgSpec, 
			final String optArg) {
		this.objectValues = new ArrayList<Object>(objValues);
		this.optionArgs = new ArrayList<OptionArg>(optArgs);
		this.optionArgSpec = optArgSpec;
		this.string = optArg;
	}
	
	public Object getObjectValue() {
		return this.objectValues.get(0);
	}
	
	public List<Object> getObjectValues() {
		return Collections.unmodifiableList(this.objectValues);
	}
	
	public List<OptionArg> getOptionArgs() {
		List<OptionArg> optArgs = new ArrayList<OptionArg>();
		if (this.optionArgs.size() == 0) {
			optArgs.add(this);
		} else {
			optArgs.addAll(this.optionArgs);
		}
		return Collections.unmodifiableList(optArgs);
	}
	
	public OptionArgSpec getOptionArgSpec() {
		return this.optionArgSpec;
	}
	
	public <T> T getTypeValue(final Class<T> type) {
		return type.cast(this.getObjectValue());
	}
	
	public <T> List<T> getTypeValues(final Class<T> type) {
		List<T> typeValues = new ArrayList<T>();
		for (Object objectValue : this.getObjectValues()) {
			typeValues.add(type.cast(objectValue));
		}
		return Collections.unmodifiableList(typeValues);
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
