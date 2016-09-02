package argmatey;

final class PosixlyCorrectArgHandler extends AbstractArgHandler {
	
	public PosixlyCorrectArgHandler(final ArgHandler handler) {
		super(handler);
	}
	
	@Override
	public void handle(final String arg, final ArgHandlerContext context) {
		if (ArgHandlerContextProperties.isOptionHandlingEnabled(context)) {
			ArgHandlerContextProperties.setOptionHandlingEnabled(context, false);
		}
		this.getArgHandler().handle(arg, context);
	}
	
}