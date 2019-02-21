package argmatey;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Options {

	public static Options copyOf(final List<Option> opts) {
		return new Options(opts);
	}
	
	public static Options of(final Option... opts) {
		return new Options(Arrays.asList(opts));
	}
	
	public static Options ofFieldValuesFrom(final Object obj) {
		return ofFieldValuesFrom(obj, null);
	}
	
	public static Options ofFieldValuesFrom(
			final Object obj, final Comparator<Option> comparator) {
		Class<?> cls = obj.getClass();
		Field[] fields = cls.getFields();
		List<Option> opts = new ArrayList<Option>();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			Class<?> type = field.getType();
			boolean isInstance = !Modifier.isStatic(modifiers);
			boolean isTypeOption = Option.class.isAssignableFrom(type);
			if (isInstance && isTypeOption) {
				Option opt = null;
				try {
					opt = (Option) field.get(obj);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				}
				if (opt == null) {
					throw new NullPointerException("Option must not be null");
				}
				opts.add(opt);
			}
		}
		Comparator<Option> cmprtr = comparator;
		if (cmprtr == null) {
			cmprtr = DefaultOptionComparator.INSTANCE;
		}
		Collections.sort(opts, cmprtr);
		return new Options(opts);
	}
	
	private final List<Option> options;
	
	private Options(final List<Option> opts) {
		for (Option opt : opts) {
			if (opt == null) {
				throw new NullPointerException("Option(s) must not be null");
			}
		}
		this.options = new ArrayList<Option>(opts);
	}
	
	public final void printHelpText() {
		this.printHelpText(System.out);
	}
	
	public final void printHelpText(final PrintStream s) {
		this.printHelpText(new PrintWriter(s));
	}
	
	public final void printHelpText(final PrintWriter s) {
		boolean earlierHelpTextNotNull = false;
		String lineSeparator = System.getProperty("line.separator");
		for (Option option : this.options) {
			String helpText = option.getHelpText();
			if (helpText != null) {
				if (earlierHelpTextNotNull) {
					s.write(lineSeparator);
				}
				s.write(helpText);
				s.flush();
				if (!earlierHelpTextNotNull) {
					earlierHelpTextNotNull = true;
				}
			}
		}
	}

	public final void printUsage() {
		this.printUsage(System.out);
	}
	
	public final void printUsage(final PrintStream s) {
		this.printUsage(new PrintWriter(s));
	}
	
	public final void printUsage(final PrintWriter s) {
		boolean earlierUsageNotNull = false;
		for (Option option : this.options) {
			String usage = null;
			for (Option opt : option.getAllOptions()) {
				if (!opt.isHidden() && !opt.isSpecial() 
						&& (usage = opt.getUsage()) != null) {
					break;
				}
			}
			if (usage != null) {
				if (earlierUsageNotNull) {
					s.write(" ");
				}
				s.write(String.format("[%s]", usage));
				s.flush();
				if (!earlierUsageNotNull) {
					earlierUsageNotNull = true;
				}
			}
		}
	}
	
	public final List<Option> toList() {
		return Collections.unmodifiableList(this.options);
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName())
			.append(" [options=")
			.append(this.options)
			.append("]");
		return sb.toString();
	}

}
