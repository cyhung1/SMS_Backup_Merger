package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrontEnd {

	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final Analyze ANALYZE = new Analyze();
	private JFrame frame = new JFrame();
	private JButton selectFirstFile, selectSecondFile;
	private File file1, file2;
	private JButton analyze, compare;
	private JButton merge;
	private JButton about;

	public FrontEnd() {
		createGUI();
		addListeners();
		init();
	}

	private void createGUI() {
		JPanel main = new JPanel(new GridLayout(6, 1));

		selectFirstFile = new JButton("File 1");
		selectSecondFile = new JButton("File 2");
		analyze = new JButton("Analyze");
		compare = new JButton("Compare");
		merge = new JButton("Merge");
		about = new JButton("About");

		main.add(selectFirstFile);
		main.add(selectSecondFile);
		main.add(analyze);
		main.add(compare);
		main.add(merge);
		main.add(about);

		frame.add(main);
	}

	private void addListeners() {
		ActionListener selectFileListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(selectFirstFile)) {
					if (CHOOSER.showOpenDialog(selectFirstFile) == JFileChooser.APPROVE_OPTION) {
						if (CHOOSER.getSelectedFile() == file2) {
							System.out.println("You selected the same file!");
						} else {
							file1 = CHOOSER.getSelectedFile();
						}

					}
				} else if (e.getSource().equals(selectSecondFile)) {
					if (CHOOSER.showOpenDialog(selectFirstFile) == JFileChooser.APPROVE_OPTION) {
						if (CHOOSER.getSelectedFile() == file1) {
							System.out.println("You selected the same file!");
						} else {
							file2 = CHOOSER.getSelectedFile();
						}
					}
				}
			}
		};
		selectFirstFile.addActionListener(selectFileListener);
		selectSecondFile.addActionListener(selectFileListener);

		analyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				analyzeFile(file1);
			}
		});

		compare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compareFile(file1, file2);
			}
		});

		merge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});

	}

	private void init() {
		FileNameExtensionFilter XMLFilter = new FileNameExtensionFilter("XML Files", "xml");
		CHOOSER.setFileFilter(XMLFilter); // Only XML files
		CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only files
		CHOOSER.setCurrentDirectory(new File(System.getProperty("user.home")));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center of screen
		frame.pack();
		frame.setVisible(true);
	}

	private void analyzeFile(File fileToAnalyze) {
		ANALYZE.gatherInfo(fileToAnalyze);
	}
	
	private void compareFile(File firstFile, File secondFile) {
		ANALYZE.compare(firstFile, secondFile);
	}

	public static void main(String[] args) {
		new FrontEnd();
	}
}
