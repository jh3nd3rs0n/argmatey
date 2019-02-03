package argmatey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OptionHelpTextParams {

	private final Option option;
	private final List<OptionHelpTextParams> otherOptionHelpTextParams;
	
	OptionHelpTextParams(final Option opt) {
		List<OptionHelpTextParams> otherParams = 
				new ArrayList<OptionHelpTextParams>();
		for (Option o : opt.getOtherOptions()) {
			otherParams.add(new OptionHelpTextParams(o));
		}
		this.option = opt;
		this.otherOptionHelpTextParams = otherParams;
	}
	
	public List<OptionHelpTextParams> getAllOptionHelpTextParams() {
		List<OptionHelpTextParams> allOptionHelpTextParams = 
				new ArrayList<OptionHelpTextParams>();
		allOptionHelpTextParams.add(this);
		allOptionHelpTextParams.addAll(this.getAllOtherOptionHelpTextParams());
		return Collections.unmodifiableList(allOptionHelpTextParams);
	}
	
	public List<OptionHelpTextParams> getAllOtherOptionHelpTextParams() {
		List<OptionHelpTextParams> allOtherOptionHelpTextParams =
				new ArrayList<OptionHelpTextParams>();
		for (OptionHelpTextParams otherParams : this.otherOptionHelpTextParams) {
			allOtherOptionHelpTextParams.add(otherParams);
			allOtherOptionHelpTextParams.addAll(
					otherParams.getAllOtherOptionHelpTextParams());
		}
		return Collections.unmodifiableList(allOtherOptionHelpTextParams);
	}

	public String getDoc() {
		return this.option.getDoc();
	}

	public String getOption() {
		return this.option.toString();
	}

	public OptionArgSpec getOptionArgSpec() {
		return this.option.getOptionArgSpec();
	}

	public List<OptionHelpTextParams> getOtherOptionHelpTextParams() {
		return Collections.unmodifiableList(this.otherOptionHelpTextParams);
	}

	public String getUsage() {
		return this.option.getUsage();
	}

	public boolean isHidden() {
		return this.option.isHidden();
	}
	
}