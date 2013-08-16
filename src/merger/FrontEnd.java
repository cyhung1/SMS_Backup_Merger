package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrontEnd {

	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final boolean DEBUG = true;
	private static final ReadWrite READNWRITE = new ReadWrite();
	private JFrame frame = new JFrame();
	private JButton selectFile, info, merge, extract;
	private File[] files;

	public FrontEnd() {
		createGUI();
		addListeners();
		init();
	}

	private void createGUI() {
		JPanel main = new JPanel(new GridLayout(4, 1));

		selectFile = new JButton("Select File(s)");
		info = new JButton("Info");
		merge = new JButton("Merge");
		extract = new JButton("Extract #");

		main.add(selectFile);
		main.add(info);
		main.add(merge);
		main.add(extract);

		frame.add(main);
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

		frame.setTitle(System.getProperty("user.name") + " :: " + getClass().getName().substring(0, 6));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center of screen
		frame.pack();
		frame.setVisible(true);
	}

	private void infoFiles(File[] listOfFiles) {
		READNWRITE.info(listOfFiles);
	}

	private void mergeFiles(File[] listOfFiles, File fileToSaveTo, String fileName) {
		READNWRITE.mergeToArchive(listOfFiles, fileToSaveTo, fileName);
	}

	private void extractNumber(File[] listOfFiles, String number, String fileName) {
		READNWRITE.extract(listOfFiles, number, fileName);
	}

	public static void main(String[] args) {
		new FrontEnd();
	}
}
