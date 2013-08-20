package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrontEnd {

	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final boolean DEBUG = true;
	private static final ReadWrite READ_WRITE = new ReadWrite();
	private static final JFrame FRAME = new JFrame();
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
				JDialog dialog = new JDialog(FRAME);
				dialog.setLayout(new GridLayout(5, 1));

				JTextField address = new JTextField("address");
				JTextField date = new JTextField("date");
				JTextField type = new JTextField("type");
				JTextField body = new JTextField("body");
				JTextField read = new JTextField("read");

				dialog.add(address);
				dialog.add(date);
				dialog.add(type);
				dialog.add(body);
				dialog.add(read);

				//createMessage();
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
		create.setEnabled(false);

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
