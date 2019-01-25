package argmatey;

import java.util.List;

public enum DefaultOptionHelpTextProvider implements OptionHelpTextProvider {

	INSTANCE; 
	
	@Override
	public String getOptionHelpText(
			final List<DocumentableOption> documentableOptions) {
		String helpText = null;
		StringBuilder sb = null;
		boolean earlierUsageNotNull = false;
		String doc = null;
		for (DocumentableOption documentableOpt : documentableOptions) {
			if (!documentableOpt.isHidden()) {
				String usage = documentableOpt.getUsage();
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
			if (doc == null) {
				doc = documentableOpt.getDoc();
			}
		}
		if (sb != null) {
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
