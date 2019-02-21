package argmatey;

import java.util.Comparator;

public enum DefaultOptionComparator implements Comparator<Option> {

	INSTANCE;

	@Override
	public int compare(final Option option1, final Option option2) {
		int diff = option1.getOrdinal() - option2.getOrdinal();
		if (diff == 0) {
			return option1.getName().compareTo(option2.getName());
		}
		return diff;
	}
	
	public String toString() {
		return DefaultOptionComparator.class.getSimpleName();
	}
	
}
