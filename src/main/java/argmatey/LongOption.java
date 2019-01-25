package argmatey;

import java.util.List;

public final class LongOption extends AbstractOption {

	public static final class Builder extends AbstractOption.Builder {

		public Builder(final String optName) {
			super(optName, "-".concat(optName));
		}

		@Override
		public LongOption build() {
			return new LongOption(this);
		}
		
		@Override
		public Builder builders(final argmatey.AbstractOption.Builder bldr) {
			super.builders(bldr);
			return this;
		}
		
		@Override
		public Builder builders(final argmatey.AbstractOption.Builder bldr1,
				final argmatey.AbstractOption.Builder bldr2) {
			super.builders(bldr1, bldr2);
			return this;
		}

		@Override
		public Builder builders(final argmatey.AbstractOption.Builder bldr1,
				final argmatey.AbstractOption.Builder bldr2,
				final argmatey.AbstractOption.Builder... additionalBldrs) {
			super.builders(bldr1, bldr2, additionalBldrs);
			return this;
		}
		
		@Override
		public Builder builders(final List<argmatey.AbstractOption.Builder> bldrs) {
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
	
	private LongOption(final Builder builder) {
		super(builder);
	}

}
