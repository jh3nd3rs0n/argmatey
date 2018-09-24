package argmatey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Option {

	public static abstract class Builder {
		
		private String afterHelpText;
		private String beforeHelpText;
		private final List<Builder> builders;
		private String doc;
		private boolean hidden;
		private boolean hiddenSet;
		private final String name;
		private OptionArgSpec optionArgSpec;
		private boolean optionArgSpecSet;
		private OptionHelpTextProvider optionHelpTextProvider;
		private OptionUsageProvider optionUsageProvider;
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
			this.special = false;
			this.specialSet = false;
			this.string = opt;
		}
		
		public Builder afterHelpText(final String afterHelpTxt) {
			this.afterHelpText = afterHelpTxt;
			return this;
		}
		
		public Builder beforeHelpText(final String beforeHelpTxt) {
			this.beforeHelpText = beforeHelpTxt;
			return this;
		}
		
		public abstract Option build();
		
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
			return this;
		}
		
		public Builder optionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			this.optionUsageProvider = optUsageProvider;
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
	
	public static OptionHelpTextProvider getDefaultOptionHelpTextProvider() {
		return defaultOptionHelpTextProvider;
	}
	
	public static Map<Class<? extends Option>, OptionUsageProvider> getDefaultOptionUsageProviders() {
		return Collections.unmodifiableMap(DEFAULT_OPTION_USAGE_PROVIDERS);
	}
	
	public static OptionUsageProvider putDefaultOptionUsageProvider(
			final Class<? extends Option> opt,
			final OptionUsageProvider optUsageProvider) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.put(opt, optUsageProvider);
	}
	
	public static OptionUsageProvider removeDefaultOptionUsageProvider(
			final Class<? extends Option> opt) {
		return DEFAULT_OPTION_USAGE_PROVIDERS.remove(opt);
	}
	
	public static void setDefaultOptionHelpTextProvider(
			final OptionHelpTextProvider optHelpTextProvider) {
		defaultOptionHelpTextProvider = optHelpTextProvider;
	}
	
	private final String afterHelpText;
	private final String beforeHelpText;	
	private final String doc;
	private final boolean hidden;
	private final String name;
	private final OptionArgSpec optionArgSpec;
	private final OptionHelpTextProvider optionHelpTextProvider;
	private final List<Option> options;
	private final OptionUsageProvider optionUsageProvider;
	private final boolean special;
	private final String string;

	Option(final Builder builder) {
		String afterHelpTxt = builder.afterHelpText;
		String beforeHelpTxt = builder.beforeHelpText;
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
		List<Option> opts = new ArrayList<Option>();
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
			Option opt = bldr.build();
			opts.add(opt);
		}
		if (optHelpTextProvider == null) {
			optHelpTextProvider = getDefaultOptionHelpTextProvider();
		}
		if (optUsageProvider == null) {
			optUsageProvider = getDefaultOptionUsageProviders().get(this.getClass());
		}
		this.afterHelpText = afterHelpTxt;
		this.beforeHelpText = beforeHelpTxt;
		this.doc = d;
		this.hidden = hide;
		this.name = n;
		this.optionArgSpec = optArgSpec;
		this.optionHelpTextProvider = optHelpTextProvider;
		this.options = opts;
		this.optionUsageProvider = optUsageProvider;
		this.special = spcl;
		this.string = str;
	}
	
	public final String getAfterHelpText() {
		return this.afterHelpText;
	}
	
	public final List<Option> getAllOptions() {
		List<Option> allOptions = new ArrayList<Option>();
		allOptions.add(this);
		for (Option option : this.options) {
			allOptions.addAll(option.getAllOptions());
		}
		return Collections.unmodifiableList(allOptions);
	}
	
	public final String getBeforeHelpText() {
		return this.beforeHelpText;
	}
	
	public final String getDoc() {
		return this.doc;
	}

	public final String getHelpText() {
		String helpText = null;
		if (this.optionHelpTextProvider != null) {
			helpText = this.optionHelpTextProvider.getOptionHelpText(this);
		} else {
			helpText = this.getSelfProvidedHelpText();
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
	
	public final List<Option> getOptions() {
		return Collections.unmodifiableList(this.options);
	}

	public final OptionUsageProvider getOptionUsageProvider() {
		return this.optionUsageProvider;
	}
	
	public final String getSelfProvidedHelpText() {
		String helpText = null;
		List<Option> allDisplayableOptions = new ArrayList<Option>();
		for (Option option : this.getAllOptions()) {
			if (!option.isHidden()) {
				allDisplayableOptions.add(option);
			}
		}
		if (allDisplayableOptions.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("  ");
			boolean earlierUsageNotNull = false;
			for (Option option : allDisplayableOptions) {
				String usage = option.getUsage();
				if (usage != null) {
					if (earlierUsageNotNull) {
						sb.append(", ");
					}
					sb.append(usage);
					if (!earlierUsageNotNull) {
						earlierUsageNotNull = true;
					}
				}
			}
			if (this.doc != null && !this.doc.isEmpty()) {
				sb.append(System.getProperty("line.separator"));
				sb.append("      ");
				sb.append(this.doc);
			}
			helpText = sb.toString();
		}
		return helpText;		
	}
	
	public abstract String getSelfProvidedUsage();

	public final String getUsage() {
		String usage = null;
		if (this.optionUsageProvider != null) {
			usage = this.optionUsageProvider.getOptionUsage(this);
		} else {
			usage = this.getSelfProvidedUsage();
		}
		return usage;
	}

	public final boolean isHidden() {
		return this.hidden;
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
