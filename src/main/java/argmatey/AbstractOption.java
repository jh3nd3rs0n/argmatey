package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOption implements DescribableOption {

	public static abstract class Builder {
		
		private final List<Builder> builders;
		private String doc;
		private boolean hidden;
		private boolean hiddenSet;
		private final String name;
		private OptionArgSpec optionArgSpec;
		private boolean optionArgSpecSet;
		private OptionHelpTextProvider optionHelpTextProvider;
		private boolean optionHelpTextProviderSet;
		private OptionUsageProvider optionUsageProvider;
		private boolean optionUsageProviderSet;
		private boolean special;
		private boolean specialSet;
		private String string;
		
		Builder(final String optName, final String opt) {
			if (optName == null) {
				throw new NullPointerException("option name must not be null");
			}
			if (optName.isEmpty()) {
				throw new IllegalArgumentException("option name must not be empty");
			}
			if (opt == null) {
				throw new NullPointerException("option must not be null");
			}
			if (opt.isEmpty()) {
				throw new IllegalArgumentException("option must not be empty");
			}
			this.builders = new ArrayList<Builder>();
			this.hidden = false;
			this.hiddenSet = false;
			this.name = optName;
			this.optionArgSpecSet = false;
			this.optionHelpTextProviderSet = false;
			this.optionUsageProviderSet = false;
			this.special = false;
			this.specialSet = false;
			this.string = opt;
		}
		
		public abstract AbstractOption build();
		
		public Builder builders(final Builder bldr) {
			List<Builder> bldrs = new ArrayList<Builder>();
			bldrs.add(bldr);
			return this.builders(bldrs);
		}
		
		public Builder builders(final Builder bldr1, final Builder bldr2) {
			List<Builder> bldrs = new ArrayList<Builder>();
			bldrs.add(bldr1);
			bldrs.add(bldr2);
			return this.builders(bldrs);
		}
		
		public Builder builders(final Builder bldr1, final Builder bldr2,
				final Builder... additionalBldrs) {
			List<Builder> bldrs = new ArrayList<Builder>();
			bldrs.add(bldr1);
			bldrs.add(bldr2);
			bldrs.addAll(Arrays.asList(additionalBldrs));
			return this.builders(bldrs);
		}
		
		public Builder builders(final List<Builder> bldrs) {
			for (Builder bldr : bldrs) {
				if (bldr == null) {
					throw new NullPointerException("Builder(s) must not be null");
				}
			}
			this.builders.clear();
			this.builders.addAll(bldrs);
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
		
		public Builder hidden(final boolean b) {
			this.hidden = b;
			this.hiddenSet = true;
			return this;
		}
		
		public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
			this.optionArgSpec = optArgSpec;
			this.optionArgSpecSet = true;
			return this;
		}
		
		public Builder optionHelpTextProvider(
				final OptionHelpTextProvider optHelpTextProvider) {
			this.optionHelpTextProvider = optHelpTextProvider;
			this.optionHelpTextProviderSet = true;
			return this;
		}
		
		public Builder optionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			this.optionUsageProvider = optUsageProvider;
			this.optionUsageProviderSet = true;
			return this;
		}
		
		public Builder special(final boolean b) {
			this.special = b;
			this.specialSet = true;
			return this;
		}
		
	}
	
	private static final Map<Class<? extends AbstractOption>, OptionUsageProvider> DEFAULT_OPTION_USAGE_PROVIDERS =
			new HashMap<Class<? extends AbstractOption>, OptionUsageProvider>();
	
	private static OptionHelpTextProvider defaultOptionHelpTextProvider;
	
	static {
		DEFAULT_OPTION_USAGE_PROVIDERS.put(
				GnuLongOption.class, DefaultGnuLongOptionUsageProvider.INSTANCE);
		DEFAULT_OPTION_USAGE_PROVIDERS.put(
				LongOption.class, DefaultLongOptionUsageProvider.INSTANCE);
		DEFAULT_OPTION_USAGE_PROVIDERS.put(
				PosixOption.class, DefaultPosixOptionUsageProvider.INSTANCE);
		defaultOptionHelpTextProvider = DefaultOptionHelpTextProvider.INSTANCE;
	}
	
	public static OptionHelpTextProvider getDefaultOptionHelpTextProvider() {
		return defaultOptionHelpTextProvider;
	}
	
	public static Map<Class<? extends AbstractOption>, OptionUsageProvider> getDefaultOptionUsageProviders() {
		return Collections.unmodifiableMap(DEFAULT_OPTION_USAGE_PROVIDERS);
	}
	
	public static OptionUsageProvider putDefaultOptionUsageProvider(
			final Class<? extends AbstractOption> abstractOptClass,
			final OptionUsageProvider optUsageProvider) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.put(abstractOptClass, optUsageProvider);
	}
	
	public static OptionUsageProvider removeDefaultOptionUsageProvider(
			final Class<? extends AbstractOption> abstractOptClass) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.remove(abstractOptClass);
	}
	
	public static void setDefaultOptionHelpTextProvider(
			final OptionHelpTextProvider optHelpTextProvider) {
		defaultOptionHelpTextProvider = optHelpTextProvider;
	}
	
	private final String doc;
	private final boolean hidden;
	private final String name;
	private final OptionArgSpec optionArgSpec;
	private final OptionHelpTextProvider optionHelpTextProvider;
	private final List<AbstractOption> abstractOptions;
	private final OptionUsageProvider optionUsageProvider;
	private final boolean special;
	private final String string;

	AbstractOption(final Builder builder) {
		List<Builder> bldrs = new ArrayList<Builder>(builder.builders);
		String d = builder.doc;
		boolean hide = builder.hidden;
		String n = builder.name;
		OptionArgSpec optArgSpec = builder.optionArgSpec;
		OptionHelpTextProvider optHelpTextProvider = 
				builder.optionHelpTextProvider;
		OptionUsageProvider optUsageProvider = builder.optionUsageProvider;
		boolean spcl = builder.special;
		String str = builder.string;
		List<AbstractOption> abstractOpts = new ArrayList<AbstractOption>();
		for (Builder bldr : bldrs) {
			if (!bldr.hiddenSet) {
				bldr.hidden(hide);
			}
			if (!bldr.optionArgSpecSet) {
				bldr.optionArgSpec(optArgSpec);
			}
			if (!bldr.specialSet) {
				bldr.special(spcl);
			}
			AbstractOption abstractOpt = bldr.build();
			abstractOpts.add(abstractOpt);
		}
		if (!builder.optionHelpTextProviderSet) {
			optHelpTextProvider = getDefaultOptionHelpTextProvider();
		}
		if (!builder.optionUsageProviderSet) {
			optUsageProvider = getDefaultOptionUsageProviders().get(this.getClass());
		}
		this.abstractOptions = abstractOpts;
		this.doc = d;
		this.hidden = hide;
		this.name = n;
		this.optionArgSpec = optArgSpec;
		this.optionHelpTextProvider = optHelpTextProvider;
		this.optionUsageProvider = optUsageProvider;
		this.special = spcl;
		this.string = str;
	}
	
	public final List<AbstractOption> getAbstractOptions() {
		return Collections.unmodifiableList(this.abstractOptions);
	}
	
	public final List<AbstractOption> getAllAbstractOptions() {
		List<AbstractOption> allAbstractOptions = new ArrayList<AbstractOption>();
		allAbstractOptions.add(this);
		for (AbstractOption abstractOption : this.abstractOptions) {
			allAbstractOptions.addAll(abstractOption.getAllAbstractOptions());
		}
		return Collections.unmodifiableList(allAbstractOptions);
	}
	
	@Override
	public final List<DescribableOption> getAllDescribableOptions() {
		List<DescribableOption> allDescribableOptions = 
				new ArrayList<DescribableOption>();
		allDescribableOptions.addAll(this.getAllAbstractOptions());
		return Collections.unmodifiableList(allDescribableOptions);
	}
	
	@Override
	public final List<Option> getAllOptions() {
		List<Option> allOptions = new ArrayList<Option>();
		allOptions.addAll(this.getAllAbstractOptions());
		return Collections.unmodifiableList(allOptions);
	}
	
	@Override
	public final List<DescribableOption> getDescribableOptions() {
		List<DescribableOption> describableOptions = 
				new ArrayList<DescribableOption>();
		describableOptions.addAll(this.abstractOptions);
		return Collections.unmodifiableList(describableOptions);
	}

	@Override
	public final String getDoc() {
		return this.doc;
	}
	
	public final String getHelpText() {
		String helpText = null;
		if (this.optionHelpTextProvider != null) {
			helpText = this.optionHelpTextProvider.getOptionHelpText(this);
		}
		return helpText;
	}

	@Override
	public final String getName() {
		return this.name;
	}
	
	@Override
	public final OptionArgSpec getOptionArgSpec() {
		return this.optionArgSpec;
	}

	public final OptionHelpTextProvider getOptionHelpTextProvider() {
		return this.optionHelpTextProvider;
	}
	
	@Override
	public final List<Option> getOptions() {
		List<Option> options = new ArrayList<Option>();
		options.addAll(this.abstractOptions);
		return Collections.unmodifiableList(options);
	}

	public final OptionUsageProvider getOptionUsageProvider() {
		return this.optionUsageProvider;
	}
	
	@Override
	public final String getUsage() {
		String usage = null;
		if (this.optionUsageProvider != null) {
			usage = this.optionUsageProvider.getOptionUsage(this);
		}
		return usage;
	}

	@Override
	public final boolean isHidden() {
		return this.hidden;
	}

	@Override
	public final boolean isSpecial() {
		return this.special;
	}
	
	@Override
	public final OptionArg newOptionArg(final String optionArg) {
		if (optionArg == null && this.optionArgSpec != null 
				&& !this.optionArgSpec.isOptional()) {
			throw new OptionArgRequiredException(this);
		}
		if (optionArg != null && this.optionArgSpec == null) {
			throw new OptionArgNotAllowedException(this);
		}
		OptionArg optArg = null;
		if (optionArg != null && this.optionArgSpec != null) {
			try {
				optArg = this.optionArgSpec.newOptionArg(optionArg);
			} catch (IllegalArgumentException e) {
				throw new IllegalOptionArgException(this, optionArg, e.getCause());
			}
		}
		return optArg;
	}
	
	@Override
	public final String toString() {
		return this.string;
	}

}
