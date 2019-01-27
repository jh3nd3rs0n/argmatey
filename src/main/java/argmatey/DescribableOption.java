package argmatey;

import java.util.List;

public interface DescribableOption extends Option {

	List<DescribableOption> getAllDescribableOptions();
	
	List<DescribableOption> getDescribableOptions();
	
	String getDoc();
	
	String getUsage();
	
	boolean isHidden();
	
	boolean isSpecial();
	
}
