package argmatey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class OptionUsageProviders {

	private static final Map<Class<? extends Option>, OptionUsageProvider> DEFAULT_INSTANCES = 
			new HashMap<Class<? extends Option>, OptionUsageProvider>();

	static {
		DEFAULT_INSTANCES.put(
				GnuLongOption.class, DefaultGnuLongOptionUsageProvider.INSTANCE);
		DEFAULT_INSTANCES.put(
				LongOption.class, DefaultLongOptionUsageProvider.INSTANCE);
		DEFAULT_INSTANCES.put(
				PosixOption.class, DefaultPosixOptionUsageProvider.INSTANCE);
	}

	public static OptionUsageProvider getDefault(
			final Class<? extends Option> optClass) {
		return DEFAULT_INSTANCES.get(optClass);
	}

	public static Map<Class<? extends Option>, OptionUsageProvider> getDefaults() {
		return Collections.unmodifiableMap(DEFAULT_INSTANCES);
	}

	public static OptionUsageProvider putDefault(
			final Class<? extends Option> optClass,
			final OptionUsageProvider optUsageProvider) {
		return DEFAULT_INSTANCES.put(optClass, optUsageProvider);
	}

	public static OptionUsageProvider removeDefault(
			final Class<? extends Option> optClass) {
		return DEFAULT_INSTANCES.remove(optClass);
	}

	private OptionUsageProviders() { }

}
