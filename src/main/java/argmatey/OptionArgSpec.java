package argmatey;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class OptionArgSpec {

	public static final class Builder {
				
		private String name;
		private boolean optional;
		private String separator;
		private Class<?> type;
		
		public Builder() {
			this.name = null;
			this.optional = false;
			this.separator = null;
			this.type = null;
		}
		
		public OptionArgSpec build() {
			return new OptionArgSpec(this); 
		}
		
		public Builder name(final String n) {
			this.name = n;
			return this;
		}
		
		public Builder optional(final boolean b) {
			this.optional = b;
			return this;
		}
		
		public Builder separator(final String s) {
			this.separator = s;
			return this;
		}
		
		public Builder type(final Class<?> t) {
			if (t != null && !t.equals(String.class)) {
				if (getStaticStringConversionMethod(t) == null 
						&& getStringParameterConstructor(t) == null) {
					throw new IllegalArgumentException(String.format(
							"option argument type %1$s does not have "
							+ "either a public static method that has one "
							+ "method parameter of type %2$s and a method "
							+ "return type of the option argument type nor "
							+ "a public instantiatable constructor that "
							+ "has one constructor parameter of type %2$s", 
							t.getName(),
							String.class.getName()));
				}
			}
			this.type = t;
			return this;
		}
		
	}

	private static final Map<Class<?>, WeakReference<Constructor<?>>> CONSTRUCTORS = 
			new WeakHashMap<Class<?>, WeakReference<Constructor<?>>>();
	
	public static final String DEFAULT_NAME = "option_argument";
	public static final String DEFAULT_SEPARATOR = "[^\\w\\W]";
	public static final Class<?> DEFAULT_TYPE = String.class;
	
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
	
	private final String separator;
	private final String name;
	private final boolean optional;
	private final Class<?> type;
	
	private OptionArgSpec(final Builder builder) {
		String n = builder.name;
		boolean o = builder.optional;
		String s = builder.separator;		
		Class<?> t = builder.type;
		if (n == null) { n = DEFAULT_NAME; }
		if (s == null) { s = DEFAULT_SEPARATOR;	}
		if (t == null) { t = DEFAULT_TYPE; }
		this.name = n;
		this.optional = o;
		this.separator = s;
		this.type = t;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSeparator() {
		return this.separator;
	}
	
	public Class<?> getType() {
		return this.type;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public OptionArg newOptionArg(final String optionArg) {
		if (optionArg == null) {
			throw new NullPointerException("option argument must not be null");
		}
		List<String> optArgs = Arrays.asList(optionArg.split(this.separator));
		List<Object> objectValues = new ArrayList<Object>();
		List<OptionArg> opArgs = new ArrayList<OptionArg>();
		if (optArgs.size() == 1) {
			Object objectValue = null;
			if (this.type.equals(String.class)) {
				objectValue = optionArg;
			} else {
				Method method = getStaticStringConversionMethod(this.type);
				Constructor<?> constructor = null;
				if (method == null) {
					constructor = getStringParameterConstructor(this.type);
				}
				if (method != null) {
					try {
						objectValue = method.invoke(null, optionArg);
					} catch (IllegalAccessException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
						throw new AssertionError(e);
					} catch (InvocationTargetException e) {
						throw new IllegalArgumentException(e.getCause());
					}
				} else if (constructor != null) {
					try {
						objectValue = constructor.newInstance(optionArg);
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
			objectValues.add(objectValue);
		} else {
			for (String optArg : optArgs) {
				OptionArg opArg = this.newOptionArg(optArg);
				objectValues.addAll(opArg.toObjectValues());
				opArgs.add(opArg);
			}
		}
		return new OptionArg(objectValues, opArgs, this, optionArg);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [separator=")
			.append(this.separator)
			.append(", name=")
			.append(this.name)
			.append(", optional=")
			.append(this.optional)
			.append(", type=")
			.append(this.type)
			.append("]");
		return sb.toString();
	}

}
