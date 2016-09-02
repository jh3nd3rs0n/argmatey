package argmatey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class ArgHandlerContext {
	
	private int argCharIndex;
	private int argIndex;
	private final String[] args;
	private final Map<String, Object> properties;
	
	public ArgHandlerContext(
			final String[] arguments,
			final int argumentIndex,
			final int argumentCharIndex,
			final Map<String, Object> props) {
		for (String argument : arguments) {
			if (argument == null) {
				throw new NullPointerException("argument(s) must not be null");
			}
		}
		if (argumentIndex < -1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (argumentIndex > arguments.length - 1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (argumentIndex == -1 && argumentCharIndex > -1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (argumentCharIndex < -1) {
			throw new StringIndexOutOfBoundsException();
		}
		if (argumentIndex > -1 
				&& argumentCharIndex > arguments[argumentIndex].length() - 1) {
			throw new StringIndexOutOfBoundsException();
		}
		this.argCharIndex = argumentCharIndex;
		this.argIndex = argumentIndex;
		this.args = Arrays.copyOf(arguments, arguments.length);
		this.properties = new HashMap<String, Object>(props);
	}
	
	public int getArgCharIndex() {
		return this.argCharIndex;
	}
	
	public int getArgIndex() {
		return this.argIndex;
	}
	
	public String[] getArgs() {
		return Arrays.copyOf(this.args, this.args.length);
	}
	
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(this.properties);
	}
	
	public Object getProperty(final String name) {
		return this.properties.get(name);
	}
	
	public int incrementArgCharIndex() {
		if (this.argIndex == -1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (this.argCharIndex == this.args[this.argIndex].length() - 1) {
			throw new StringIndexOutOfBoundsException();
		}
		return ++this.argCharIndex;
	}
	
	public int incrementArgIndex() {
		if (this.argIndex == this.args.length - 1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return ++this.argIndex;
	}
	
	public void putProperty(final String name, final Object value) {
		this.properties.put(name, value);
	}
	
	public int resetArgCharIndex() {
		this.argCharIndex = -1;
		return this.argCharIndex;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [argCharIndex=")
			.append(this.argCharIndex)
			.append(", argIndex=")
			.append(this.argIndex)
			.append(", args=")
			.append(Arrays.toString(this.args))
			.append(", properties=")
			.append(this.properties)
			.append("]");
		return builder.toString();
	}
	
}
