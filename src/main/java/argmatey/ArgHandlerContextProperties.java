package argmatey;

import java.util.Collections;
import java.util.Map;

final class ArgHandlerContextProperties {
	
	public static Map<String, Option> getOptions(
			final ArgHandlerContext context) {
		Map<String, Option> options = Collections.emptyMap();
		Object value = context.getProperty(
				ArgHandlerContextPropertyNames.OPTIONS);
		if (value != null) {
			@SuppressWarnings("unchecked")
			Map<String, Option> opts = (Map<String, Option>) value;
			options = opts;
		}
		return Collections.unmodifiableMap(options);
	}

	public static boolean isOptionHandlingEnabled(
			final ArgHandlerContext context) {
		boolean optionHandlingEnabled = false;
		Object value = context.getProperty(
				ArgHandlerContextPropertyNames.OPTION_HANDLING_ENABLED);
		if (value != null) {
			Boolean optHandlingEnabled = (Boolean) value;
			optionHandlingEnabled = optHandlingEnabled.booleanValue();
		}
		return optionHandlingEnabled;
	}

	public static void setOptionHandlingEnabled(
			final ArgHandlerContext context, 
			final boolean optionHandlingEnabled) {
		context.putProperty(
				ArgHandlerContextPropertyNames.OPTION_HANDLING_ENABLED, 
				Boolean.valueOf(optionHandlingEnabled));		
	}
	
	public static void setParseResult(final ArgHandlerContext context, 
			final ParseResult parseResult) {
		context.putProperty(
				ArgHandlerContextPropertyNames.PARSE_RESULT, 
				parseResult);
	}
	
	private ArgHandlerContextProperties() { }
	
}