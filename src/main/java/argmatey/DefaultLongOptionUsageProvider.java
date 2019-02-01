package argmatey;

public enum DefaultLongOptionUsageProvider implements OptionUsageProvider {

	INSTANCE;
	
	@Override
	public String getOptionUsage(final OptionUsageParams params) {
		String usage = null;
		String option = params.getOption();
		OptionArgSpec optionArgSpec = params.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = option;
		} else {
			if (optionArgSpec.isOptional()) {
				usage = option;
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
