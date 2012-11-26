package record.metadata;

public class Metadata {
	String[] formats;
	String[] identifiers;
	String[] languages;
	String[] publishers;
	String[] rightss;
	String[] dates;
	String[] descriptions;
	String[] creators;
	String[] coverages;
	String[] types;
	String[] titles;
	String[] subjects;

	public Metadata(String[] formats, String[] identifiers, String[] languages,
			String[] publishers, String[] rightss, String[] dates,
			String[] descriptions, String[] creators, String[] coverages,
			String[] types, String[] titles, String[] subjects,
			String[] contributors) {
		super();
		this.formats = formats;
		this.identifiers = identifiers;
		this.languages = languages;
		this.publishers = publishers;
		this.rightss = rightss;
		this.dates = dates;
		this.descriptions = descriptions;
		this.creators = creators;
		this.coverages = coverages;
		this.types = types;
		this.titles = titles;
		this.subjects = subjects;
		this.contributors = contributors;
	}

	String[] contributors;

	public String[] getMetadatas(MetaDatas md) {
		switch (md) {
		case contributorMax:
			return contributors;
		case coverageMax:
			return coverages;
		case creatorMax:
			return creators;
		case dateMax:
			return dates;
		case descriptionMax:
			return descriptions;
		case formatMax:
			return formats;
		case idMax:
			return identifiers;
		case languageMax:
			return languages;
		case publisherMax:
			return publishers;
		case rightsMax:
			return rightss;
		case subjectMax:
			return subjects;
		case titleMax:
			return titles;
		case typeMax:
			return types;
		default:
			return null;

		}
	}
}
