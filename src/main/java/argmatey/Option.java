package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Option {

	public static abstract class Builder {
				
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
		private int ordinal;
		private final List<Builder> otherBuilders;
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
			this.hidden = false;
			this.hiddenSet = false;
			this.name = optName;
			this.optionArgSpecSet = false;
			this.optionHelpTextProviderSet = false;
			this.optionUsageProviderSet = false;
			this.ordinal = 0;
			this.otherBuilders = new ArrayList<Builder>();
			this.special = false;
			this.specialSet = false;
			this.string = opt;
		}
		
		public abstract Option build();
		
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
		
		public Builder ordinal(final int i) {
			this.ordinal = i;
			return this;
		}
		
		public Builder otherBuilders(final Builder otherBldr) {
			List<Builder> otherBldrs = new ArrayList<Builder>();
			otherBldrs.add(otherBldr);
			return this.otherBuilders(otherBldrs);
		}
		
		public Builder otherBuilders(
				final Builder otherBldr1, final Builder otherBldr2) {
			List<Builder> otherBldrs = new ArrayList<Builder>();
			otherBldrs.add(otherBldr1);
			otherBldrs.add(otherBldr2);
			return this.otherBuilders(otherBldrs);
		}
		
		public Builder otherBuilders(
				final Builder otherBldr1, 
				final Builder otherBldr2,
				final Builder... additionalOtherBldrs) {
			List<Builder> otherBldrs = new ArrayList<Builder>();
			otherBldrs.add(otherBldr1);
			otherBldrs.add(otherBldr2);
			otherBldrs.addAll(Arrays.asList(additionalOtherBldrs));
			return this.otherBuilders(otherBldrs);
		}
		
		public Builder otherBuilders(final List<Builder> otherBldrs) {
			for (Builder otherBldr : otherBldrs) {
				if (otherBldr == null) {
					throw new NullPointerException("Builder(s) must not be null");
				}
			}
			this.otherBuilders.clear();
			this.otherBuilders.addAll(otherBldrs);
			return this;
		}
		
		public Builder special(final boolean b) {
			this.special = b;
			this.specialSet = true;
			return this;
		}
		
	}
	
	private static final Map<Class<? extends Option>, OptionUsageProvider> DEFAULT_OPTION_USAGE_PROVIDERS =
			new HashMap<Class<? extends Option>, OptionUsageProvider>();
	
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

	public static OptionUsageProvider getDefaultOptionUsageProvider(
			final Class<? extends Option> optClass) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.get(optClass);
	}
	
	public static Map<Class<? extends Option>, OptionUsageProvider> getDefaultOptionUsageProviders() {
		return Collections.unmodifiableMap(DEFAULT_OPTION_USAGE_PROVIDERS);
	}
	
	public static OptionUsageProvider putDefaultOptionUsageProvider(
			final Class<? extends Option> optClass,
			final OptionUsageProvider optUsageProvider) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.put(optClass, optUsageProvider);
	}
	
	public static OptionUsageProvider removeDefaultOptionUsageProvider(
			final Class<? extends Option> optClass) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.remove(optClass);
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
	private final OptionUsageProvider optionUsageProvider;
	private final int ordinal;
	private final List<Option> otherOptions;
	private final boolean special;
	private final String string;

	Option(final Builder builder) {
		String d = builder.doc;
		boolean hide = builder.hidden;
		String n = builder.name;
		OptionArgSpec optArgSpec = builder.optionArgSpec;
		OptionHelpTextProvider optHelpTextProvider = 
				builder.optionHelpTextProvider;
		OptionUsageProvider optUsageProvider = builder.optionUsageProvider;
		int ord = builder.ordinal;
		List<Builder> otherBldrs = new ArrayList<Builder>(
				builder.otherBuilders);
		boolean spcl = builder.special;
		String str = builder.string;
		if (!builder.optionHelpTextProviderSet) {
			optHelpTextProvider = getDefaultOptionHelpTextProvider();
		}
		if (!builder.optionUsageProviderSet) {
			optUsageProvider = getDefaultOptionUsageProvider(this.getClass());
		}
		List<Option> otherOpts = new ArrayList<Option>();
		for (Builder otherBldr : otherBldrs) {
			if (!otherBldr.hiddenSet) {
				otherBldr.hidden(hide);
			}
			if (!otherBldr.optionArgSpecSet) {
				otherBldr.optionArgSpec(optArgSpec);
			}
			if (!otherBldr.specialSet) {
				otherBldr.special(spcl);
			}
			Option otherOpt = otherBldr.build();
			otherOpts.add(otherOpt);
		}		
		this.doc = d;
		this.hidden = hide;
		this.name = n;
		this.optionArgSpec = optArgSpec;
		this.optionHelpTextProvider = optHelpTextProvider;
		this.optionUsageProvider = optUsageProvider;
		this.ordinal = ord;
		this.otherOptions = otherOpts;
		this.special = spcl;
		this.string = str;
	}
	
	public final boolean equalsOrHas(final Option opt) {
		return this.equals(opt) || this.has(opt);
	}
	
	public final boolean equalsOrIsFrom(final Option opt) {
		return this.equals(opt) || this.isFrom(opt);
	}
	
	public final List<Option> getAllOptions() {
		List<Option> allOptions = new ArrayList<Option>();
		allOptions.add(this);
		allOptions.addAll(this.getAllOtherOptions());
		return Collections.unmodifiableList(allOptions);
	}
	
	public final List<Option> getAllOtherOptions() {
		List<Option> allOtherOptions = new ArrayList<Option>();
		for (Option otherOption : this.otherOptions) {
			allOtherOptions.add(otherOption);
			allOtherOptions.addAll(otherOption.getAllOtherOptions());
		}
		return Collections.unmodifiableList(allOtherOptions);
	}
	
	public final String getDoc() {
		return this.doc;
	}
	
	public final String getHelpText() {
		String helpText = null;
		if (this.optionHelpTextProvider != null) {
			helpText = this.optionHelpTextProvider.getOptionHelpText(
					new OptionHelpTextParams(this));
		}
		return helpText;
	}

	public final String getName() {
		return this.name;
	}
	
	public final OptionArgSpec getOptionArgSpec() {
		return this.optionArgSpec;
	}

	public final OptionHelpTextProvider getOptionHelpTextProvider() {
		return this.optionHelpTextProvider;
	}
	
	public final OptionUsageProvider getOptionUsageProvider() {
		return this.optionUsageProvider;
	}

	public final int getOrdinal() {
		return this.ordinal;
	}

	public final List<Option> getOtherOptions() {
		return Collections.unmodifiableList(this.otherOptions);
	}
	
	public final String getUsage() {
		String usage = null;
		if (this.optionUsageProvider != null) {
			usage = this.optionUsageProvider.getOptionUsage(
					new OptionUsageParams(this));
		}
		return usage;
	}
	
	public final boolean has(final Option opt) {
		return this.getAllOtherOptions().contains(opt);
	}

	public final boolean isFrom(final Option opt) {
		return opt.getAllOtherOptions().contains(this);
	}
	
	public final boolean isHidden() {
		return this.hidden;
	}
	
	public final boolean isOf(final String opt) {
		return this.string.equals(opt);
	}
	
	public final boolean isOfAnyOfOrHasOptionOfAnyOf(final List<String> opts) {
		List<Option> allOtherOptions = this.getAllOtherOptions();
		for (String opt : opts) {
			if (this.isOf(opt)) {
				return true;
			}
			for (Option o : allOtherOptions) {
				if (o.isOf(opt)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public final boolean isOfAnyOfOrHasOptionOfAnyOf(
			final String opt1, 
			final String opt2) {
		return this.isOfAnyOfOrHasOptionOfAnyOf(Arrays.asList(opt1, opt2));
	}
	
	public final boolean isOfAnyOfOrHasOptionOfAnyOf(
			final String opt1, 
			final String opt2, 
			final String opt3) {
		return this.isOfAnyOfOrHasOptionOfAnyOf(Arrays.asList(opt1, opt2, 
				opt3));
	}
	
	public final boolean isOfAnyOfOrHasOptionOfAnyOf(
			final String opt1, 
			final String opt2, 
			final String opt3, 
			final String... additionalOpts) {
		List<String> opts = new ArrayList<String>();
		opts.add(opt1);
		opts.add(opt2);
		opts.add(opt3);
		opts.addAll(Arrays.asList(additionalOpts));
		return this.isOfAnyOfOrHasOptionOfAnyOf(opts);
	}
	
	public final boolean isOfOrHasOptionOf(final String opt) {
		if (this.isOf(opt)) {
			return true;
		}
		for (Option o : this.getAllOtherOptions()) {
			if (o.isOf(opt)) {
				return true;
			}
		}
		return false;
	}
	
	public final boolean isSpecial() {
		return this.special;
	}
	
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
				throw new IllegalOptionArgException(this, optionArg, e);
			}
		}
		return optArg;
	}
	
	@Override
	public final String toString() {
		return this.string;
	}

}
