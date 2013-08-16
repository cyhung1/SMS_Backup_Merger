package merger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class ReadWrite {
	private static final boolean DEBUG = true;
	private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();

	/**
	 * --------------------------------- Read ---------------------------------
	 */

	/**
	 * Gather and compare contents of xml sms files
	 * 
	 * @param listOfFiles
	 *            list of xml files to be compared
	 */
	public void info(File[] listOfFiles) {
		int totalNumChars = 0;
		int totalSentChars = 0;
		int totalRecievedChars = 0;
		int totalSent = 0;
		int totalRecieved = 0;
		int totalWords = 0;
		ArrayList<String> contactList = new ArrayList<String>();

		if (!(listOfFiles.length > 0)) return; // Files must exist

		for (File f : listOfFiles) { // Files must not be null
			if (f == null) return;
		}

		//TESTING CODE
		ArrayList<SMS> list = new ArrayList<SMS>();
		for (File f : listOfFiles) {
			list = gatherSMSFromFile(f);

			for (SMS s : list) {
				if (!contactList.contains(s.getAddress())) contactList.add(s.getAddress());

				if (s.getType().equals("1")) {
					totalRecieved++;
					totalRecievedChars += s.getBody().length();
				}
				if (s.getType().equals("2")) {
					totalSent++;
					totalSentChars += s.getBody().length();
				}
				totalNumChars += s.getBody().length();
				totalWords += s.getBody().replaceAll("[^ ]", "").length() + 1;
			}
		}

		System.out.println("INFO: " + (totalSent + totalRecieved) + " : total messages");
		System.out.println("INFO: " + totalSent + " : messages sent");
		System.out.println("INFO: " + totalRecieved + " : messages recieved");
		System.out.println("INFO: " + totalWords + " : total words");
		System.out.println("INFO: " + totalNumChars + " : total letters");
		System.out.println("INFO: " + totalSentChars + " : total letters sent");
		System.out.println("INFO: " + totalRecievedChars + " : total letters recieved");
		System.out.println("INFO: " + contactList.size() + " : unique contacts");

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

	private TreeSet<SMS> sortSMS(ArrayList<SMS> unsortedList) {
		TreeSet<SMS> sortedList = new TreeSet<>(new Comparator<SMS>() {
			@Override
			public int compare(SMS first, SMS second) {
				int ret = first.getDate().compareTo(second.getDate());
				if (ret == 0) ret = first.getBody().compareTo(second.getBody());
				return ret;
			}
		});

		for (SMS s : unsortedList) {
			sortedList.add(s);
		}

		return sortedList;
	}

	/**
	 * --------------------------------- Write ---------------------------------
	 */

	public void mergeToArchive(File[] listOfFiles, File fileToSaveTo, String fileName) {
		ArrayList<SMS> fullList = new ArrayList<SMS>();
		TreeSet<SMS> fullSortedList = new TreeSet<SMS>();

		for (File f : listOfFiles) {
			fullList.addAll(gatherSMSFromFile(f));
		}

		fullSortedList = sortSMS(fullList);

		writeToFile(fullSortedList, fileName);
	}

	public void extract(File[] listOfFiles, String number, String fileName) {
		ArrayList<SMS> fullList = new ArrayList<SMS>();
		ArrayList<SMS> chosenList = new ArrayList<SMS>();
		TreeSet<SMS> fullSortedList = new TreeSet<SMS>();

		for (File f : listOfFiles) {
			fullList.addAll(gatherSMSFromFile(f));
		}

		// Gather selected numbers
		for (SMS s : fullList) {
			if (s.getAddress().equals(number)) chosenList.add(s);
		}

		fullSortedList = sortSMS(chosenList);

		writeToFile(fullSortedList, fileName);
	}

	private void writeToFile(TreeSet<SMS> list, String fileName) {
		try {

			if (!fileName.endsWith(".xml")) fileName += ".xml";

			File archive = new File(System.getProperty("user.home") + File.separator + fileName);

			if (!archive.exists()) archive.createNewFile();

			BufferedWriter writer = new BufferedWriter(new FileWriter(archive));

			writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n");
			writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"sms.xsl\"?>\n");
			writer.write("<smses count=\"" + list.size() + "\">\n");

			Iterator<SMS> iterator = list.iterator();
			while (iterator.hasNext()) {
				writer.write(iterator.next().toFileString());
				writer.newLine();
			}

			writer.write("</smses>");

			writer.close();

			if (DEBUG) System.out.println("INFO: Files Merged!");

		} catch (IOException e) {
			System.err.println("ERROR: Problem creating new file!");
			System.err.println("ERROR MESSAGE: " + e.getMessage());
			e.printStackTrace();
		}
	}

	//protocol – its mostly 0 in case of SMS messages
	//address – phone number of the sender/recipient
	//date – Java date representation of the time when the message was sent/received
	//type – 1 = Received, 2 = Sent, 3 = Draft, 4 = Outbox, 5 = Failed, 6 = Queued 
	//subject – always null in case of SMS
	//body – content of the message
	//toa – n/a, default to null
	//sc_toa – n/a, default to null
	//service_center – service center for received message, null in case of sent messages
	//read – Read Message = 1, Unread Message = 0
	//status – None = -1, Complete = 0, Pending = 32, Failed = 64
	//readable_date – Optional field that has the date in a human readable format
	//contact_name – Optional field that has the name of the contact.

}
