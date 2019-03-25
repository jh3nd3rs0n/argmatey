package argmatey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Default {@code StringConverter} that converts the provided {@code String} to 
 * an {@code Object} of the provided type. This {@code StringConverter} uses 
 * the provided type's public static method that has one method parameter of 
 * type {@code String} and a method return type of the provided type. If the 
 * provided type does not have that type of method, this 
 * {@code StringConverter} uses the provided type's public instantiatable 
 * constructor that has one constructor parameter of type {@code String}. If 
 * the provided type has neither, a {@code IllegalArgumentException} is thrown.
 */
public final class DefaultStringConverter implements StringConverter {
	
	/**
	 * Returns the provided type's {@code Method} that is public and static 
	 * and has one method parameter of type {@code String} and a method return 
	 * type of the provided type. If the provided type has no such method, 
	 * {@code null} is returned.
	 *  
	 * @param type the provided type
	 * 
	 * @return the provided type's {@code Method} that is public and static 
	 * and has one method parameter of type {@code String} and a method return 
	 * type of the provided type or {@code null} if no such method is found in 
	 * the provided type
	 */
	private static Method getStaticStringConversionMethod(final Class<?> type) {
		for (Method method : type.getDeclaredMethods()) {
			int modifiers = method.getModifiers();
			Class<?> returnType = method.getReturnType();
			Class<?>[] parameterTypes = method.getParameterTypes();
			boolean isPublic = Modifier.isPublic(modifiers);
			boolean isStatic = Modifier.isStatic(modifiers);
			boolean isReturnTypeClass = returnType.equals(type);
			boolean isParameterTypeString = parameterTypes.length == 1 
					&& parameterTypes[0].equals(String.class);
			if (isPublic 
					&& isStatic 
					&& isReturnTypeClass 
					&& isParameterTypeString) {
				return method;
			}
		}
		return null;
	}
	
	/**
	 * Returns the provided type's {@code Constructor} that is public and 
	 * instantiatable and has a constructor parameter of type {@code String}. 
	 * If the provided type has no such constructor, {@code null} is returned.
	 * 
	 * @param type the provided type
	 * 
	 * @return the provided type's {@code Constructor} that is public and 
	 * instantiatable and has a constructor parameter of type {@code String} or 
	 * {@code null} if no such constructor is found
	 */
	private static <T> Constructor<T> getStringParameterConstructor(
			final Class<T> type) {
		for (Constructor<?> constructor : type.getConstructors()) {
			int modifiers = constructor.getModifiers();
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			boolean isInstantiatable = !Modifier.isAbstract(modifiers)
					&& !Modifier.isInterface(modifiers);
			boolean isParameterTypeString = parameterTypes.length == 1 
					&& parameterTypes[0].equals(String.class);
			if (isInstantiatable && isParameterTypeString) {
				@SuppressWarnings("unchecked")
				Constructor<T> ctor = (Constructor<T>) constructor;
				return ctor;
			}
		}
		return null;
	}

	/** 
	 * The provided type of the {@code Object} to which the {@code String} is 
	 * converted. 
	 */
	private final Class<?> convertedType;
	
	/** 
	 * The {@code Method} that is public and static and has one method 
	 * parameter of type {@code String} and a method return type of the 
	 * converted type. 
	 */
	private final Method staticStringConversionMethod;
	
	/**
	 * The {@code Constructor} that is public and instantiatable and has a 
	 * constructor parameter of type {@code String}.
	 */
	private final Constructor<?> stringParameterConstructor;
	
	/**
	 * Constructs a {@code DefaultStringConverter} with the provided type.
	 * 
	 * @param type the provided type
	 * 
	 * @throws IllegalArgumentException if the provided type does not have 
	 * either a public static method that has one method parameter of type 
	 * {@code String} and a method return type of the provided type nor a 
	 * public instantiatable constructor that has one constructor parameter of 
	 * type {@code String}
	 */
	public DefaultStringConverter(final Class<?> type) {
		Method method = null;
		Constructor<?> constructor = null;
		if (!type.equals(String.class)) {
			method = getStaticStringConversionMethod(type);
			if (method == null) {
				constructor = getStringParameterConstructor(type);
			}
			if (method == null && constructor == null) {
				throw new IllegalArgumentException(String.format(
						"type %1$s does not have either a public static method "
						+ "that has one method parameter of type %2$s and a "
						+ "method return type of the provided type nor "
						+ "a public instantiatable constructor that has one "
						+ "constructor parameter of type %2$s", 
						type.getName(),
						String.class.getName()));
			}
		}
		this.convertedType = type;
		this.staticStringConversionMethod = method;
		this.stringParameterConstructor = constructor;
	}
	
	/**
	 * Converts the provided {@code String} to an {@code Object} of the 
	 * provided type. If the provided type is {@code String}, the provided 
	 * {@code String} is returned.
	 * 
	 * @param string the provided {@code String}
	 * 
	 * @return the converted {@code Object} of the provided type or the 
	 * provided {@code String} if the provided type is {@code String}
	 */
	@Override
	public Object convert(final String string) {
		Object object = null;
		if (this.convertedType.equals(String.class)) {
			object = string;
		} else {
			if (this.staticStringConversionMethod != null) {
				try {
					object = this.staticStringConversionMethod.invoke(
							null, string);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (InvocationTargetException e) {
					Throwable cause = e.getCause();
					if (cause instanceof Error) {
						Error err = (Error) cause;
						throw err;
					}
					if (cause instanceof RuntimeException) {
						RuntimeException rte = (RuntimeException) cause;
						throw rte;
					}
					throw new RuntimeException(cause);
				}
			} else if (this.stringParameterConstructor != null) {
				try {
					object = this.stringParameterConstructor.newInstance(
							string);
				} catch (InstantiationException e) {
					throw new AssertionError(e);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (InvocationTargetException e) {
					Throwable cause = e.getCause();
					if (cause instanceof Error) {
						Error err = (Error) cause;
						throw err;
					}
					if (cause instanceof RuntimeException) {
						RuntimeException rte = (RuntimeException) cause;
						throw rte;
					}
					throw new RuntimeException(cause);
				}
			}
		}
		return object;
	}

	/**
	 * Returns the provided type of the {@code Object} to which the 
	 * {@code String} is converted.
	 * 
	 * @return the provided type of the {@code Object} to which the 
	 * {@code String} is converted
	 */
	public Class<?> getConvertedType() {
		return this.convertedType;
	}
	
	/**
	 * Returns the {@code String} representation of this 
	 * {@code DefaultStringConverter}.
	 * 
	 * @return the {@code String} representation of this 
	 * {@code DefaultStringConverter}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [convertedType=")
			.append(this.convertedType)
			.append("]");
		return sb.toString();
	}

}
