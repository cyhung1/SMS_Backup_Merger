package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


// TODO need to update number of sms in file every time merge is run
// TODO rename analyze to info

public class FrontEnd {

	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final boolean DEBUG = true;
	private static final ReadWrite READNWRITE = new ReadWrite();
	private JFrame frame = new JFrame();
	private JButton selectFile, compare, merge;
	private File[] files;

	public FrontEnd() {
		createGUI();
		addListeners();
		init();
	}

	private void createGUI() {
		JPanel main = new JPanel(new GridLayout(3, 1));

		selectFile = new JButton("Select File(s)");
		compare = new JButton("Compare");
		merge = new JButton("Merge");

		main.add(selectFile);
		main.add(compare);
		main.add(merge);

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
					compare.setEnabled(true);
					merge.setEnabled(true);
				}
			}
		});

		compare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compareFiles(files);
			}
		});

		merge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File saveFile = null;

				if (CHOOSER.showSaveDialog(merge) == JFileChooser.APPROVE_OPTION) {
					if (CHOOSER.getSelectedFile() != null) {
						saveFile = CHOOSER.getSelectedFile();
					}
					if (DEBUG) System.out.println("DEBUG: Save to : " + saveFile.toPath());
					if (DEBUG) System.out.println("DEBUG: Does save file exist?: " + saveFile.exists());
				}

				mergeFiles(files, saveFile);
			}
		});

	}

	private void init() {
		FileNameExtensionFilter XMLFilter = new FileNameExtensionFilter("XML Files", "xml");
		CHOOSER.setFileFilter(XMLFilter); // Only XML files
		CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only files
		CHOOSER.setCurrentDirectory(new File(System.getProperty("user.home")));
		CHOOSER.setMultiSelectionEnabled(true); // Allow multiple file selection

		compare.setEnabled(false);
		merge.setEnabled(false);

		frame.setTitle(System.getProperty("user.name") + " :: " + getClass().getName().substring(0, 6));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center of screen
		frame.pack();
		frame.setVisible(true);
	}

	private void compareFiles(File[] listOfFiles) {
		READNWRITE.compare(listOfFiles);
	}

	private void mergeFiles(File[] listOfFiles, File fileToSaveTo) {
		READNWRITE.mergeToArchive(listOfFiles, fileToSaveTo);
	}

	public static void main(String[] args) {
		new FrontEnd();
	}
}
