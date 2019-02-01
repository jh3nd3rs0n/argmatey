package argmatey;

public enum DefaultGnuLongOptionUsageProvider implements OptionUsageProvider {

	INSTANCE;
	
	@Override
	public String getOptionUsage(final OptionUsageParams params) {
		String usage = null;
		OptionArgSpec optionArgSpec = params.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = params.getOption();
		} else {
			if (optionArgSpec.isOptional()) {
				usage = String.format(
						"%s[=%s]", params.getOption(), optionArgSpec.getName());
			} else {
				usage = String.format(
						"%s=%s", params.getOption(), optionArgSpec.getName());
			}
		}
		return usage;
	}

	@Override
	public String toString() {
		return DefaultGnuLongOptionUsageProvider.class.getSimpleName();
	}
	
}
