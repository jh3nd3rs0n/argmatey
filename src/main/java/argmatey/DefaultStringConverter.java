package argmatey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class DefaultStringConverter implements StringConverter {
	
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

	private final Class<?> convertedType;
	private final Method staticStringConversionMethod;
	private final Constructor<?> stringParameterConstructor;
			
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
						+ "method return type of the option argument type nor "
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
					if (this.convertedType.isEnum()) {
						StringBuilder sb = new StringBuilder(
								"must be one of the following: ");
						List<?> list = Arrays.asList(
								this.convertedType.getEnumConstants());
						for (Iterator<?> iterator = list.iterator(); 
								iterator.hasNext();) {
							sb.append(iterator.next().toString());
							if (iterator.hasNext()) {
								sb.append(", ");
							}
						}
						cause = new IllegalArgumentException(sb.toString());
					}
					throw new IllegalArgumentException(cause);
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
					throw new IllegalArgumentException(e.getCause());
				}
			}
		}
		return object;
	}

	public Class<?> getConvertedType() {
		return this.convertedType;
	}
	
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
