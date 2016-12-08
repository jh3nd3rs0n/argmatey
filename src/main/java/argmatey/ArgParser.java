package argmatey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class ArgParser {
	
	private final ArgHandler argHandler;
	private ArgHandlerContext argHandlerContext;
	private final Options options;
	private final boolean posixlyCorrect;
	
	public ArgParser(
			final String[] args, 
			final Options opts, 
			final boolean posixCorrect) {
		for (String arg : args) {
			if (arg == null) {
				throw new NullPointerException("argument(s) must not be null");
			}
		}
		if (opts == null) {
			throw new NullPointerException("Options must not be null");
		}
		ArgHandler posixlyCorrectArgHandler = null;
		if (posixCorrect) {
			posixlyCorrectArgHandler = new PosixlyCorrectArgHandler(null);
		}
		ArgHandler handler = new GnuLongOptionHandler(new LongOptionHandler(
				new EndOfOptionsDelimiterHandler(posixlyCorrectArgHandler)));
		Map<String, Object> properties = new HashMap<String, Object>();
		List<Option> optsList = opts.asList();
		if (optsList.size() > 0) {
			Map<String, Option> optsMap = new HashMap<String, Option>();
			for (Option opt : optsList) {
				for (Option o : opt.getAllOptions()) {
					optsMap.put(o.toString(), o);
				}
			}
			properties.put(
					ArgHandlerContextPropertyNames.OPTION_HANDLING_ENABLED, 
					Boolean.TRUE);			
			properties.put(
					ArgHandlerContextPropertyNames.OPTIONS, 
					optsMap);
		}
		ArgHandlerContext handlerContext = new ArgHandlerContext(
				args, -1, -1, properties);
		this.argHandler = handler;
		this.argHandlerContext = handlerContext;
		this.options = opts;
		this.posixlyCorrect = posixCorrect;
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
	
	public boolean isPosixlyCorrect() {
		return this.posixlyCorrect;
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
	
	public ParseResult parseNext() {
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
		ParseResult parseResult = ArgHandlerContextProperties.getParseResult(
				this.argHandlerContext);
		ArgHandlerContextProperties.setParseResult(
				this.argHandlerContext, null);
		return parseResult;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [options=")
			.append(this.options)
			.append(", posixlyCorrect=")
			.append(this.posixlyCorrect)
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
