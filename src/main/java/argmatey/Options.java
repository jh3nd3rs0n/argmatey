package argmatey;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Options {
	
	private final List<Option> options;
	
	protected Options() {
		this(new ArrayList<Option>(), DefaultOptionComparator.INSTANCE);
	}
	
	protected Options(final Comparator<Option> comparator) {
		this(new ArrayList<Option>(), comparator);
	}
	
	protected Options(final List<Option> additionalOpts) {
		this(additionalOpts, DefaultOptionComparator.INSTANCE);
	}
	
	protected Options(
			final List<Option> additionalOpts, 
			final Comparator<Option> comparator) {
		Class<?> cls = this.getClass();
		Field[] fields = cls.getFields();
		List<Option> opts = new ArrayList<Option>();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			Class<?> type = field.getType();
			boolean isStatic = Modifier.isStatic(modifiers);
			boolean isFinal = Modifier.isFinal(modifiers);
			boolean isTypeOption = Option.class.isAssignableFrom(type);
			if (isStatic && isFinal && isTypeOption) {
				Option opt = null;
				try {
					opt = (Option) field.get(null);
				} catch (IllegalArgumentException e) {
					throw new AssertionError(e);
				} catch (IllegalAccessException e) {
					throw new AssertionError(e);
				}
				if (opt == null) {
					throw new NullPointerException(String.format(
							"Field '%s %s' in class '%s' must not be null",
							Modifier.toString(modifiers),
							field.getName(),
							cls.getName()));
				}
				opts.add(opt);
			}
		}
		for (Option additionalOpt : additionalOpts) {
			if (additionalOpt == null) {
				throw new NullPointerException(
						"additional Option(s) must not be null");
			}
		}
		opts.addAll(additionalOpts);
		Comparator<Option> cmprtr = comparator;
		if (cmprtr == null) {
			cmprtr = DefaultOptionComparator.INSTANCE;
		}
		Collections.sort(opts, cmprtr);
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
