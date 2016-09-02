package argmatey;

import java.util.Map;

abstract class OptionHandler extends AbstractArgHandler {
	
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
	public final void handle(final String arg, final ArgHandlerContext context) {
		if (!ArgHandlerContextProperties.isOptionHandlingEnabled(context)) {
			this.getArgHandler().handle(arg, context);
			return;
		}
		boolean hasOption = false;
		Map<String, Option> options = 
				ArgHandlerContextProperties.getOptions(context);
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
	
	protected abstract void handleOption(final String arg, final ArgHandlerContext context);

	protected abstract boolean isOption(final String arg, final ArgHandlerContext context);
	
}
