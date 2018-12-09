package argmatey;

public final class DefaultGnuLongOptionUsageProvider implements OptionUsageProvider {

	public static final DefaultGnuLongOptionUsageProvider INSTANCE = 
			new DefaultGnuLongOptionUsageProvider();
	
	private DefaultGnuLongOptionUsageProvider() {
		if (INSTANCE != null) { 
			throw new AssertionError("There can only be one"); 
		}
	}
	
	@Override
	public String getOptionUsage(final Option option) {
		String usage = null;
		OptionArgSpec optionArgSpec = option.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = option.toString();
		} else {
			if (optionArgSpec.isOptional()) {
				usage = String.format("%s[=%s]", option, optionArgSpec.getName());
			} else {
				usage = String.format("%s=%s", option, optionArgSpec.getName());
			}
		}
		return usage;
	}

}
