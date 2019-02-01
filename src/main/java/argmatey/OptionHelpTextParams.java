package argmatey;

import java.util.List;

public interface OptionHelpTextParams {
	
	List<OptionHelpTextParams> getAllOptionHelpTextParams();
	
	List<OptionHelpTextParams> getAllOtherOptionHelpTextParams();

	String getDoc();
	
	String getOption();
	
	OptionArgSpec getOptionArgSpec();
	
	List<OptionHelpTextParams> getOtherOptionHelpTextParams();
	
	String getUsage();
	
	boolean isHidden();
	
}
