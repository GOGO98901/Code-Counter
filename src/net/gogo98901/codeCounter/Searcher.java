package net.gogo98901.codeCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import net.gogo98901.codeCounter.Display.config.Config;
import net.gogo98901.log.Log;

public class Searcher {
	private List<String> paths = new ArrayList<String>();
	private String path;

	private int lines, whiteSpace, files, dirs, filesExcluded, dirsExcluded;

	private Config config;

	public Searcher(Config config) {
		this.config = config;
		path = "";
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void search() {
		File filePath = new File(path);
		if (!filePath.exists()) {
			Log.info("Searching... Failed");
			return;
		}
		paths.clear();
		lines = 0;
		whiteSpace = 0;
		files = 0;
		dirs = 0;
		filesExcluded = 0;
		dirsExcluded = 0;

		File[] start = filePath.listFiles();
		scan(start);
		files = paths.size();
		Log.info("Searching... Finished");
		Log.info("Found " + files + " files and " + dirs + " directories");
		Log.info(lines + " lines, but with " + whiteSpace + " white space lines");
		Log.info("");
	}

	private void scan(File[] group) {
		for (File file : group) {
			// Log.info(file);
			if (file.isDirectory()) {
				if (checkDir("\\" + file.getName())) scan(file.listFiles());
			} else if (checkFile(file.getName())) {
				count(file);
				paths.add(file.getAbsolutePath());
			}
		}
	}

	private void count(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String data;
			while ((data = br.readLine()) != null) {
				if (data.length() == 0) whiteSpace++;
				lines++;
			}
			br.close();
		} catch (Exception e) {
			Log.warn("Could not read '" + file.getAbsolutePath() + "'");
			Log.stackTrace(e);
		}
	}

	private boolean checkFile(String file) {
		for (String type : config.getExcludeFiles()) {
			if (file.toLowerCase().endsWith(type.toLowerCase())) {
				filesExcluded++;
				return false;

			}
		}
		files++;
		return true;
	}

	private boolean checkDir(String dir) {
		for (String type : config.getExcludeDirectories()) {
			if (dir.toLowerCase().endsWith(type.toLowerCase())) {
				dirsExcluded++;
				return false;
			}
		}
		dirs++;
		return true;
	}

	public List<String> getPaths() {
		return paths;
	}

	public String getPath() {
		return path;
	}

	public int getLines() {
		return lines;
	}

	public int getWhiteSpace() {
		return whiteSpace;
	}

	public int getFiles() {
		return files;
	}

	public int getDirs() {
		return dirs;
	}

	public int getFilesExcluded() {
		return filesExcluded;
	}

	public int getDirsExcluded() {
		return dirsExcluded;
	}
}
