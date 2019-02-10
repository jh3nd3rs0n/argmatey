package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OptionArgSpec {

	public static final class Builder {
				
		private String name;
		private boolean optional;
		private String separator;
		private StringConverter stringConverter;
		private Class<?> type;
		
		public Builder() {
			this.name = null;
			this.optional = false;
			this.separator = null;
			this.stringConverter = null;
			this.type = null;
		}
		
		public OptionArgSpec build() {
			return new OptionArgSpec(this); 
		}
		
		public Builder name(final String n) {
			this.name = n;
			return this;
		}
		
		public Builder optional(final boolean b) {
			this.optional = b;
			return this;
		}
		
		public Builder separator(final String s) {
			this.separator = s;
			return this;
		}
		
		public Builder stringConverter(final StringConverter sc) {
			this.stringConverter = sc;
			return this;
		}
		
		public Builder type(final Class<?> t) {
			this.type = t;
			return this;
		}
		
	}
	
	public static final String DEFAULT_NAME = "option_argument";
	public static final String DEFAULT_SEPARATOR = "[^\\w\\W]";
	public static final Class<?> DEFAULT_TYPE = String.class;
		
	private final String name;
	private final boolean optional;
	private final String separator;
	private final StringConverter stringConverter;
	private final Class<?> type;
	
	private OptionArgSpec(final Builder builder) {
		String n = builder.name;
		boolean o = builder.optional;
		String s = builder.separator;
		StringConverter sc = builder.stringConverter;
		Class<?> t = builder.type;
		if (n == null) { n = DEFAULT_NAME; }
		if (s == null) { s = DEFAULT_SEPARATOR;	}
		if (t == null) { t = DEFAULT_TYPE; }
		if (sc == null) { sc = new DefaultStringConverter(t); }
		this.name = n;
		this.optional = o;
		this.separator = s;
		this.stringConverter = sc;
		this.type = t;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSeparator() {
		return this.separator;
	}
	
	public StringConverter getStringConverter() {
		return this.stringConverter;
	}
	
	public Class<?> getType() {
		return this.type;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public OptionArg newOptionArg(final String optionArg) {
		if (optionArg == null) {
			throw new NullPointerException("option argument must not be null");
		}
		List<String> optArgs = Arrays.asList(optionArg.split(this.separator));
		List<Object> objectValues = new ArrayList<Object>();
		List<OptionArg> opArgs = new ArrayList<OptionArg>();
		if (optArgs.size() == 1) {
			objectValues.add(this.stringConverter.convert(optionArg));
		} else {
			for (String optArg : optArgs) {
				OptionArg opArg = this.newOptionArg(optArg);
				objectValues.addAll(opArg.getObjectValues());
				opArgs.add(opArg);
			}
		}
		return new OptionArg(objectValues, opArgs, optionArg);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [name=")
			.append(this.name)
			.append(", optional=")
			.append(this.optional)
			.append(", separator=")
			.append(this.separator)
			.append(", stringConverter=")
			.append(this.stringConverter)			
			.append(", type=")
			.append(this.type)
			.append("]");
		return sb.toString();
	}

}
