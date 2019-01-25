package argmatey;

public interface Option {

	String getName();
	
	OptionArgSpec getOptionArgSpec();
	
	OptionArg newOptionArg(String optionArg);
	
	String toString();
	
}
