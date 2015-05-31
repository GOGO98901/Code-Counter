package net.gogo98901.codeCounter;

import java.awt.ScrollPane;
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
import javax.swing.border.EmptyBorder;

import net.gogo98901.Bootstrap;
import net.gogo98901.util.Data;
import net.gogo98901.util.log.Log;
import java.awt.Label;
import javax.swing.SwingConstants;

public class Display extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField searchField;
	private JButton btnBrowse, btnSearch;
	private JTextPane result;

	private Searcher searcher;
	private JFileChooser fileChooser = new JFileChooser();
	private JTextField fieldLines;
	private JTextField fieldWhiteSpace;
	private JTextField fieldFiles;
	private JTextField fieldDirs;
	private Label labelDir;

	public Display() {
		Log.info("Started " + Bootstrap.getTitle() + " by " + Bootstrap.getAuthor());
		try {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(600, 508);
			setResizable(false);
			setLocationRelativeTo(null);

			searcher = new Searcher();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			init();
		} catch (Exception e) {
			Log.info("Display... FAILED");
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

		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSearchWindow();
			}
		});
		btnBrowse.setBounds(396, 10, 89, 23);
		contentPane.add(btnBrowse);

		btnSearch = new JButton("Scan Dir");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				search();
			}
		});
		btnSearch.setBounds(495, 10, 89, 23);
		contentPane.add(btnSearch);

		result = new JTextPane();
		result.setEditable(false);
		result.setBounds(0, 0, 6, 20);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(result);
		scrollPane.setBounds(10, 37, 574, 374);
		contentPane.add(scrollPane);

		fieldLines = new JTextField("0");
		fieldLines.setBounds(10, 417, 86, 20);
		fieldLines.setHorizontalAlignment(SwingConstants.RIGHT);
		fieldLines.setEditable(false);
		contentPane.add(fieldLines);

		Label labelLines = new Label("Lines");
		labelLines.setBounds(102, 415, 62, 22);
		contentPane.add(labelLines);

		fieldWhiteSpace = new JTextField("0");
		fieldWhiteSpace.setBounds(10, 448, 86, 20);
		fieldWhiteSpace.setHorizontalAlignment(SwingConstants.RIGHT);
		fieldWhiteSpace.setEditable(false);
		contentPane.add(fieldWhiteSpace);

		Label labelWhiteSpace = new Label("White Space");
		labelWhiteSpace.setBounds(102, 446, 62, 22);
		contentPane.add(labelWhiteSpace);
		
		fieldFiles = new JTextField("0");
		fieldFiles.setBounds(190, 417, 86, 20);
		fieldFiles.setEditable(false);
		fieldFiles.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(fieldFiles);
		
		Label labelFiles = new Label("Files");
		labelFiles.setBounds(282, 417, 62, 22);
		contentPane.add(labelFiles);
		
		fieldDirs = new JTextField("0");
		fieldDirs.setBounds(190, 448, 86, 20);
		fieldDirs.setEditable(false);
		fieldDirs.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(fieldDirs);
		
		labelDir = new Label("Directories");
		labelDir.setBounds(282, 446, 62, 22);
		contentPane.add(labelDir);
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
		for (String path : searcher.getPaths()) {
			apened(path.replace("\\", "/").replace(searchField.getText(), "") + "\n");
		}
		fieldLines.setText(searcher.getLines() + "");
		fieldWhiteSpace.setText(searcher.getWhiteSpace() + "");
		fieldFiles.setText(searcher.getFiles() + "");
		fieldDirs.setText(searcher.getDirs() + "");
	}

	private void apened(String line) {
		result.setText(result.getText() + line);
	}
}
