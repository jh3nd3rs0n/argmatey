package argmatey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class ArgsParser {

	private static abstract class AbstractArgHandler implements ArgHandler {

		private final ArgHandler argHandler;
		
		protected AbstractArgHandler(final ArgHandler handler) {
			ArgHandler h = handler;
			if (h == null) {
				h = DefaultArgHandler.INSTANCE;
			}
			this.argHandler = h;
		}
		
		public final ArgHandler getArgHandler() {
			return this.argHandler;
		}
		
		@Override
		public abstract void handle(
				final String arg, final ArgHandlerContext context);

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [argHandler=")
				.append(this.argHandler)
				.append("]");
			return sb.toString();
		}	
		
	}
		
	private static interface ArgHandler {
		
		void handle(String arg, ArgHandlerContext context);
		
	}
	
	private static final class ArgHandlerContext {
		
		private int argCharIndex;
		private int argIndex;
		private final String[] args;
		private final Map<String, Object> properties;
		
		public ArgHandlerContext(final ArgHandlerContext other) {
			this.argCharIndex = other.argCharIndex;
			this.argIndex = other.argIndex;
			this.args = Arrays.copyOf(other.args, other.args.length);
			this.properties = new HashMap<String, Object>(other.properties);
		}
		
		public ArgHandlerContext(final String[] arguments) {
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
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [argCharIndex=")
				.append(this.argCharIndex)
				.append(", argIndex=")
				.append(this.argIndex)
				.append(", args=")
				.append(Arrays.toString(this.args))
				.append(", properties=")
				.append(this.properties)
				.append("]");
			return sb.toString();
		}
		
	}
	
	private static final class ArgHandlerContextProperties {
		
		private static final class PropertyNames {
			
			public static final String OPTION_HANDLING_ENABLED = 
					"OPTION_HANDLING_ENABLED";
			
			public static final String OPTIONS = "OPTIONS";
			
			public static final String PARSE_RESULT_HOLDER = 
					"PARSE_RESULT_HOLDER";
			
			private PropertyNames() { }

		}
		
		private final ArgHandlerContext argHandlerContext;
		
		public ArgHandlerContextProperties(
				final ArgHandlerContext handlerContext) {
			this.argHandlerContext = handlerContext;
		}
		
		public Map<String, Option> getOptions() {
			Map<String, Option> options = null;
			@SuppressWarnings("unchecked")
			Map<String, Option> value = 
					(Map<String, Option>) this.argHandlerContext.getProperty(
							PropertyNames.OPTIONS);
			if (value != null) {
				options = Collections.unmodifiableMap(
						new HashMap<String, Option>(value));
			}
			return options;
		}
		
		public ParseResultHolder getParseResultHolder() {
			ParseResultHolder parseResult = null;
			ParseResultHolder value = 
					(ParseResultHolder) this.argHandlerContext.getProperty(
							PropertyNames.PARSE_RESULT_HOLDER);
			if (value != null) {
				parseResult = value;
			}
			return parseResult;
		}
		
		public boolean isOptionHandlingEnabled() {
			boolean optionHandlingEnabled = false;
			Boolean value = (Boolean) this.argHandlerContext.getProperty(
					PropertyNames.OPTION_HANDLING_ENABLED);
			if (value != null) {
				optionHandlingEnabled = value.booleanValue();
			}
			return optionHandlingEnabled;
		}
		
		public void setOptionHandlingEnabled(final boolean optHandlingEnabled) {
			this.argHandlerContext.putProperty(
					PropertyNames.OPTION_HANDLING_ENABLED, 
					Boolean.valueOf(optHandlingEnabled));
		}
		
		public void setOptions(final Map<String, Option> opts) {
			this.argHandlerContext.putProperty(PropertyNames.OPTIONS, opts);
		}
		
		public void setParseResultHolder(
				final ParseResultHolder parseResultHolder) {
			this.argHandlerContext.putProperty(
					PropertyNames.PARSE_RESULT_HOLDER, parseResultHolder);
		}
	}
	
	private static enum DefaultArgHandler implements ArgHandler {

		INSTANCE;
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties =
					new ArgHandlerContextProperties(context);
			properties.setParseResultHolder(new ParseResultHolder(arg));
		}
		
		@Override
		public String toString() {
			return DefaultArgHandler.class.getSimpleName();
		}
		
	}

	private static final class EndOfOptionsArgHandler 
		extends AbstractArgHandler {
		
		public EndOfOptionsArgHandler(final ArgHandler handler) {
			super(handler);
		}
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context); 
			if (properties.isOptionHandlingEnabled()) {
				properties.setOptionHandlingEnabled(false);
			}
			this.getArgHandler().handle(arg, context);
		}
		
	}
	
	private static final class EndOfOptionsDelimiterHandler 
		extends AbstractArgHandler {
		
		public EndOfOptionsDelimiterHandler(final ArgHandler argHandler) {
			super(argHandler);
		}
		
		@Override
		public void handle(final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			if (!(properties.isOptionHandlingEnabled()
					&& arg.equals(EndOfOptionsDelimiter.INSTANCE.toString()))) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			properties.setOptionHandlingEnabled(false);
			properties.setParseResultHolder(new ParseResultHolder(
					EndOfOptionsDelimiter.INSTANCE));
		}

	}
	
	private static final class GnuLongOptionHandler extends OptionHandler {
		
		public GnuLongOptionHandler(final ArgHandler handler) {
			super(handler, GnuLongOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			String option = arg;
			String optionArg = null;
			String[] argElements = arg.split("=", 2);
			if (argElements.length == 2) {
				option = argElements[0];
				optionArg = argElements[1];
			}
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
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
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.startsWith("--") && arg.length() > 2;
		}
		
	}
	
	private static final class LongOptionHandler extends OptionHandler {
		
		public LongOptionHandler(final ArgHandler handler) {
			super(new PosixOptionHandler(handler), LongOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			String option = arg;
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			Map<String, Option> options = properties.getOptions();
			Option opt = options.get(option);
			if (opt == null) {
				OptionHandler posixOptionHandler = 
						(PosixOptionHandler) this.getArgHandler();
				boolean hasPosixOption = false;
				for (Option o : options.values()) {
					if (posixOptionHandler.getOptionClass().isInstance(o)) {
						hasPosixOption = true;
						break;
					}
				}
				if (hasPosixOption) {
					posixOptionHandler.handleOption(arg, context);
					return;
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
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}
		
	}
	
	private static abstract class OptionHandler extends AbstractArgHandler {
		
		private final Class<?> optionClass;
		
		protected OptionHandler(final ArgHandler handler, 
				final Class<? extends Option> optClass) {
			super(handler);
			if (optClass == null) {
				throw new NullPointerException("Option class must not be null");
			}
			this.optionClass = optClass;
		}
		
		public final Class<?> getOptionClass() {
			return this.optionClass;
		}
		
		@Override
		public final void handle(
				final String arg, final ArgHandlerContext context) {
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
			if (!properties.isOptionHandlingEnabled()) {
				this.getArgHandler().handle(arg, context);
				return;
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
				this.getArgHandler().handle(arg, context);
				return;
			}
			if (!this.isOption(arg, context)) {
				this.getArgHandler().handle(arg, context);
				return;
			}
			this.handleOption(arg, context);
		}
		
		protected abstract void handleOption(
				final String arg, final ArgHandlerContext context);

		protected abstract boolean isOption(
				final String arg, final ArgHandlerContext context);
		
	}
	
	private static final class PosixOptionHandler extends OptionHandler {

		public PosixOptionHandler(final ArgHandler handler) {
			super(handler, PosixOption.class);
		}

		@Override
		protected void handleOption(
				final String arg, final ArgHandlerContext context) {
			int argCharIndex = context.getArgCharIndex();
			if (argCharIndex == -1) { /* not incremented yet */
				/* initiate incrementing. ArgsParser will do the incrementing */
				/* index 0 - '-' */
				argCharIndex = context.incrementArgCharIndex();
				/* index 1 - first alphanumeric character */
				argCharIndex = context.incrementArgCharIndex();
			}
			char ch = arg.charAt(argCharIndex);
			String name = Character.toString(ch);
			String option = "-".concat(name);
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(context);
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
			properties.setParseResultHolder(new ParseResultHolder(
					new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
		}

		@Override
		protected boolean isOption(
				final String arg, final ArgHandlerContext context) {
			return arg.length() > 1 
					&& arg.startsWith("-") 
					&& !arg.startsWith("--");
		}		
	}
	
	public static ArgsParser newInstance(
			final String[] args, 
			final Options options, 
			final boolean posixlyCorrect) {
		ArgHandler endOfOptionsArgHandler = null;
		if (posixlyCorrect) {
			endOfOptionsArgHandler = new EndOfOptionsArgHandler(null);
		}
		ArgHandler argHandler = new GnuLongOptionHandler(new LongOptionHandler(
				new EndOfOptionsDelimiterHandler(endOfOptionsArgHandler)));
		return new ArgsParser(args, options, argHandler);
	}
	
	private final ArgHandler argHandler;
	private ArgHandlerContext argHandlerContext;
	private final Options options;
		
	private ArgsParser(
			final String[] args, 
			final Options opts, 
			final ArgHandler handler) {
		for (String arg : args) {
			if (arg == null) {
				throw new NullPointerException("argument(s) must not be null");
			}
		}
		if (opts == null) {
			throw new NullPointerException("Options must not be null");
		}
		ArgHandlerContext handlerContext = new ArgHandlerContext(args);
		List<Option> optsList = opts.toList();
		if (optsList.size() > 0) {
			Map<String, Option> optsMap = new HashMap<String, Option>();
			for (Option opt : optsList) {
				for (Option o : opt.getAllOptions()) {
					optsMap.put(o.toString(), o);
				}
			}
			ArgHandlerContextProperties properties = 
					new ArgHandlerContextProperties(handlerContext);
			properties.setOptionHandlingEnabled(true);
			properties.setOptions(optsMap);
		}
		this.argHandler = handler;
		this.argHandlerContext = handlerContext;
		this.options = opts;
	}

	public int getArgCharIndex() {
		return this.argHandlerContext.getArgCharIndex();
	}
	
	public int getArgIndex() {
		return this.argHandlerContext.getArgIndex();
	}
	
	public String[] getArgs() {
		return this.argHandlerContext.getArgs();
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
		ArgHandlerContext recentArgHandlerContext = 
				new ArgHandlerContext(this.argHandlerContext);
		if (argIndex > -1 && argCharIndex > -1) {
			/* 
			 * The argument character index was incremented by this method or 
			 * another ArgHandler
			 */
			if (argCharIndex < args[argIndex].length() - 1) {
				/* 
				 * if the argument character index is not the last index, 
				 * increment it 
				 */
				argCharIndex = this.argHandlerContext.incrementArgCharIndex();
				next = Character.toString(args[argIndex].charAt(argCharIndex)); 
			} else {
				/* 
				 * if the argument character index is the last index, reset it 
				 */
				argCharIndex = this.argHandlerContext.resetArgCharIndex();
			}
		}
		if (argCharIndex == -1) {
			/* 
			 * The argument character index was not incremented or was reset by 
			 * this method or another ArgHandler
			 */
			if (argIndex < args.length - 1) {
				/* 
				 * if the argument index is not the last index, increment it
				 */
				argIndex = this.argHandlerContext.incrementArgIndex();
				next = args[argIndex];
			} else {
				/* 
				 * failure atomicity (return back to most recent working state) 
				 */
				this.argHandlerContext = recentArgHandlerContext;
				throw new NoSuchElementException();
			}
		}
		return next;
	}
	
	public void parseEachRemainingTo(
			final ParseResultHandler parseResultHandler) {
		while (this.hasNext()) {
			ParseResultHolder parseResultHolder = this.parseNext();
			parseResultHandler.handle(parseResultHolder);
			if (parseResultHolder.hasEndOfOptionsDelimiter()) {
				parseResultHandler.handle(
						parseResultHolder.getEndOfOptionsDelimiter());
			}
			if (parseResultHolder.hasNonparsedArg()) {
				parseResultHandler.handle(parseResultHolder.getNonparsedArg());
			}
			if (parseResultHolder.hasOptionOccurrence()) {
				OptionOccurrence optionOccurrence = 
						parseResultHolder.getOptionOccurrence();
				parseResultHandler.handle(optionOccurrence);
				parseResultHandler.handle(optionOccurrence.getOption(), 
						optionOccurrence.getOptionArg());
			}
			parseResultHandler.handle(parseResultHolder.getParseResult());
		}
	}
	
	public ParseResultHolder parseNext() {
		ArgHandlerContext recentArgHandlerContext = 
				new ArgHandlerContext(this.argHandlerContext);
		this.next();
		try {
			this.argHandler.handle(
					this.getArgs()[this.getArgIndex()], this.argHandlerContext);
		} catch (Throwable t) {
			/* 
			 * failure atomicity (return back to most recent working state) 
			 */
			this.argHandlerContext = recentArgHandlerContext;
			if (t instanceof Error) {
				Error e = (Error) t;
				throw e;
			}
			if (t instanceof RuntimeException) {
				RuntimeException rte = (RuntimeException) t;
				throw rte;
			}
		}
		ArgHandlerContextProperties properties = 
				new ArgHandlerContextProperties(this.argHandlerContext);
		ParseResultHolder parseResultHolder = properties.getParseResultHolder();
		properties.setParseResultHolder(null);
		return parseResultHolder;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [options=")
			.append(this.options)
			.append(", getArgCharIndex()=")
			.append(this.getArgCharIndex())
			.append(", getArgIndex()=")
			.append(this.getArgIndex())
			.append(", getArgs()=")
			.append(Arrays.toString(this.getArgs()))
			.append("]");
		return sb.toString();
	}

}
