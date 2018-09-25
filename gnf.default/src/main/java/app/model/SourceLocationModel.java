package app.model;

/**
 * @author AdarshSinghal
 *
 */
public class SourceLocationModel {

	private String fileName;
	private long lineNumber;
	private String function;

	public String getFileName() {
		return fileName;
	}

	public SourceLocationModel() {

	}

	public SourceLocationModel(String fileName) {
		this.fileName = fileName;
	}

	public SourceLocationModel(String fileName, long lineNumber) {
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}

	public SourceLocationModel(String fileName, String function) {
		this.fileName = fileName;
		this.function = function;
	}

	public SourceLocationModel(String fileName, long lineNumber, String function) {
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.function = function;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return "SourceLocationModel [fileName=" + fileName + ", lineNumber=" + lineNumber + ", function=" + function
				+ "]";
	}

}
