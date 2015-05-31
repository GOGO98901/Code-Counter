package net.gogo98901.codeCounter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.gogo98901.Bootstrap;
import net.gogo98901.util.log.Log;
import java.awt.ScrollPane;
import javax.swing.JTextPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Display extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField searchField;
	private JButton btnBrowse, btnSearch;
	private JTextPane result;

	public Display() {
		Log.info("Started " + Bootstrap.getTitle() + " by " + Bootstrap.getAuthor());
		try {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(600, 600);
			setResizable(false);
			setLocationRelativeTo(null);

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
	}

	private void showSearchWindow() {

	}

	private void search() {

	}
}
