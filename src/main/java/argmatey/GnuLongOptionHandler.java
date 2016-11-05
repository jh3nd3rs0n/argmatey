package argmatey;

import java.util.Map;

final class GnuLongOptionHandler extends OptionHandler {
	
	public GnuLongOptionHandler(final ArgHandler handler) {
		super(handler, GnuLongOption.class);
	}

	@Override
	protected void handleOption(final String arg, final ArgHandlerContext context) {
		String option = arg;
		String optionArg = null;
		String[] argElements = arg.split("=", 2);
		if (argElements.length == 2) {
			option = argElements[0];
			optionArg = argElements[1];
		}
		Map<String, Option> options = 
				ArgHandlerContextProperties.getOptions(context);
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
		ArgHandlerContextProperties.setParseResult(context, new ParseResult(
				new OptionOccurrence(opt, opt.newOptionArg(optionArg))));
	}

	@Override
	protected boolean isOption(final String arg, final ArgHandlerContext context) {
		return arg.startsWith("--") && arg.length() > 2;
	}
	
}
