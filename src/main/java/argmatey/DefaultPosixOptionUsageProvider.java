package argmatey;

public enum DefaultPosixOptionUsageProvider implements OptionUsageProvider {

	INSTANCE;
	
	@Override
	public String getOptionUsage(final OptionUsageParams params) {
		String usage = null;
		String option = params.getOption();
		OptionArgSpec optionArgSpec = params.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = option;
		} else {
			String optionArgName = optionArgSpec.getName();
			if (optionArgSpec.isOptional()) {
				usage = String.format("%s[%s]", option, optionArgName);
			} else {
				usage = String.format("%s %s", option, optionArgName);
			}
		}
		return usage;
	}

	@Override
	public String toString() {
		return DefaultPosixOptionUsageProvider.class.getSimpleName();
	}
}
