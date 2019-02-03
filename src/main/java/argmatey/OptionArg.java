package argmatey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OptionArg {
	
	private final List<Object> objectValues;
	private final List<OptionArg> optionArgs;
	private final String string;
		
	OptionArg(
			final List<Object> objValues, 
			final List<OptionArg> optArgs, 
			final String optArg) {
		this.objectValues = new ArrayList<Object>(objValues);
		this.optionArgs = new ArrayList<OptionArg>(optArgs);
		this.string = optArg;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OptionArg)) {
			return false;
		}
		OptionArg other = (OptionArg) obj;
		if (this.objectValues == null) {
			if (other.objectValues != null) {
				return false;
			}
		} else if (!this.objectValues.equals(other.objectValues)) {
			return false;
		}
		if (this.optionArgs == null) {
			if (other.optionArgs != null) {
				return false;
			}
		} else if (!this.optionArgs.equals(other.optionArgs)) {
			return false;
		}
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.objectValues == null) ? 0 : this.objectValues.hashCode());
		result = prime * result + ((this.optionArgs == null) ? 0 : this.optionArgs.hashCode());
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
