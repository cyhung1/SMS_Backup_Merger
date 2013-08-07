package merger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class Analyze {

	private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();

	public Analyze() {

	}

	public void gatherInfo(File fileToAnaylze) {
		if (fileToAnaylze == null) return;

		try {
			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(fileToAnaylze));

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == "sms") {
						Iterator<?> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							if (attribute.getName().getLocalPart() == "body") System.out.println(attribute.getValue());
						}
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

	public void compare(File firstFile, File secondFile) {
		if (firstFile == null || secondFile == null) return;

		// Read files
		ArrayList<String> firstFileContent = gatherFileContents(firstFile);
		ArrayList<String> secondFileContent = gatherFileContents(secondFile);

		// Compare files
		findDifferences(firstFileContent, secondFileContent);
	}

	private ArrayList<String> gatherFileContents(File file) {
		ArrayList<String> fileContent = new ArrayList<String>();

		try {
			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(file));

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == "sms") {
						fileContent.add(event.toString());
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

		return fileContent;
	}

	private void findDifferences(ArrayList<String> list1, ArrayList<String> list2) {
		for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				System.out.println("File 1: " + list1.get(i));
				System.out.println("File 2: " + list2.get(i));
			}
		}
	}
}
