package argmatey;

public final class OptionHelpTextProviders {
	
	private static OptionHelpTextProvider defaultInstance = 
			DefaultOptionHelpTextProvider.INSTANCE;
	
	public static OptionHelpTextProvider getDefault() {
		return defaultInstance;
	}

	public static void setDefault(
			final OptionHelpTextProvider optHelpTextProvider) {
		defaultInstance = optHelpTextProvider;
	}

	private OptionHelpTextProviders() {	}

}
