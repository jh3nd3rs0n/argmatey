package argmatey;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class DefaultStringConverter implements StringConverter {

	private static final Map<Class<?>, WeakReference<Constructor<?>>> CONSTRUCTORS = 
			new WeakHashMap<Class<?>, WeakReference<Constructor<?>>>();
	
	private static final Map<Class<?>, WeakReference<Method>> METHODS = 
			new WeakHashMap<Class<?>, WeakReference<Method>>();
	
	private static Method getStaticStringConversionMethod(final Class<?> type) {
		Method method = null;
		if (METHODS.containsKey(type)) {
			method = METHODS.get(type).get();
		} else {
			for (Method m : type.getDeclaredMethods()) {
				int modifiers = m.getModifiers();
				Class<?> returnType = m.getReturnType();
				Class<?>[] parameterTypes = m.getParameterTypes();
				boolean isPublic = Modifier.isPublic(modifiers);
				boolean isStatic = Modifier.isStatic(modifiers);
				boolean isReturnTypeClass = returnType.equals(type);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isPublic 
						&& isStatic 
						&& isReturnTypeClass 
						&& isParameterTypeString) {
					method = m;
					break;
				}
			}
			if (method != null) {
				METHODS.put(type, new WeakReference<Method>(method));
			}
		}
		return method;
	}
	
	private static <T> Constructor<T> getStringParameterConstructor(
			final Class<T> type) {
		Constructor<T> constructor = null;
		if (CONSTRUCTORS.containsKey(type)) {
			@SuppressWarnings("unchecked")
			Constructor<T> ctor = (Constructor<T>) CONSTRUCTORS.get(type).get();
			constructor = ctor;
		} else {
			for (Constructor<?> c : type.getConstructors()) {
				int modifiers = c.getModifiers();
				Class<?>[] parameterTypes = c.getParameterTypes();
				boolean isInstantiatable = !Modifier.isAbstract(modifiers)
						&& !Modifier.isInterface(modifiers);
				boolean isParameterTypeString = parameterTypes.length == 1 
						&& parameterTypes[0].equals(String.class);
				if (isInstantiatable && isParameterTypeString) {
					@SuppressWarnings("unchecked")
					Constructor<T> ctor = (Constructor<T>) c;
					constructor = ctor;
					break;
				}
			}
			if (constructor != null) {
				CONSTRUCTORS.put(
						type, 
						new WeakReference<Constructor<?>>(constructor));
			}
		}
		return constructor;
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
						cause = new IllegalArgumentException(
								sb.toString(), cause);
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
