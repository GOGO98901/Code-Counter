package net.gogo98901.codeCounter.Display;

import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.gogo98901.Bootstrap;
import net.gogo98901.codeCounter.Searcher;
import net.gogo98901.log.Log;
import net.gogo98901.util.Data;

public class Display extends JFrame {
	private static final long serialVersionUID = 1L;

	private Config config;

	private JPanel contentPane;
	private JTextField searchField;
	private JButton btnBrowse, btnSearch;
	private JTextPane result;

	private Searcher searcher;
	private JFileChooser fileChooser;
	private JTextField fieldLines, fieldWhiteSpace;
	private JTextField fieldFiles, fieldDirs;
	private JTextField fieldFilesExcluded, fieldDirsExcluded;

	private JButton btnConfig;

	public Display() {
		Log.info("Started " + Bootstrap.getTitle() + " by " + Bootstrap.getAuthor());
		try {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(600, 508);
			setResizable(false);
			setLocationRelativeTo(null);
			setIconImage(Toolkit.getDefaultToolkit().getImage(Display.class.getResource("/com/sun/java/swing/plaf/windows/icons/DetailsView.gif")));

			setTitle(Bootstrap.getTitle() + " " + Bootstrap.getVersion());

			config = new Config(this);
			searcher = new Searcher(config);
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			init();
		} catch (Exception e) {
			Log.severe("Display... FAILED");
			Log.stackTrace(e);
			return;
		}
		Log.info("Display... OK");
	}

	private void init() {
		Log.info("Display... Initialized");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		{
			searchField = new JTextField();
			searchField.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					if (event.getKeyCode() == KeyEvent.VK_ENTER) search();
				}
			});
			searchField.setBounds(10, 11, 376, 20);
			searchField.setText(new File(System.getProperty("user.home")).getAbsolutePath());
			contentPane.add(searchField);
			searchField.setColumns(10);

			btnSearch = new JButton("Scan Dir");
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					search();
				}
			});
			btnSearch.setBounds(495, 10, 89, 23);
			contentPane.add(btnSearch);
		}
		{
			btnBrowse = new JButton("Browse");
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showSearchWindow();
				}
			});
			btnBrowse.setBounds(396, 10, 89, 23);
			contentPane.add(btnBrowse);
		}
		{
			result = new JTextPane();
			result.setEditable(false);
			result.setBounds(0, 0, 6, 20);

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.add(result);
			scrollPane.setBounds(10, 37, 574, 374);
			contentPane.add(scrollPane);
		}
		{
			fieldLines = new JTextField("0");
			fieldLines.setBounds(10, 417, 86, 20);
			fieldLines.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldLines.setEditable(false);
			fieldLines.setToolTipText(Tooltip.lines);
			contentPane.add(fieldLines);

			Label labelLines = new Label("Lines");
			labelLines.setBounds(102, 415, 62, 22);
			contentPane.add(labelLines);
		}
		{
			fieldWhiteSpace = new JTextField("0");
			fieldWhiteSpace.setBounds(10, 448, 86, 20);
			fieldWhiteSpace.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldWhiteSpace.setEditable(false);
			fieldWhiteSpace.setToolTipText(Tooltip.whitespace);
			contentPane.add(fieldWhiteSpace);

			Label labelWhiteSpace = new Label("White Space");
			labelWhiteSpace.setBounds(102, 446, 62, 22);
			contentPane.add(labelWhiteSpace);
		}
		{
			fieldFiles = new JTextField("0");
			fieldFiles.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldFiles.setEditable(false);
			fieldFiles.setBounds(170, 417, 44, 20);
			fieldFiles.setToolTipText(Tooltip.files);
			contentPane.add(fieldFiles);

			Label lableFiles = new Label("Files");
			lableFiles.setBounds(220, 417, 62, 22);
			contentPane.add(lableFiles);
		}
		{
			fieldDirs = new JTextField("0");
			fieldDirs.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldDirs.setEditable(false);
			fieldDirs.setBounds(170, 448, 44, 20);
			fieldDirs.setToolTipText(Tooltip.dirs);
			contentPane.add(fieldDirs);

			Label lableDirs = new Label("Directories");
			lableDirs.setBounds(220, 448, 62, 22);
			contentPane.add(lableDirs);
		}
		{
			fieldFilesExcluded = new JTextField("0");
			fieldFilesExcluded.setBounds(288, 417, 44, 20);
			fieldFilesExcluded.setEditable(false);
			fieldFilesExcluded.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldFilesExcluded.setToolTipText(Tooltip.filesExcluded);
			contentPane.add(fieldFilesExcluded);

			Label labelFilesExclude = new Label("Files Excluded");
			labelFilesExclude.setBounds(338, 415, 147, 22);
			contentPane.add(labelFilesExclude);
		}
		{
			fieldDirsExcluded = new JTextField("0");
			fieldDirsExcluded.setBounds(288, 448, 44, 20);
			fieldDirsExcluded.setEditable(false);
			fieldDirsExcluded.setHorizontalAlignment(SwingConstants.RIGHT);
			fieldDirsExcluded.setToolTipText(Tooltip.dirsExcluded);
			contentPane.add(fieldDirsExcluded);

			Label labelDirExcluded = new Label("Directories Excluded");
			labelDirExcluded.setBounds(338, 446, 147, 22);
			contentPane.add(labelDirExcluded);
		}
		{
			btnConfig = new JButton("Config");
			btnConfig.setBounds(495, 447, 89, 23);
			btnConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					config.show();
				}
			});
			contentPane.add(btnConfig);
		}
	}

	private void showSearchWindow() {
		Log.info("Opeining search window");
		if (searchField.getText().length() == 0) fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		else {
			if (Data.pathExists(searchField.getText())) fileChooser.setCurrentDirectory(new File(searchField.getText()));
			else fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		}
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			searchField.setText(fileChooser.getSelectedFile().getAbsolutePath().replace("\\", "/"));
			searchField.revalidate();
			searchField.repaint();
			search();
		}
	}

	private void search() {
		Log.info("Searching...");
		searcher.setPath(searchField.getText());
		searcher.search();
		result.setText("");
		for (String path : searcher.getPaths()) {
			apened(path.replace("\\", "/").replace(searchField.getText(), "") + "\n");
		}
		fieldLines.setText(searcher.getLines() + "");
		fieldWhiteSpace.setText(searcher.getWhiteSpace() + "");
		fieldFiles.setText(searcher.getFiles() + "");
		fieldDirs.setText(searcher.getDirs() + "");
		fieldFilesExcluded.setText(searcher.getFilesExcluded() + "");
		fieldDirsExcluded.setText(searcher.getDirsExcluded() + "");
	}

	private void apened(String line) {
		result.setText(result.getText() + line);
	}
}
