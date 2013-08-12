package merger;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FrontEnd {

	private static final JFileChooser CHOOSER = new JFileChooser();
	private static final Read READ = new Read();
	private static final Write WRITE = new Write();
	private JFrame frame = new JFrame();
	private JButton selectFirstFile, selectSecondFile;
	private JButton analyze1, analyze2;
	private File file1, file2;
	private JButton compare, merge, about;
	private JCheckBox mergeTo1, mergeTo2, mergeToArchive;

	public FrontEnd() {
		createGUI();
		addListeners();
		init();
	}

	private void createGUI() {
		JPanel main = new JPanel(new GridLayout(8, 1));
		JPanel file1 = new JPanel(new GridLayout(1, 2));
		JPanel file2 = new JPanel(new GridLayout(1, 2));

		selectFirstFile = new JButton("File 1");
		selectSecondFile = new JButton("File 2");
		analyze1 = new JButton("Analyze");
		analyze2 = new JButton("Analyze");
		compare = new JButton("Compare");
		merge = new JButton("Merge");
		about = new JButton("About");
		mergeTo1 = new JCheckBox("Merge into File 1");
		mergeTo2 = new JCheckBox("Merge into File 2");
		mergeToArchive = new JCheckBox("Merge into \"The Archive\"");

		file1.add(selectFirstFile);
		file1.add(analyze1);
		file2.add(selectSecondFile);
		file2.add(analyze2);
		main.add(file1);
		main.add(file2);
		main.add(compare);
		main.add(merge);
		main.add(about);
		main.add(mergeTo1);
		main.add(mergeTo2);
		main.add(mergeToArchive);

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
							analyze1.setEnabled(true);
						}

					}
				} else if (e.getSource().equals(selectSecondFile)) {
					if (CHOOSER.showOpenDialog(selectFirstFile) == JFileChooser.APPROVE_OPTION) {
						if (CHOOSER.getSelectedFile() == file1) {
							System.out.println("You selected the same file!");
						} else {
							file2 = CHOOSER.getSelectedFile();
							analyze2.setEnabled(true);
						}
					}
				}

				if (analyze1.isEnabled() && analyze2.isEnabled()) {
					compare.setEnabled(true);
					merge.setEnabled(true);
				}

			}
		};
		selectFirstFile.addActionListener(selectFileListener);
		selectSecondFile.addActionListener(selectFileListener);

		ActionListener analyzeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(analyze1)) {
					analyzeFile(file1);
				} else if (e.getSource().equals(analyze2)) {
					analyzeFile(file2);
				}
			}
		};
		analyze1.addActionListener(analyzeListener);
		analyze2.addActionListener(analyzeListener);

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
				ArrayList<String> temp1 = READ.gatherFileContents(file1);
				ArrayList<String> temp2 = READ.gatherFileContents(file2);
				WRITE.mergeToArchive(temp1, temp2);;
				//WRITE.mergeToArchive(READ.gatherFileContents(file1), READ.gatherFileContents(file2));
			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Create dialog with info in it or get rid of this button
			}
		});

	}

	private void init() {
		FileNameExtensionFilter XMLFilter = new FileNameExtensionFilter("XML Files", "xml");
		CHOOSER.setFileFilter(XMLFilter); // Only XML files
		CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only files
		CHOOSER.setCurrentDirectory(new File(System.getProperty("user.home")));

		analyze1.setEnabled(false);
		analyze2.setEnabled(false);
		compare.setEnabled(false);
		//merge.setEnabled(false);

		mergeTo1.setEnabled(false);
		mergeTo2.setEnabled(false);
		mergeToArchive.setEnabled(false);
		mergeToArchive.setSelected(true);
		

		frame.setTitle(System.getProperty("user.name") + " :: " + getClass().getName().substring(0, 6));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center of screen
		frame.pack();
		frame.setVisible(true);
	}

	private void analyzeFile(File fileToAnalyze) {
		READ.gatherInfo(fileToAnalyze);
	}

	private void compareFile(File firstFile, File secondFile) {
		READ.compare(firstFile, secondFile);
	}

	public static void main(String[] args) {
		new FrontEnd();
	}
}
