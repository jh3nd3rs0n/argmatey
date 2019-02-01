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
			String optionArgSeparator = optionArgSpec.getSeparator();
			String optionArgName = optionArgSpec.getName();
			if (optionArgSpec.isOptional()) {
				usage = option;
			} else {
				if (optionArgSeparator.equals(
						OptionArgSpec.DEFAULT_SEPARATOR)) {
					usage = String.format("%s %s", option, optionArgName);
				} else {
					usage = String.format(
							"%1$s %2$s1[%3$s%2$s2]...", 
							option,
							optionArgName,
							optionArgSeparator);
				}
			}
		}		
		return usage;
	}
	
	@Override
	public String toString() {
		return DefaultLongOptionUsageProvider.class.getSimpleName();
	}

}
