package argmatey;

public enum DefaultOptionHelpTextProvider implements OptionHelpTextProvider {

	INSTANCE; 
	
	@Override
	public String getOptionHelpText(final OptionHelpTextParams params) {
		String helpText = null;
		StringBuilder sb = null;
		boolean earlierUsageNotNull = false;
		for (OptionHelpTextParams p : params.getAllOptionHelpTextParams()) {
			if (!p.isHidden()) {
				String usage = p.getUsage();
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
			String doc = params.getDoc();
			if (doc != null) {
				sb.append(System.getProperty("line.separator"));
				sb.append("      ");
				sb.append(doc);
			}
			helpText = sb.toString();
		}
		return helpText;
	}

	@Override
	public String toString() {
		return DefaultOptionHelpTextProvider.class.getSimpleName();
	}
}
