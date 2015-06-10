package net.gogo98901.codeCounter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.gogo98901.log.Log;
import net.gogo98901.reader.StaticReader;

public class Config {
	private JFrame frame;
	private List<String> files = new ArrayList<String>();
	private List<String> dirs = new ArrayList<String>();

	private String file = "CodeCounter/options.cfg";

	public Config(JFrame display) {
		init(display);
	}

	private void init(JFrame display) {
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
	}

	public void show() {
		frame.setVisible(true);
	}

	public void hide() {
		frame.setVisible(false);
	}

	public boolean isVisible() {
		return frame.isVisible();
	}

	public void load() {
		String[] content = StaticReader.read(file, false).split(System.getProperty("line.separator"));
		if (content != null && content.length > 0) {
			for (String line : content) {

			}
		}else Log.warn("Could not find config file");
	}

	public void save() {

	}

	public List<String> getExcludeFiles() {
		return files;
	}

	public List<String> getExcludeDirectories() {
		return dirs;
	}
}
