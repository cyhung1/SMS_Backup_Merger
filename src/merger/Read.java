package merger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class Read {

	private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();

	/**
	 * Currently spits out the body of the sms to the console
	 * 
	 * @param fileToAnaylze
	 *            File that is going to be analyzed
	 */
	public void gatherInfo(File fileToAnaylze) {
		if (fileToAnaylze == null) return;

		try {
			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(fileToAnaylze));

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == "smses") { // Get count value
						Attribute count = startElement.getAttributeByName(new QName("count"));
						System.out.println("Count value: " + count.getValue());
						//					} else if (startElement.getName().getLocalPart() == "sms") {
						//						Iterator<?> attributes = startElement.getAttributes();
						//						while (attributes.hasNext()) {
						//							Attribute attribute = (Attribute) attributes.next();
						//							if (attribute.getName().getLocalPart() == "body") System.out.println(attribute.getValue());
						//						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Compare file contents of 2 xml sms files
	 * 
	 * @param firstFile
	 *            First xml sms file to compare
	 * @param secondFile
	 *            Second xml sms file to compare
	 */
	public void compare(File firstFile, File secondFile) {
		if (firstFile == null || secondFile == null) return;

		// Read files
		ArrayList<String> firstFileContent = gatherFileContents(firstFile);
		ArrayList<String> secondFileContent = gatherFileContents(secondFile);

		// Compare files
		findDifferences(firstFileContent, secondFileContent);
	}

	/**
	 * Helper for compare method to collect all sms elements in a file and store
	 * them in an arraylist
	 * 
	 * @param file
	 *            File that all sms elements will be gathered from
	 * @return An arraylist containing the whole sms string as a full string
	 */
	public ArrayList<String> gatherFileContents(File file) {
		ArrayList<String> fileContent = new ArrayList<String>();

		try {
			String line = "";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.readLine(); // Skip XML Version
			reader.readLine(); // Skip XML Style Sheet
			line = reader.readLine(); // SMS Count

			while (!(line = reader.readLine()).contains("</smses>")) {
				fileContent.add(line);
				System.out.println("1) Added " + line);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		try {
		//			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(file));
		//
		//			while (eventReader.hasNext()) {
		//				XMLEvent event = eventReader.nextEvent();
		//				if (event.isStartElement()) {
		//					StartElement startElement = event.asStartElement();
		//					if (startElement.getName().getLocalPart().equals("sms")) {
		//						fileContent.add(event.toString());
		//					}
		//				}
		//			}
		//		} catch (FileNotFoundException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (XMLStreamException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		System.out.println("made it to end of gather contents of " + file.getName());
		return fileContent;
	}

	/**
	 * Helper for compare method to compare 2 arraylists together to find the
	 * differences
	 * 
	 * @param list1
	 *            First arraylist to compare
	 * @param list2
	 *            Second arraylist to compare
	 */
	private void findDifferences(ArrayList<String> list1, ArrayList<String> list2) {
		int numDifferences = 0;
		int numSame = 0;
		//int numUnique = 0;

		for (int i = 0; i < Math.max(list1.size(), list2.size()); i++) {
			if (list1.get(i).equals(list2.get(i))) {
				numSame++;
			} else if (!list1.get(i).equals(list2.get(i))) {
				numDifferences++;
				System.out.println("File 1: " + list1.get(i));
				System.out.println("File 2: " + list2.get(i));
			}

		}
		System.out.println("Found " + numSame + " duplicates");
		System.out.println("Found " + numDifferences + " differences");
	}
}
