package argmatey;

/**
 * Converts the provided {@code String} to an {@code Object}.
 */
public interface StringConverter {

	/**
	 * Converts the provided {@code String} to an {@code Object}.
	 * 
	 * @param string the provided {@code String}
	 * 
	 * @return the converted {@code Object}
	 * 
	 * @throws IllegalArgumentException if the provided {@code String} is 
	 * illegal or inappropriate
	 */
	Object convert(String string);
	
}
