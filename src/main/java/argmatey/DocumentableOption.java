package argmatey;

import java.util.List;

public interface DocumentableOption extends Option {

	List<DocumentableOption> getAllDocumentableOptions();
	
	String getDoc();
	
	List<DocumentableOption> getDocumentableOptions();
	
	String getUsage();
	
	boolean isHidden();
	
	boolean isSpecial();
	
}
