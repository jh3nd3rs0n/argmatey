package argmatey;

import java.util.Arrays;
import java.util.List;

public final class AddedOptions extends Options {

	public AddedOptions(final List<Option> addedOpts) {
		super(addedOpts, new NullComparator<Option>());
	}
	
	public AddedOptions(final Option... addedOpts) {
		this(Arrays.asList(addedOpts));
	}

}
