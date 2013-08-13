package merger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class Read {

	private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();

	/**
	 * Compare file contents of 2 xml sms files
	 * 
	 * @param firstFile
	 *            First xml sms file to compare
	 * @param secondFile
	 *            Second xml sms file to compare
	 */
	public void compare(File[] listOfFiles) {
		if (!(listOfFiles.length > 0)) return; // Files must exist

		for (File f : listOfFiles) { // Files must not be null
			if (f == null) return;
		}

		//TODO info gathered might need to be displayed in a JDialog instead of system console
		ArrayList<SMS> list = new ArrayList<SMS>();
		for (File f : listOfFiles) {
			list = createSmsNodes(f);
		}

		for (SMS sms : list) {
			System.out.println(sms);
		}
		
	}

	/**
	 * Take xml file and parse all the sms nodes in it into a sms object and
	 * store them in an arraylist
	 * 
	 * @param xmlFile
	 *            passed in xml file
	 * @return list of sms nodes
	 */
	private ArrayList<SMS> createSmsNodes(File xmlFile) {
		ArrayList<SMS> smsList = new ArrayList<SMS>();
		try {
			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(xmlFile));

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == "sms") { // Get sms node
						SMS sms = new SMS();
						Iterator<?> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							System.out.println(attribute.getName().toString() + " --> " + attribute.getValue());
							switch (attribute.getName().toString()) {
							case "protocol":
								sms.setProtocol(attribute.getValue());
								break;
							case "address":
								sms.setAddress(attribute.getValue());
								break;
							case "date":
								sms.setDate(attribute.getValue());
								break;
							case "type":
								sms.setType(attribute.getValue());
								break;
							case "subject":
								sms.setSubject(attribute.getValue());
								break;
							case "body":
								sms.setBody(attribute.getValue());
								break;
							case "toa":
								sms.setToa(attribute.getValue());
								break;
							case "sc_toa":
								sms.setSc_toa(attribute.getValue());
								break;
							case "service_center":
								sms.setService_Center(attribute.getValue());
								break;
							case "read":
								sms.setRead(attribute.getValue());
								break;
							case "status":
								sms.setStatus(attribute.getValue());
								break;
							case "locked": // Not always included
								sms.setLocked(attribute.getValue());
								break;
							case "date_sent": // Not always included
								sms.setDate_Sent(attribute.getValue());
								break;
							case "readable_date": // Optional
								sms.setReadable_Date(attribute.getValue());
								break;
							case "contact_name": // Optional
								sms.setContact_Name(attribute.getValue());
								break;
							}
						}
						smsList.add(sms);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find file being passed in!");
			System.err.println("ERROR MESSAGE: " + e.getMessage());
			e.printStackTrace();
		} catch (XMLStreamException e) {
			System.err.println("ERROR: Could not read from XML input stream!");
			System.err.println("ERROR MESSAGE: " + e.getMessage());
			e.printStackTrace();
		}

		return smsList;
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
			line = reader.readLine(); // Skip SMS Count

			while (!(line = reader.readLine()).contains("</smses>")) {
				fileContent.add(line);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find file being passed in!");
			System.err.println("ERROR MESSAGE: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: Could not read from file input stream!");
			System.err.println("ERROR MESSAGE: " + e.getMessage());
			e.printStackTrace();
		}

		return fileContent;
	}
}
