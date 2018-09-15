package app.service.dlp.model;

import java.util.ArrayList;
import java.util.List;

public class InspectionResultWrapper {

	List<InspectionEntry> inspectResults;
	boolean sensitiveDataFlag;

	public InspectionResultWrapper() {
		inspectResults = new ArrayList<>();
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

}
