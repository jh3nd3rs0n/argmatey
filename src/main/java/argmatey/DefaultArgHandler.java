package argmatey;

final class DefaultArgHandler implements ArgHandler {

	public static final DefaultArgHandler INSTANCE = new DefaultArgHandler();
	
	private DefaultArgHandler() { 
		if (INSTANCE != null) {
			throw new AssertionError("There can only be one");
		}
	}
	
	@Override
	public void handle(final String arg, final ArgHandlerContext context) {
		ArgHandlerContextProperties.setParseResult(context, new NonparsedArg(arg));
	}
	
}