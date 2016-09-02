package argmatey;


abstract class AbstractArgHandler implements ArgHandler {

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
	public abstract void handle(final String arg, final ArgHandlerContext context);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getArgHandler()=")
			.append(this.getArgHandler())
			.append("]");
		return builder.toString();
	}	
	
}