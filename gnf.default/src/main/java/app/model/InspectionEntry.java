package app.model;

/**
 * @author AdarshSinghal
 *
 */
public class InspectionEntry {

	String quote;
	String infoType;
	String likelihood;
	
	public InspectionEntry() {
	}

	public InspectionEntry(String quote, String infoType, String likelihood) {
		super();
		this.quote = quote;
		this.infoType = infoType;
		this.likelihood = likelihood;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(String likelihood) {
		this.likelihood = likelihood;
	}

	@Override
	public String toString() {
		return "InspectionEntry [quote=" + quote + ", infoType=" + infoType + ", likelihood=" + likelihood + "]";
	}

}
