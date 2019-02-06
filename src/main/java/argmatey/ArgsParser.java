package argmatey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class ArgsParser {

	private static abstract class AbstractArgParser implements ArgParser {

		private final ArgParser argParser;
		
		protected AbstractArgParser(final ArgParser parser) {
			ArgParser p = parser;
			if (p == null) {
				p = DefaultArgParser.INSTANCE;
			}
			this.argParser = p;
		}
		
		public final ArgParser getArgParser() {
			return this.argParser;
		}
		
		@Override
		public abstract ParseResult parse(
				final String arg, final ArgParserContext context);

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [argParser=")
				.append(this.argParser)
				.append("]");
			return builder.toString();
		}	
		
	}
	
	
	private static interface ArgParser {
		
		ParseResult parse(String arg, ArgParserContext context);
		
	}
	
	private static final class ArgParserContext {
		
		private int argCharIndex;
		private int argIndex;
		private final String[] args;
		private final Map<String, Object> properties;
		
		public ArgParserContext(final ArgParserContext other) {
			this.argCharIndex = other.argCharIndex;
			this.argIndex = other.argIndex;
			this.args = Arrays.copyOf(other.args, other.args.length);
			this.properties = new HashMap<String, Object>(other.properties);
		}
		
		public ArgParserContext(final String[] arguments) {
			for (String argument : arguments) {
				if (argument == null) {
					throw new NullPointerException(
							"argument(s) must not be null");
				}
			}
			this.argCharIndex = -1;
			this.argIndex = -1;
			this.args = Arrays.copyOf(arguments, arguments.length);
			this.properties = new HashMap<String, Object>();
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
		
		@SuppressWarnings("unused")
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
		
		public Object putProperty(final String name, final Object value) {
			return this.properties.put(name, value);
		}
		
		@SuppressWarnings("unused")
		public Object removeProperty(final String name) {
			return this.properties.remove(name);
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
	
	private static final class ArgParserContextProperties {
		
		private static final class PropertyNames {
			
			public static final String OPTION_PARSING_ENABLED = 
					"OPTION_PARSING_ENABLED";
			
			public static final String OPTIONS = "OPTIONS";
			
			private PropertyNames() { }

		}
		
		private final ArgParserContext argParserContext;
		
		public ArgParserContextProperties(
				final ArgParserContext parserContext) {
			this.argParserContext = parserContext;
		}
		
		public Map<String, Option> getOptions() {
			Map<String, Option> options = null;
			@SuppressWarnings("unchecked")
			Map<String, Option> value = 
					(Map<String, Option>) this.argParserContext.getProperty(
							PropertyNames.OPTIONS);
			if (value != null) {
				options = Collections.unmodifiableMap(
						new HashMap<String, Option>(value));
			}
			return options;
		}
		
		public boolean isOptionParsingEnabled() {
			boolean optionParsingEnabled = false;
			Boolean value = (Boolean) this.argParserContext.getProperty(
					PropertyNames.OPTION_PARSING_ENABLED);
			if (value != null) {
				optionParsingEnabled = value.booleanValue();
			}
			return optionParsingEnabled;
		}
		
		public void setOptionParsingEnabled(final boolean optParsingEnabled) {
			this.argParserContext.putProperty(
					PropertyNames.OPTION_PARSING_ENABLED, 
					Boolean.valueOf(optParsingEnabled));
		}
		
		public void setOptions(final Map<String, Option> opts) {
			this.argParserContext.putProperty(PropertyNames.OPTIONS, opts);
		}
	}
	
	private static enum DefaultArgParser implements ArgParser {

		INSTANCE;
		
		@Override
		public ParseResult parse(final String arg, final ArgParserContext context) {
			return new ParseResult(arg);
		}
		
		@Override
		public String toString() {
			return DefaultArgParser.class.getSimpleName();
		}
		
	}

	private static final class EndOfOptionsArgParser extends AbstractArgParser {
		
		public EndOfOptionsArgParser(final ArgParser parser) {
			super(parser);
		}
		
		@Override
		public ParseResult parse(final String arg, final ArgParserContext context) {
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context); 
			if (properties.isOptionParsingEnabled()) {
				properties.setOptionParsingEnabled(false);
			}
			return this.getArgParser().parse(arg, context);
		}
		
	}
	
	private static final class EndOfOptionsDelimiterParser extends AbstractArgParser {
		
		public EndOfOptionsDelimiterParser(final ArgParser argParser) {
			super(argParser);
		}
		
		@Override
		public ParseResult parse(final String arg, final ArgParserContext context) {
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context);
			if (!(properties.isOptionParsingEnabled()
					&& arg.equals(EndOfOptionsDelimiter.INSTANCE.toString()))) {
				return this.getArgParser().parse(arg, context);
			}
			properties.setOptionParsingEnabled(false);
			return new ParseResult(EndOfOptionsDelimiter.INSTANCE);
		}

	}
	
	private static final class GnuLongOptionParser extends OptionParser {
		
		public GnuLongOptionParser(final ArgParser parser) {
			super(parser, GnuLongOption.class);
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgParserContext context) {
			return arg.startsWith("--") && arg.length() > 2;
		}

		@Override
		protected ParseResult parseOption(
				final String arg, final ArgParserContext context) {
			String option = arg;
			String optionArg = null;
			String[] argElements = arg.split("=", 2);
			if (argElements.length == 2) {
				option = argElements[0];
				optionArg = argElements[1];
			}
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context);
			Map<String, Option> options = properties.getOptions();
			Option opt = options.get(option);
			if (opt == null) {
				throw new UnknownOptionException(option);
			}
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArg == null && optionArgSpec != null 
					&& !optionArgSpec.isOptional()) {
				String[] args = context.getArgs();
				int argIndex = context.getArgIndex();
				if (argIndex < args.length - 1) {
					argIndex = context.incrementArgIndex();
					optionArg = args[argIndex];
				}
			}
			return new ParseResult(new OptionOccurrence(
					opt, opt.newOptionArg(optionArg)));
		}
		
	}
	
	private static final class LongOptionParser extends OptionParser {
		
		public LongOptionParser(final ArgParser parser) {
			super(new PosixOptionParser(parser), LongOption.class);
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgParserContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}

		@Override
		protected ParseResult parseOption(
				final String arg, final ArgParserContext context) {
			String option = arg;
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context);
			Map<String, Option> options = properties.getOptions();
			Option opt = options.get(option);
			if (opt == null) {
				OptionParser posixOptionParser = 
						(PosixOptionParser) this.getArgParser();
				boolean hasPosixOption = false;
				for (Option o : options.values()) {
					if (posixOptionParser.getOptionClass().isInstance(o)) {
						hasPosixOption = true;
						break;
					}
				}
				if (hasPosixOption) {
					return posixOptionParser.parseOption(arg, context);
				}
				throw new UnknownOptionException(option);
			}
			String optionArg = null;
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArgSpec != null && !optionArgSpec.isOptional()) {
				String[] args = context.getArgs();
				int argIndex = context.getArgIndex();
				if (argIndex < args.length - 1) {
					argIndex = context.incrementArgIndex();
					optionArg = args[argIndex];
				}
			}
			return new ParseResult(new OptionOccurrence(
					opt, opt.newOptionArg(optionArg)));
		}
		
	}
	
	private static abstract class OptionParser extends AbstractArgParser {
		
		private final Class<?> optionClass;
		
		protected OptionParser(final ArgParser parser, 
				final Class<? extends Option> optClass) {
			super(parser);
			if (optClass == null) {
				throw new NullPointerException("Option class must not be null");
			}
			this.optionClass = optClass;
		}
		
		public final Class<?> getOptionClass() {
			return this.optionClass;
		}
		
		protected abstract boolean isOption(
				final String arg, final ArgParserContext context);
		
		@Override
		public final ParseResult parse(
				final String arg, final ArgParserContext context) {
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context);
			if (!properties.isOptionParsingEnabled()) {
				return this.getArgParser().parse(arg, context);
			}
			boolean hasOption = false;
			Map<String, Option> options = properties.getOptions();
			for (Option option : options.values()) {
				if (this.optionClass.isInstance(option)) {
					hasOption = true;
					break;
				}
			}
			if (!hasOption) {
				return this.getArgParser().parse(arg, context);
			}
			if (!this.isOption(arg, context)) {
				return this.getArgParser().parse(arg, context);
			}
			return this.parseOption(arg, context);
		}

		protected abstract ParseResult parseOption(
				final String arg, final ArgParserContext context);
		
	}
	
	private static final class PosixOptionParser extends OptionParser {

		public PosixOptionParser(final ArgParser parser) {
			super(parser, PosixOption.class);
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgParserContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}

		@Override
		protected ParseResult parseOption(
				final String arg, final ArgParserContext context) {
			int argCharIndex = context.getArgCharIndex();
			if (argCharIndex == -1) { /* not incremented yet */
				/* initiate incrementing. ArgParser will do the incrementing  */
				/* index 0 - '-' */
				argCharIndex = context.incrementArgCharIndex();
				/* index 1 - first alphanumeric character */
				argCharIndex = context.incrementArgCharIndex();
			}
			char ch = arg.charAt(argCharIndex);
			String name = Character.toString(ch);
			String option = "-".concat(name);
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(context);
			Map<String, Option> options = properties.getOptions();
			Option opt = options.get(option);
			if (opt == null) {
				throw new UnknownOptionException(option);
			}
			String optionArg = null;
			OptionArgSpec optionArgSpec = opt.getOptionArgSpec();
			if (optionArgSpec != null) {
				if (argCharIndex < arg.length() - 1) {
					argCharIndex = context.incrementArgCharIndex();
					optionArg = arg.substring(argCharIndex);
					while (argCharIndex < arg.length() - 1) {
						argCharIndex = context.incrementArgCharIndex();
					}
				} else {
					if (!optionArgSpec.isOptional()) {
						String[] args = context.getArgs();
						int argIndex = context.getArgIndex();
						if (argIndex < args.length - 1) {
							argCharIndex = context.resetArgCharIndex();
							argIndex = context.incrementArgIndex();
							optionArg = args[argIndex];
						}
					}
				}
			}
			return new ParseResult(new OptionOccurrence(
					opt, opt.newOptionArg(optionArg)));
		}		
	}
	
	public static ArgsParser newInstance(
			final String[] args, 
			final Options options, 
			final boolean posixlyCorrect) {
		ArgParser endOfOptionsArgParser = null;
		if (posixlyCorrect) {
			endOfOptionsArgParser = new EndOfOptionsArgParser(null);
		}
		ArgParser argParser = new GnuLongOptionParser(new LongOptionParser(
				new EndOfOptionsDelimiterParser(endOfOptionsArgParser)));
		return new ArgsParser(args, options, argParser);
	}
	
	private final ArgParser argParser;
	private ArgParserContext argParserContext;
	private final Options options;
		
	private ArgsParser(
			final String[] args, 
			final Options opts, 
			final ArgParser parser) {
		for (String arg : args) {
			if (arg == null) {
				throw new NullPointerException("argument(s) must not be null");
			}
		}
		if (opts == null) {
			throw new NullPointerException("Options must not be null");
		}
		ArgParserContext parserContext = new ArgParserContext(args);
		List<Option> optsList = opts.toList();
		if (optsList.size() > 0) {
			Map<String, Option> optsMap = new HashMap<String, Option>();
			for (Option opt : optsList) {
				for (Option o : opt.getAllOptions()) {
					optsMap.put(o.toString(), o);
				}
			}
			ArgParserContextProperties properties = 
					new ArgParserContextProperties(parserContext);
			properties.setOptionParsingEnabled(true);
			properties.setOptions(optsMap);
		}
		this.argParser = parser;
		this.argParserContext = parserContext;
		this.options = opts;
	}

	public int getArgCharIndex() {
		return this.argParserContext.getArgCharIndex();
	}
	
	public int getArgIndex() {
		return this.argParserContext.getArgIndex();
	}
	
	public String[] getArgs() {
		return this.argParserContext.getArgs();
	}
	
	public Options getOptions() {
		return this.options;
	}
	
	public boolean hasNext() {
		String[] args = this.getArgs();
		int argIndex = this.getArgIndex();
		int argCharIndex = this.getArgCharIndex();
		return (argIndex > -1 && argCharIndex > -1 
				&& argCharIndex < args[argIndex].length() - 1) 
				|| argIndex < args.length - 1;
	}
	
	public String next() {
		String next = null;
		int argIndex = this.getArgIndex();
		int argCharIndex = this.getArgCharIndex();
		String[] args = this.getArgs();
		ArgParserContext recentArgParserContext = 
				new ArgParserContext(this.argParserContext);
		if (argIndex > -1 && argCharIndex > -1) {
			/* 
			 * The argument character index was incremented by this method or 
			 * another ArgParser
			 */
			if (argCharIndex < args[argIndex].length() - 1) {
				/* 
				 * if the argument character index is not the last index, 
				 * increment it 
				 */
				argCharIndex = this.argParserContext.incrementArgCharIndex();
				next = Character.toString(args[argIndex].charAt(argCharIndex)); 
			} else {
				/* 
				 * if the argument character index is the last index, reset it 
				 */
				argCharIndex = this.argParserContext.resetArgCharIndex();
			}
		}
		if (argCharIndex == -1) {
			/* 
			 * The argument character index was not incremented or was reset by 
			 * this method or another ArgParser
			 */
			if (argIndex < args.length - 1) {
				/* 
				 * if the argument index is not the last index, increment it
				 */
				argIndex = this.argParserContext.incrementArgIndex();
				next = args[argIndex];
			} else {
				/* 
				 * failure atomicity (return back to most recent working state) 
				 */
				this.argParserContext = recentArgParserContext;
				throw new NoSuchElementException();
			}
		}
		return next;
	}
	
	public ParseResult parseNext() {
		ArgParserContext recentArgParserContext = 
				new ArgParserContext(this.argParserContext);
		this.next();
		ParseResult parseResult = null;
		try {
			parseResult = this.argParser.parse(
					this.getArgs()[this.getArgIndex()], this.argParserContext);
		} catch (Throwable t) {
			/* 
			 * failure atomicity (return back to most recent working state) 
			 */
			this.argParserContext = recentArgParserContext;
			if (t instanceof Error) {
				Error e = (Error) t;
				throw e;
			}
			if (t instanceof RuntimeException) {
				RuntimeException rte = (RuntimeException) t;
				throw rte;
			}
		}
		return parseResult;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [options=")
			.append(this.options)
			.append(", getArgCharIndex()=")
			.append(this.getArgCharIndex())
			.append(", getArgIndex()=")
			.append(this.getArgIndex())
			.append(", getArgs()=")
			.append(Arrays.toString(this.getArgs()))
			.append("]");
		return builder.toString();
	}

}
