package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrontEnd {

	private static final JFrame FRAME = new JFrame();
	private static final ReadWrite READ_WRITE = new ReadWrite();
	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final boolean DEBUG = true;
	private JButton selectFile, info, merge, extract, create;
	private File[] files;

	public FrontEnd() {
		createGUI();
		addListeners();
		init();
	}

	private void createGUI() {
		JPanel main = new JPanel(new GridLayout(5, 1));

		selectFile = new JButton("Select File(s)");
		info = new JButton("Info");
		merge = new JButton("Merge");
		extract = new JButton("Extract #");
		create = new JButton("Create");

		main.add(selectFile);
		main.add(info);
		main.add(merge);
		main.add(extract);
		main.add(create);

		FRAME.add(main);
	}

	private void addListeners() {

		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CHOOSER.showOpenDialog(selectFile) == JFileChooser.APPROVE_OPTION) {
					files = CHOOSER.getSelectedFiles();

					if (DEBUG) System.out.println("DEBUG: You selected: " + files.length + " files --> "
							+ Arrays.toString(files));

					info.setEnabled(true);
					merge.setEnabled(true);
					extract.setEnabled(true);
					create.setEnabled(true);
				}
			}
		});

		info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				infoFiles(files);
			}
		});

		merge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File saveFile = null;

				if (CHOOSER.showSaveDialog(merge) == JFileChooser.APPROVE_OPTION) {
					if (CHOOSER.getSelectedFile() != null) saveFile = CHOOSER.getSelectedFile();

					if (DEBUG) System.out.println("DEBUG: Save to : " + saveFile.toPath());
					if (DEBUG) System.out.println("DEBUG: Does save file exist?: " + saveFile.exists());

					mergeFiles(files, saveFile, saveFile.getName());
				}

			}
		});

		extract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog(extract, "Please enter a phone number to extract:",
						"1234567890");
				if (!input.matches("\\d{10}")) return;

				File saveFile = null;

				if (CHOOSER.showSaveDialog(extract) == JFileChooser.APPROVE_OPTION) {
					if (CHOOSER.getSelectedFile() != null) saveFile = CHOOSER.getSelectedFile();

					if (DEBUG) System.out.println("DEBUG: Save to : " + saveFile.toPath());
					if (DEBUG) System.out.println("DEBUG: Does save file exist?: " + saveFile.exists());

					extractNumber(files, input, saveFile.getName());
				}

			}
		});

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int inInt;
				String inString;
				String address = "";
				String date = "";
				String type = "";
				String body = "";
				String read = "";

				// Type
				inInt = JOptionPane.showConfirmDialog(create, "Is this an outgoing message?", "Message type",
						JOptionPane.YES_NO_OPTION);
				if (inInt == -1) return;
				type = (inInt == 0) ? "2" : "1";

				if (inInt == 1) {
					// Read
					inInt = JOptionPane.showConfirmDialog(create, "Is this message read?", "Message read",
							JOptionPane.YES_NO_OPTION);
					if (inInt == -1) return;
					read = (inInt == 0) ? "1" : "0";
				}

				// Address
				do {
					inString = JOptionPane.showInputDialog(create, "Enter # to or from:", "1234567890");
					if (inString == null) return;
				} while (!inString.matches("\\d{10}"));
				address = inString;

				// Body
				do {
					inString = JOptionPane.showInputDialog(create, "Message content:", "");
					if (inString == null) return;
				} while (inString.length() == 0);
				body = inString;

				// Date
				// Get month
				String epochDate = "";
				String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
						"September", "October", "November", "December" };
				inString = (String) JOptionPane.showInputDialog(create, "What month?", "Choose month",
						JOptionPane.QUESTION_MESSAGE, null, months, months[0]);
				if (inString == null) return;
				epochDate += inString + " ";

				// Get day
				String[] days = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
						"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
						"31" };
				inString = (String) JOptionPane.showInputDialog(create, "On what day?", "Choose day",
						JOptionPane.QUESTION_MESSAGE, null, days, days[0]);
				if (inString == null) return;
				epochDate += inString + " ";

				// Get year
				String[] years = { "1970", "1970", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979",
						"1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991",
						"1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003",
						"2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013" };
				inString = (String) JOptionPane.showInputDialog(create, "Of what year?", "Choose year",
						JOptionPane.QUESTION_MESSAGE, null, years, years[years.length - 1]);
				if (inString == null) return;
				epochDate += inString + " ";

				// Get hours
				String[] hours = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
						"14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
				inString = (String) JOptionPane.showInputDialog(create, "During what hour?", "Choose hour",
						JOptionPane.QUESTION_MESSAGE, null, hours, hours[0]);
				if (inString == null) return;
				epochDate += inString + " ";

				// Get minutes
				String[] minutes = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
						"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
						"29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
						"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
				inString = (String) JOptionPane.showInputDialog(create, "Within what minute?", "Choose minute",
						JOptionPane.QUESTION_MESSAGE, null, minutes, minutes[0]);
				if (inString == null) return;
				epochDate += inString + " ";

				// Get seconds
				String[] seconds = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
						"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
						"29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
						"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
				inString = (String) JOptionPane.showInputDialog(create, "At what second?", "Choose second",
						JOptionPane.QUESTION_MESSAGE, null, seconds, seconds[0]);
				if (inString == null) return;
				epochDate += inString;

				System.out.println(epochDate);
				long epoch = 0;
				try {
					epoch = new java.text.SimpleDateFormat("MMM dd yyyy HH mm ss").parse(epochDate).getTime();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				date = String.valueOf(epoch);

				if (DEBUG) System.out.println("DEBUG: address: " + address + " date: " + date + " type: " + type
						+ " body: " + body + " read: " + read);
				createMessage(address, date, type, body, read);
				//address – phone number of the sender/recipient
				//date – Java date representation of the time when the message was sent/received
				//type – 1 = Received, 2 = Sent, 3 = Draft, 4 = Outbox, 5 = Failed, 6 = Queued 
				//body – content of the message
				//read – Read Message = 1, Unread Message = 0
			}
		});
	}

	private void init() {
		FileNameExtensionFilter XMLFilter = new FileNameExtensionFilter("XML Files", "xml");
		CHOOSER.setFileFilter(XMLFilter); // Only XML files
		CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only files
		CHOOSER.setCurrentDirectory(new File(System.getProperty("user.home")));
		CHOOSER.setMultiSelectionEnabled(true); // Allow multiple file selection

		info.setEnabled(false);
		merge.setEnabled(false);
		extract.setEnabled(false);
		//create.setEnabled(false);

		FRAME.setTitle(System.getProperty("user.name") + " :: " + getClass().getName().substring(0, 6));
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setLocationRelativeTo(null); // Center of screen
		FRAME.pack();
		FRAME.setVisible(true);
	}

	private void infoFiles(File[] listOfFiles) {
		READ_WRITE.info(listOfFiles);
	}

	private void mergeFiles(File[] listOfFiles, File fileToSaveTo, String fileName) {
		READ_WRITE.mergeToArchive(listOfFiles, fileToSaveTo, fileName);
	}

	private void extractNumber(File[] listOfFiles, String number, String fileName) {
		READ_WRITE.extract(listOfFiles, number, fileName);
	}

	private void createMessage(String address, String date, String type, String body, String read) {
		READ_WRITE.createMessage(address, date, type, body, read);
	}

	public static void main(String[] args) {
		new FrontEnd();
	}
}
