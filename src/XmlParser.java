import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import record.PaintingRecord;
import record.header.Header;
import record.metadata.MetaDatas;
import record.metadata.Metadata;

public class XmlParser {

	private static final String DIV = "^";

	public static void main(String[] args) {

		 int lastFile = 112410;
//		int lastFile = 10;
		// Pattern p = Pattern.compile("");
		List<PaintingRecord> records = new ArrayList<PaintingRecord>(lastFile);

		Document dom;
		for (int i = 0; i <= lastFile; i += 10) {
			if (i % 1000 == 0) {
				System.out.println("Checkpoint " + i);
			}
			dom = parseXmlFile("C:/Users/MartyP/Desktop/harvest/" + i + ".xml");
			records.addAll(parseDocument(dom));
		}
		System.out.println(records);

		File outFile = new File("C:/Users/MartyP/Desktop/out.csv");
		try {
			writeCSVfile(outFile, records);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeCSVfile(File outFile, List<PaintingRecord> records)
			throws IOException {
		List<String> lines = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("identifier" + DIV);
		sb.append("datestamp" + DIV);
		for (MetaDatas metadata : MetaDatas.values()) {
			for (int i = 0; i < metadata.getMaxOcc(); i++) {
				sb.append(metadata.toString() + DIV);
			}
		}
		

		lines.add(sb.toString());

		for (PaintingRecord record : records) {
			sb = new StringBuilder();

			Header header = record.getHeader();
			sb.append(header.identifier + DIV);
			sb.append(header.datestamp + DIV);

			for (MetaDatas metadata : MetaDatas.values()) {
				String[] recordMetas = record.getMetadatas(metadata);

				for (int i = 0; i < metadata.getMaxOcc(); i++) {
					if (i < recordMetas.length) {
						sb.append(recordMetas[i] + DIV);
					} else {
						sb.append(DIV);
					}
				}
			}

			System.out.println("_________________");
			lines.add(sb.toString().replaceAll("\t","").replaceAll(
					"\n", " "));
		}

		FileWriter fw = new FileWriter(outFile);
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			System.out.println("Writing CSV file to "+outFile+"...");
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			System.out.println("Written!");
		} finally {
			bw.close();
			fw.close();
		}
	}

	private static Document parseXmlFile(String path) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			return db.parse(path);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	private static List<PaintingRecord> parseDocument(Document dom) {
		List<PaintingRecord> myEmpls = new ArrayList<>();

		// get the root element
		Element docEle = dom.getDocumentElement();

		// get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("record");
		if (nl != null) {
			for (int i = 0; i < nl.getLength(); i++) {
				// get the Record element
				Element el = (Element) nl.item(i);

				// create Record object
				PaintingRecord e = createRecord(el);

				// add it to list
				myEmpls.add(e);
			}
		}
		return myEmpls;
	}

	private static PaintingRecord createRecord(Element el) {
		NodeList list = el.getElementsByTagName("header");
		if (list.getLength() != 1) {
			throw new Error("Number of headers was not 1");
		}
		Header header = createHeader((Element) list.item(0));

		list = el.getElementsByTagName("metadata");
		Metadata metadata = createMetadata((Element) list.item(0));

		if (list.getLength() != 1) {
			throw new Error("Number of metadatas was not 1");
		}

		PaintingRecord record = new PaintingRecord(header, metadata);
		return record;
	}

	private static Metadata createMetadata(Element top) {
		Element item = (Element) (top.getElementsByTagName("oai_dc:dc").item(0));

		String[] formats = getTextValue(item, "dc:format");
		MetaDatas.formatMax.checkMax(formats);

		String[] identifiers = getTextValue(item, "dc:identifier");
		MetaDatas.idMax.checkMax(identifiers);

		String[] languages = getTextValue(item, "dc:language");
		MetaDatas.languageMax.checkMax(languages);

		String[] publishers = getTextValue(item, "dc:publisher");
		MetaDatas.publisherMax.checkMax(publishers);

		String[] rights = getTextValue(item, "dc:rights");
		MetaDatas.rightsMax.checkMax(rights);

		String[] dates = getTextValue(item, "dc:date");
		MetaDatas.dateMax.checkMax(dates);

		String[] descriptions = getTextValue(item, "dc:description");
		MetaDatas.descriptionMax.checkMax(descriptions);

		String[] creators = getTextValue(item, "dc:creator");
		MetaDatas.creatorMax.checkMax(creators);

		String[] types = getTextValue(item, "dc:type");
		MetaDatas.typeMax.checkMax(types);

		String[] titles = getTextValue(item, "dc:title");
		MetaDatas.titleMax.checkMax(titles);

		String[] contributors = getTextValue(item, "dc:contributor");
		MetaDatas.contributorMax.checkMax(contributors);

		String[] coverages = getTextValue(item, "dc:coverage");
		MetaDatas.coverageMax.checkMax(coverages);

		String[] subjects = getTextValue(item, "dc:subject");
		MetaDatas.subjectMax.checkMax(subjects);

		return new Metadata(formats, identifiers, languages, publishers,
				rights, dates, descriptions, creators, coverages, types,
				titles, subjects, contributors);
	}

	private static Header createHeader(Element item) {
		String[] ids = getTextValue(item, "identifier");
		String[] dates = getTextValue(item, "datestamp");

		return new Header(ids[0], dates[0]);
	}

	/**
	 * I take a xml element and the tag name, look for the tag and get the text
	 * content i.e for <employee><name>John</name></employee> xml snippet if the
	 * Element points to employee node and tagName is 'name' I will return John
	 */
	private static String[] getTextValue(Element ele, String tagName) {
		NodeList nl = ele.getElementsByTagName(tagName);
		String[] textVals = new String[nl.getLength()];
		for (int i = 0; i < textVals.length; i++) {
			Element el = (Element) nl.item(i);

			Node child = el.getFirstChild();
			if (child != null) {

				textVals[i] = child.getNodeValue();
				// System.out.println(textVals[i]);
			} else {
				// System.err.println(el);
				textVals[i] = "";
			}
		}

		return textVals;
	}

	// /**
	// * Calls getTextValue and returns a int value
	// */
	// private static int getIntValue(Element ele, String tagName) {
	// //in production application you would catch the exception
	// return Integer.parseInt(getTextValue(ele,tagName));
	// }
}
