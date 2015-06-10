package net.gogo98901.codeCounter;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

	private List<String[]> files = new ArrayList<String[]>();
	private List<String[]> dirs = new ArrayList<String[]>();

	private String file = "Code Counter.cfg";

	private String[] stock = new String[] { ".png", ".jpg", ".psd", ".db", ".ttf", ".dll", ".class", File.separator + ".git" };

	public Config(JFrame display) {
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
		model = new DefaultTableModel(data, columnNames);
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
				System.out.println("Row   : " + tcl.getRow());
				System.out.println("Column: " + tcl.getColumn());
				System.out.println("Old   : " + tcl.getOldValue());
				System.out.println("New   : " + tcl.getNewValue());
			}
		});

		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);
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
				for (int i = model.getRowCount() - 1; i > 0; i--) {
					model.removeRow(i);
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
}
