package merger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class Write {

	private static final XMLOutputFactory XMLOUTFACTORY = XMLOutputFactory.newInstance();

	public void merge(ArrayList<String> first, ArrayList<String> second) {

	}

	public void mergeToArchive(ArrayList<String> first, ArrayList<String> second) {

		SortedSet<String> archive = new TreeSet<String>(new Comparator<String>() {
			@Override
			public int compare(String a, String b) {

				int aStart = a.indexOf(" date=") + 7;
				int aEnd = a.indexOf(" date=") + 7 + 13;
				int bStart = b.indexOf(" date=") + 7;
				int bEnd = b.indexOf(" date=") + 7 + 13;
				System.out.println(a);
				if (a.contains("1354037472309")) {
					System.out.println("debug");
				}
				
				String aS = a.substring(aStart, aEnd);
				System.out.println(aS);
				String bS = b.substring(bStart, bEnd);
				System.out.println(bS);
				Long aDate = new Long(aS);
				Long bDate = new Long(bS);

				return aDate.compareTo(bDate);
			}
		});

		for (String s : first) {
			archive.add(s);
		}

		for (String s : second) {
			archive.add(s);
		}

		System.out.println("Archive size: " + archive.size());
		createFile(archive);

	}

	public void create(String fileName) {
		try {
			//XMLEventWriter eventWriter = XMLOUTFACTORY.createXMLEventWriter(new FileOutputStream(fileName));
			StringWriter stringWriter = new StringWriter();
			XMLEventWriter eventWriter = XMLOUTFACTORY.createXMLEventWriter(stringWriter);
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();

			// A few standard events
			XMLEvent newLine = eventFactory.createDTD("\n");
			//			XMLEvent tab = eventFactory.createDTD("\t");
			StartDocument startDoc = eventFactory.createStartDocument("UTF-8", "1.0", true);
			EndDocument endDoc = eventFactory.createEndDocument();

			// Create start tag
			eventWriter.add(startDoc);
			eventWriter.add(newLine);

			// Create config? open tag
			StartElement SMSStartElement = eventFactory.createStartElement("", "", "sms");
			eventWriter.add(SMSStartElement);
			eventWriter.add(newLine);

			// Add items
			//TODO

			EndElement SMSEndElement = eventFactory.createEndElement("", "", "sms");

			eventWriter.add(SMSEndElement);
			eventWriter.add(newLine);

			eventWriter.add(endDoc);

			System.out.println(stringWriter);
			System.out.println(eventWriter.toString());

			eventWriter.close();

		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createFile(SortedSet<String> content) {
		try {

			File test = new File(System.getProperty("user.home") + File.separator + "test.xml");

			if (!test.exists()) test.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(test));

			Iterator<String> iterator = content.iterator();
			while (iterator.hasNext()) {
				writer.write(iterator.next());
				writer.newLine();
			}

			writer.close();

			System.out.println("done merging");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * protocol – Protocol used by the message, its mostly 0 in case of SMS
	 * messages. address – The phone number of the sender/recipient. date – The
	 * Java date representation of the time when the message was sent/received
	 * type – 1 = Received, 2 = Sent, 3 = Draft, 4 = Outbox, 5 = Failed, 6 =
	 * Queued subject – Subject of the message, its always null in case of SMS
	 * messages. body – The content of the message. toa – n/a, default to null.
	 * sc_toa – n/a, default to null. service_center – The service center for
	 * the received message, null in case of sent messages. read – Read Message
	 * = 1, Unread Message = 0. status – None = -1, Complete = 0, Pending = 32,
	 * Failed = 64. readable_date – Optional field that has the date in a human
	 * readable format. contact_name – Optional field that has the name of the
	 * contact.
	 */
}
