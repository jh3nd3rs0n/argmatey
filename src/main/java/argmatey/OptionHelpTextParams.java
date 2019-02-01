package argmatey;

import java.util.List;

public interface OptionHelpTextParams extends OptionUsageParams {

	List<OptionHelpTextParams> getAllOptionHelpTextParams();
	
	List<OptionHelpTextParams> getAllOtherOptionHelpTextParams();
	
	String getDoc();
	
	List<OptionHelpTextParams> getOtherOptionHelpTextParams();
	
	String getUsage();
	
	boolean isHidden();
	
}
