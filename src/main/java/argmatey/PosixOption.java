package argmatey;

import java.util.List;

public final class PosixOption extends Option {

	public static final class Builder extends Option.Builder {

		private static String toPosixOptionName(final char ch) {
			if (!Character.isLetterOrDigit(ch)) {
				throw new IllegalArgumentException("option name must be a letter or a digit");
			}
			return Character.toString(ch);
		}
		
		public Builder(final char optName) {
			super(toPosixOptionName(optName), "-".concat(toPosixOptionName(optName)));
		}

		@Override
		public PosixOption build() {
			return new PosixOption(this);
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
		public Builder otherBuilders(final argmatey.Option.Builder otherBldr) {
			super.otherBuilders(otherBldr);
			return this;
		}
		
		@Override
		public Builder otherBuilders(final argmatey.Option.Builder otherBldr1,
				final argmatey.Option.Builder otherBldr2) {
			super.otherBuilders(otherBldr1, otherBldr2);
			return this;
		}
		
		@Override
		public Builder otherBuilders(final argmatey.Option.Builder otherBldr1,
				final argmatey.Option.Builder otherBldr2,
				final argmatey.Option.Builder... additionalOtherBldrs) {
			super.otherBuilders(otherBldr1, otherBldr2, additionalOtherBldrs);
			return this;
		}
		
		@Override
		public Builder otherBuilders(
				final List<argmatey.Option.Builder> otherBldrs) {
			super.otherBuilders(otherBldrs);
			return this;
		}
		
		@Override
		public Builder special(final boolean b) {
			super.special(b);
			return this;
		}
		
	}
	
	private PosixOption(final Builder builder) {
		super(builder);
	}
	
}
