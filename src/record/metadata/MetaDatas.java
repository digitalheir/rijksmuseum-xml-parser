package record.metadata;

public enum MetaDatas {
	subjectMax("subject"), coverageMax("coverage"), descriptionMax(
			"description"), dateMax("date"), rightsMax("rights"), publisherMax(
			"publisher"), formatMax("format"), idMax("id"), languageMax(
			"language"), creatorMax("creator"), typeMax("type"), titleMax(
			"title"), contributorMax("contributor");

	String toString;
	private int maxOcc = 0;

	MetaDatas(String toString) {
		this.toString = toString;
	}

	public int getMaxOcc() {
		return maxOcc;
	}

	public void checkMax(String[] arr) {
		if (arr.length > maxOcc) {
			maxOcc = arr.length;
		}
	}

	@Override
	public String toString() {
		return toString;
	}

}
