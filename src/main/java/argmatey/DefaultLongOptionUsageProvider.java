package argmatey;

public final class DefaultLongOptionUsageProvider implements OptionUsageProvider {

	public static final DefaultLongOptionUsageProvider INSTANCE =
			new DefaultLongOptionUsageProvider();
	
	private DefaultLongOptionUsageProvider() {
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
				usage = option.toString();
			} else {
				usage = String.format("%s %s", option, optionArgSpec.getName());
			}
		}		
		return usage;
	}

}
