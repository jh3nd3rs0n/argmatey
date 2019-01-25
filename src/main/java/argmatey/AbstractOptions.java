package argmatey;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AbstractOptions {

	private final List<AbstractOption> abstractOptions;
	
	public AbstractOptions(final AbstractOption... abstractOpts) {
		this(Arrays.asList(abstractOpts));
	}
	
	public AbstractOptions(final List<AbstractOption> abstractOpts) {
		for (AbstractOption abstractOpt : abstractOpts) {
			if (abstractOpt == null) {
				throw new NullPointerException(
						"AbstractOption(s) must not be null");
			}
		}
		this.abstractOptions = new ArrayList<AbstractOption>(abstractOpts);
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
		for (AbstractOption abstractOption : this.abstractOptions) {
			String helpText = abstractOption.getHelpText();
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
		for (AbstractOption abstractOption : this.abstractOptions) {
			AbstractOption firstDisplayableAbstractOption = null;
			for (AbstractOption abstractOpt : abstractOption.getAllAbstractOptions()) {
				if (!abstractOpt.isHidden() && !abstractOpt.isSpecial()) {
					firstDisplayableAbstractOption = abstractOpt;
					break;
				}
			}
			if (firstDisplayableAbstractOption == null) { 
				continue; 
			} 
			String usage = firstDisplayableAbstractOption.getUsage();
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
	
	public List<AbstractOption> toList() {
		return Collections.unmodifiableList(this.abstractOptions);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [abstractOptions=")
			.append(this.abstractOptions)
			.append("]");
		return builder.toString();
	}

}
