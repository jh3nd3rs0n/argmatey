package argmatey;

public final class NonparsedArg implements ParseResult {

	private final String string;
	
	NonparsedArg(final String arg) {
		this.string = arg;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NonparsedArg)) {
			return false;
		}
		NonparsedArg other = (NonparsedArg) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.string;
	}
}
