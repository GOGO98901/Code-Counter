package net.gogo98901.codeCounter.Display.config;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.gogo98901.Bootstrap;
import net.gogo98901.log.Log;
import net.gogo98901.reader.StaticReader;
import net.gogo98901.swing.TableCellListener;
import net.gogo98901.util.Data;

public class Config {
	private JFrame display;
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnAdd, btnRemove;

	private List<String[]> files = new ArrayList<String[]>();
	private List<String[]> dirs = new ArrayList<String[]>();

	private String file = "Code Counter.cfg";

	private String[] stock = new String[] { ".png", ".jpg", ".psd", ".db", ".ttf", ".dll", ".class", File.separator + ".git" };

	public Config (JFrame display) {
		this.display = display;
		try {
			initFrame();
			Log.info("Config... OK");
		} catch (Exception e) {
			Log.warn("Config... FAILED");
			Log.stackTrace(e);
		}
		for (String s : stock) {
			if (s.startsWith(".")) files.add(new String[] { s, "true" });
			if (s.startsWith(File.separator)) dirs.add(new String[] { s, "true" });
		}

		load();
	}

	private void initFrame() {
		frame = new JFrame();
		frame.setTitle(display.getTitle() + " - Config");
		frame.setIconImage(display.getIconImage());
		frame.setAlwaysOnTop(false);
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(display);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				hide();
			}
		});

		Object[] columnNames = { "Type", "Name", "Exclude" };
		Object[][] data = { { "test", "test", false } };
		model = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				if (column == 2) return true;
				return false;
			}
		};
		model.removeRow(0);
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};
		new TableCellListener(table, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				TableCellListener tcl = (TableCellListener) e.getSource();
				String type = model.getValueAt(tcl.getRow(), 0).toString();
				String name = model.getValueAt(tcl.getRow(), 1).toString();
				String excluded = model.getValueAt(tcl.getRow(), 2).toString();
				if (type.equals("file")) for (String[] file : files) {
					if (file[0].equals(name)) files.set(files.indexOf(file), new String[] { name, excluded });
				}
				if (type.equals("dir")) for (String[] dir : dirs) {
					if (dir[0].equals(name)) dirs.set(dirs.indexOf(dir), new String[] { name, excluded });
				}
			}
		});
		table.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) remove(table.getSelectedRow());
			}

			public void keyPressed(KeyEvent e) {}
		});
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane, BorderLayout.CENTER);

		JPanel bottom = new JPanel();

		btnAdd = new JButton();
		btnAdd.setText("Add filter");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel dialog = new JPanel();
				dialog.add(new JLabel("Type"));
				JComboBox<String> type = new JComboBox<String>();
				type.addItem("file");
				type.addItem("dir");
				dialog.add(type);
				dialog.add(new JLabel("Name"));
				JTextField name = new JTextField(10);
				dialog.add(name);
				int result = JOptionPane.showConfirmDialog(null, dialog, "Please select type and enter name", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					String enteredName = name.getText();
					if (enteredName.length() > 0) {
						String enteredType = null;
						if (type.getSelectedIndex() == 0) {
							enteredType = "file";
							if (!enteredName.startsWith(".")) enteredName = "." + enteredName;
							files.add(new String[] { enteredName, "true" });
							Log.info("Filter added (file)");
						}
						if (type.getSelectedIndex() == 1) {
							enteredType = "dir";
							if (!enteredName.startsWith(File.separator)) enteredName = File.separator + enteredName;
							dirs.add(new String[] { enteredName, "true" });
							Log.info("Filter added (dir)");
						}
						if (enteredType != null) model.addRow(new Object[] { enteredType, enteredName, true });
					}
				}
			}
		});
		bottom.add(btnAdd, BorderLayout.WEST);
		btnRemove = new JButton();
		btnRemove.setText("Remove Filter");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) remove(table.getSelectedRow());
				else {
					JPanel dialog = new JPanel();
					dialog.add(new JLabel("Filter"));
					JComboBox<String> items = new JComboBox<String>();
					for (int i = 0; i < table.getRowCount(); i++) {
						String type = model.getValueAt(i, 0).toString();
						String name = model.getValueAt(i, 1).toString();
						items.addItem("(" + type + ")  " + name);
					}
					dialog.add(items);

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please select the item to remove", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) remove(items.getSelectedIndex());
				}
			}
		});
		bottom.add(btnRemove, BorderLayout.EAST);
		frame.add(bottom, BorderLayout.SOUTH);
	}

	public void show() {
		load();
		frame.setLocationRelativeTo(display);
		frame.setVisible(true);
	}

	public void hide() {
		save();
		frame.setVisible(false);
	}

	public boolean isVisible() {
		return frame.isVisible();
	}

	public void load() {
		if (Data.fileExists(file)) {
			String[] content = StaticReader.read(file, false).split(System.getProperty("line.separator"));
			if (content != null && content.length > 0) {
				files.clear();
				dirs.clear();
				while (model.getRowCount() > 0) {
					model.removeRow(0);
				}
				for (String line : content) {
					line = line.toLowerCase();
					if (!line.startsWith("//")) try {
						String[] data = line.split(" ");
						if (data[0].endsWith("file")) {
							if (!data[1].startsWith(".")) data[1] = "." + data[1];
							if (!files.contains(new String[] { data[1], data[2] })) {
								files.add(new String[] { data[1], data[2] });
								model.addRow(new Object[] { "file", data[1], Boolean.parseBoolean(data[2]) });
							}
						}

						if (data[0].endsWith("dir")) {
							if (!data[1].startsWith(File.separator)) data[1] = File.separator + data[1];
							if (!dirs.contains(new String[] { data[1], data[2] })) {
								dirs.add(new String[] { data[1], data[2] });
								model.addRow(new Object[] { "dir", data[1], Boolean.parseBoolean(data[2]) });
							}
						}
					} catch (Exception e) {
						Log.warn("Could not phase line");
						Log.stackTrace(e);
					}
				}
			} else Log.warn("Could not find config file");
		} else save();
	}

	public void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("// " + Bootstrap.getTitle() + " " + Bootstrap.getVersion() + " by " + Bootstrap.getAuthor() + System.getProperty("line.separator") + System.getProperty("line.separator"));
			bw.write("// content must follow this format 'tpye name exclude'" + System.getProperty("line.separator"));
			bw.write("// 'file' tells the program that it is a file" + System.getProperty("line.separator"));
			bw.write("// 'dir' tells the program that it is a directory" + System.getProperty("line.separator"));
			bw.write("// 'true' means that the type will be excluded" + System.getProperty("line.separator") + System.getProperty("line.separator"));
			for (String[] file : files) {
				bw.write("file " + file[0] + " " + file[1] + System.getProperty("line.separator"));
			}
			bw.write(System.getProperty("line.separator"));
			for (String dir[] : dirs) {
				bw.write("dir " + dir[0] + " " + dir[1] + System.getProperty("line.separator"));
			}
			bw.write(System.getProperty("line.separator"));
			bw.close();
			Log.info("Config save... OK");
		} catch (IOException e) {
			Log.info("Config save... FAILED");
			Log.stackTrace(e);
		}
	}

	public List<String> getExcludeFiles() {
		List<String> goodFiles = new ArrayList<String>();
		for (String[] file : files) {
			if (file[1].equals("true")) goodFiles.add(file[0]);
		}
		return goodFiles;
	}

	public List<String> getExcludeDirectories() {
		List<String> goodDirs = new ArrayList<String>();
		for (String[] dir : dirs) {
			if (dir[1].equals("true")) goodDirs.add(dir[0]);
		}
		return goodDirs;
	}

	private void remove(int row) {
		if (row != -1) {
			if (table.getValueAt(row, 0).equals("file")) for (String[] file : files) {
				if (file[0].equals(table.getValueAt(row, 1))) {
					files.remove(files.indexOf(file));
					Log.info("Filter removed (file)");
					break;
				}
			}
			else if (table.getValueAt(row, 0).equals("dir")) for (String[] dir : dirs) {
				if (dir[0].equals(table.getValueAt(row, 1))) {
					dirs.remove(dirs.indexOf(dir));
					Log.info("Filter removed (dir)");
					break;
				}
			}
			model.removeRow(row);
		}
	}
}
