package app.model;

import java.util.List;

public class InspectionResultWrapper {

	List<InspectionEntry> inspectResults;
	boolean sensitiveDataFlag;

	public InspectionResultWrapper() {

	}

	public InspectionResultWrapper(List<InspectionEntry> inspectResults) {
		super();
		this.inspectResults = inspectResults;
	}

	public List<InspectionEntry> getInspectResults() {
		return inspectResults;
	}

	public void setInspectResults(List<InspectionEntry> inspectResults) {
		this.inspectResults = inspectResults;
	}

	public boolean getSensitiveDataFlag() {
		return sensitiveDataFlag;
	}

	public void setSensitiveDataFlag(boolean sensitiveDataFlag) {
		this.sensitiveDataFlag = sensitiveDataFlag;
	}

	@Override
	public String toString() {
		return "InspectionResultWrapper [inspectResults=" + inspectResults + ", sensitiveDataFlag=" + sensitiveDataFlag
				+ "]";
	}

}
