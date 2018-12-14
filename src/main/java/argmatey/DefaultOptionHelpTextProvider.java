package argmatey;

public final class DefaultOptionHelpTextProvider implements OptionHelpTextProvider {

	public static final DefaultOptionHelpTextProvider INSTANCE = 
			new DefaultOptionHelpTextProvider(); 
	
	private DefaultOptionHelpTextProvider() { 
		if (INSTANCE != null) { 
			throw new AssertionError("There can only be one"); 
		}
	}
	
	@Override
	public String getOptionHelpText(final Option option) {
		String helpText = null;
		StringBuilder sb = null;
		boolean earlierUsageNotNull = false;
		for (Option opt : option.getAllOptions()) {
			if (!opt.isHidden()) {
				String usage = opt.getUsage();
				if (usage != null) {
					if (sb == null) {
						sb = new StringBuilder();
						sb.append("  ");
					}
					if (earlierUsageNotNull) {
						sb.append(", ");
					}
					sb.append(usage);
					if (!earlierUsageNotNull) {
						earlierUsageNotNull = true;
					}
				}
			}
		}
		if (sb != null) {
			String doc = option.getDoc();
			if (doc != null) {
				sb.append(System.getProperty("line.separator"));
				sb.append("      ");
				sb.append(doc);
			}
			helpText = sb.toString();
		}
		return helpText;
	}

}
