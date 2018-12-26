package argmatey;

public enum DefaultLongOptionUsageProvider implements OptionUsageProvider {

	INSTANCE;
	
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
	
	@Override
	public String toString() {
		return DefaultLongOptionUsageProvider.class.getSimpleName();
	}

}
