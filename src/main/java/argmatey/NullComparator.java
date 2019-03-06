package argmatey;

import java.util.Comparator;

public class NullComparator<T> implements Comparator<T> {
	
	public NullComparator() { }
	
	@Override
	public int compare(T t1, T t2) {
		return 0;
	}
	
	@Override
	public String toString() {
		return NullComparator.class.getSimpleName();
	}

}
