package argmatey;

public final class OptionUsageParams {

	private final Option option;
	
	OptionUsageParams(final Option opt) {
		this.option = opt;
	}
	
	public String getOption() {
		return this.option.toString();
	}

	public OptionArgSpec getOptionArgSpec() {
		return this.option.getOptionArgSpec();
	}
			
}