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


public class Read {

	private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();

	/**
	 * Gather and compare contents of xml sms files
	 * 
	 * @param listOfFiles
	 *            list of xml files to be compared
	 */
	public void compare(File[] listOfFiles) {
		if (!(listOfFiles.length > 0)) return; // Files must exist

		for (File f : listOfFiles) { // Files must not be null
			if (f == null) return;
		}

		//TODO info gathered might need to be displayed in a JDialog instead of system console
		//TESTING CODE
		ArrayList<SMS> list = new ArrayList<SMS>();
		for (File f : listOfFiles) {
			list = gatherSMSFromFile(f);
		}

		for (SMS sms : list) {
			System.out.println(sms.toFileString());
		}
		//ENDTESTINGCODE

	}

	/**
	 * Helper Method - Take xml file and parse all the sms nodes in it into a
	 * sms object and store them in an arraylist
	 * 
	 * @param xmlFile
	 *            passed in xml file
	 * @return list of sms nodes
	 */
	private ArrayList<SMS> gatherSMSFromFile(File xmlFile) {
		ArrayList<SMS> smsList = new ArrayList<SMS>();
		try {
			XMLEventReader eventReader = XMLINFACTORY.createXMLEventReader(new FileInputStream(xmlFile));

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == "sms") { // Get sms node
						SMS sms = new SMS();
						sms = createSMS(startElement.getAttributes());
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
	 * Helper method - Creates a new sms with given content attributes
	 * 
	 * @return new sms node
	 */
	private SMS createSMS(Iterator<?> attributes) {
		SMS sms = new SMS();

		while (attributes.hasNext()) {
			Attribute attribute = (Attribute) attributes.next();
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

		return sms;
	}

}
