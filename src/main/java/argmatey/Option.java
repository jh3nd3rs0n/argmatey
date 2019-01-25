package argmatey;

import java.util.List;

public interface Option {

	List<Option> getAllOptions();
	
	String getName();
	
	OptionArgSpec getOptionArgSpec();
	
	List<Option> getOptions();
	
	OptionArg newOptionArg(String optionArg);
	
	String toString();
	
}
