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

	public void printUsage() {
		this.printUsage(System.out);
	}
	
	public void printUsage(final PrintStream s) {
		this.printUsage(new PrintWriter(s));
	}
	
	public void printUsage(final PrintWriter s) {
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
	
	public List<Option> toList() {
		return Collections.unmodifiableList(this.options);
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
