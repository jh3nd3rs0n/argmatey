package argmatey;

import java.util.Map;

final class PosixOptionHandler extends OptionHandler {

	public PosixOptionHandler(final ArgHandler handler) {
		super(handler, PosixOption.class);
	}

	@Override
	protected void handleOption(final String arg, final ArgHandlerContext context) {
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
		Map<String, Option> options = 
				ArgHandlerContextProperties.getOptions(context);
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
		ArgHandlerContextProperties.setParseResult(context, 
				ParseResult.newInstance(opt, opt.newOptionArg(optionArg)));
	}

	@Override
	protected boolean isOption(final String arg, final ArgHandlerContext context) {
		return arg.length() > 1 && arg.startsWith("-") && !arg.startsWith("--");
	}		
}
