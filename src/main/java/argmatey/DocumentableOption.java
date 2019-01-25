package argmatey;

public interface DocumentableOption extends Option {

	String getDoc();
	
	String getUsage();
	
	boolean isHidden();
	
	boolean isSpecial();
	
}
