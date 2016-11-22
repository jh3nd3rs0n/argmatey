package argmatey;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Options {

	private final List<Option> options;
	
	public Options(final List<Option> opts) {
		for (Option opt : opts) {
			if (opt == null) {
				throw new NullPointerException("Option(s) must not be null");
			}
		}
		this.options = new ArrayList<Option>(opts);
	}
	
	public Options(final Option... opts) {
		this(Arrays.asList(opts));
	}
	
	public List<Option> asList() {
		return Collections.unmodifiableList(this.options);
	}
	
	public void printHelpText() {
		this.printHelpText(System.out);
	}
	
	public void printHelpText(final PrintStream s) {
		this.printHelpText(new PrintWriter(s));
	}

	public void printHelpText(final PrintWriter s) {
		boolean earlierHelpTextNotNull = false;
		String lineSeparator = System.getProperty("line.separator");
		for (Option option : this.options) {
			String beforeHelpText = option.getBeforeHelpText();
			String helpText = option.getHelpText();
			String afterHelpText = option.getAfterHelpText();
			if (beforeHelpText != null || helpText != null || afterHelpText != null) {
				if (earlierHelpTextNotNull) {
					s.write(lineSeparator);
				}
				if (beforeHelpText != null) {
					s.write(beforeHelpText);
				}
				if (helpText != null) {
					if (beforeHelpText != null) {
						s.write(lineSeparator);
					}
					s.write(helpText);
				}
				if (afterHelpText != null) {
					if (beforeHelpText != null || helpText != null) {
						s.write(lineSeparator);
					}
					s.write(afterHelpText);
				}
				s.flush();
				if (!earlierHelpTextNotNull) {
					earlierHelpTextNotNull = true;
				}
			}
		}
	}
	
	public void printUsage() {
		this.printUsage(System.out);
	}
	
	public void printUsage(final PrintStream s) {
		this.printUsage(new PrintWriter(s));
	}
	
	public void printUsage(final PrintWriter s) {
		boolean earlierUsageNotNull = false;
		for (Option option : this.options) {
			Option o = null;
			for (Option opt : option.getAllOptions()) {
				if (!opt.isHidden() && !opt.isSpecial()) {
					o = opt;
					break;
				}
			}
			if (o == null) { 
				continue; 
			} 
			String usage = o.getUsage();
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [options=")
			.append(this.options)
			.append("]");
		return builder.toString();
	}

}
