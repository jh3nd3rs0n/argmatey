package argmatey;

import java.util.Arrays;
import java.util.List;

public final class AddedOptions extends Options {

	public AddedOptions(final List<Option> opts) {
		super(opts, new NullComparator<Option>());
	}
	
	public AddedOptions(final Option... opts) {
		this(Arrays.asList(opts));
	}

}
