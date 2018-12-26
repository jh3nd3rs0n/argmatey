package argmatey;

public enum DefaultPosixOptionUsageProvider implements OptionUsageProvider {

	INSTANCE;
	
	@Override
	public String getOptionUsage(final Option option) {
		String usage = null;
		OptionArgSpec optionArgSpec = option.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = option.toString();
		} else {
			if (optionArgSpec.isOptional()) {
				usage = String.format("%s[%s]", option, optionArgSpec.getName());
			} else {
				usage = String.format("%s %s", option, optionArgSpec.getName());
			}
		}
		return usage;
	}

	@Override
	public String toString() {
		return DefaultPosixOptionUsageProvider.class.getSimpleName();
	}
}
