package argmatey;

import java.util.List;

public final class GnuLongOption extends Option {

	public static final class Builder extends Option.Builder {

		public Builder(final String optName) {
			super(optName, "--".concat(optName));
		}

		@Override
		public GnuLongOption build() {
			return new GnuLongOption(this);
		}
		
		@Override
		public Builder builders(final argmatey.Option.Builder bldr) {
			super.builders(bldr);
			return this;
		}
		
		@Override
		public Builder builders(final argmatey.Option.Builder bldr1,
				final argmatey.Option.Builder bldr2) {
			super.builders(bldr1, bldr2);
			return this;
		}
		
		@Override
		public Builder builders(final argmatey.Option.Builder bldr1,
				final argmatey.Option.Builder bldr2,
				final argmatey.Option.Builder... additionalBldrs) {
			super.builders(bldr1, bldr2, additionalBldrs);
			return this;
		}
		
		@Override
		public Builder builders(final List<argmatey.Option.Builder> bldrs) {
			super.builders(bldrs);
			return this;
		}
		
		@Override
		public Builder doc(final String d) {
			super.doc(d);
			return this;
		}
		
		@Override
		public Builder hidden(final boolean b) {
			super.hidden(b);
			return this;
		}
		
		@Override
		public Builder optionArgSpec(final OptionArgSpec optArgSpec) {
			super.optionArgSpec(optArgSpec);
			return this;
		}
		
		@Override
		public Builder optionHelpTextProvider(
				final OptionHelpTextProvider optHelpTextProvider) {
			super.optionHelpTextProvider(optHelpTextProvider);
			return this;
		}
		
		@Override
		public Builder optionUsageProvider(
				final OptionUsageProvider optUsageProvider) {
			super.optionUsageProvider(optUsageProvider);
			return this;
		}
		
		@Override
		public Builder special(final boolean b) {
			super.special(b);
			return this;
		}
		
	}
	
	private GnuLongOption(final Builder builder) {
		super(builder);
	}

	@Override
	public String getSelfProvidedUsage() {
		String usage = null;
		OptionArgSpec optionArgSpec = this.getOptionArgSpec();
		if (optionArgSpec == null) {
			usage = this.toString();
		} else {
			if (optionArgSpec.isOptional()) {
				usage = String.format("%s[=%s]", this, optionArgSpec.getName());
			} else {
				usage = String.format("%s=%s", this, optionArgSpec.getName());
			}
		}
		return usage;
	}

}
