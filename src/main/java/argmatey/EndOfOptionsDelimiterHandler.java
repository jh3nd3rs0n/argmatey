package argmatey;

final class EndOfOptionsDelimiterHandler extends AbstractArgHandler {
	
	public EndOfOptionsDelimiterHandler(final ArgHandler argHandler) {
		super(argHandler);
	}
	
	@Override
	public void handle(final String arg, final ArgHandlerContext context) {
		if (!(ArgHandlerContextProperties.isOptionHandlingEnabled(context)
				&& arg.equals(EndOfOptionsDelimiter.STRING))) {
			this.getArgHandler().handle(arg, context);
			return;
		}
		ArgHandlerContextProperties.setOptionHandlingEnabled(context, false);
		ArgHandlerContextProperties.setParseResult(context, 
				ParseResult.newInstance(EndOfOptionsDelimiter.INSTANCE));
	}

}