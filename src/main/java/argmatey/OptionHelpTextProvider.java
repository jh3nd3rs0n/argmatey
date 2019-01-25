package argmatey;

import java.util.List;

public interface OptionHelpTextProvider {

	String getOptionHelpText(List<DocumentableOption> documentableOptions);
	
}
