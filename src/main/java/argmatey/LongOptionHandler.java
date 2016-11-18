package argmatey;

import java.util.Map;

final class LongOptionHandler extends OptionHandler {
	
	public LongOptionHandler(final ArgHandler handler) {
		super(new PosixOptionHandler(handler), LongOption.class);
	}

	@Override
	protected void handleOption(final String arg, final ArgHandlerContext context) {
		String option = arg;
		Map<String, Option> options = 
				ArgHandlerContextProperties.getOptions(context);
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
		OptionArg optArg = opt.newOptionArg(optionArg);
		ParseResult parseResult = null;
		if (optArg == null) {
			parseResult = new ParseResult(opt);
		} else {
			parseResult = new ParseResult(opt, optArg);
		}
		ArgHandlerContextProperties.setParseResult(context, parseResult);
	}

	@Override
	protected boolean isOption(final String arg, final ArgHandlerContext context) {
		return arg.length() > 1 && arg.startsWith("-") && !arg.startsWith("--");
	}
	
}
