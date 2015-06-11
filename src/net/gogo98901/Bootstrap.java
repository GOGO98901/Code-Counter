package net.gogo98901;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import net.gogo98901.codeCounter.Display.Display;
import net.gogo98901.log.Log;
import net.gogo98901.util.Data;

public class Bootstrap {
	private static Display display;

	public static void main(String[] args) {
		Log.info("Bootstrap... STARTED [" + new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()) + "]");
		try {
			Data.setDefultLookAndFeel();

			display = new Display();
			Log.info("Bootstrap... OK");
		} catch (Exception e) {
			Log.severe("Bootstrap... ERROR");
			Log.stackTrace(Level.SEVERE, e);
			return;
		}
		display.setVisible(true);
	}

	public static String getTitle() {
		return "Code Counter";
	}

	public static String getVersion() {
		return "2.0";
	}

	public static String getAuthor() {
		return "Rory Claasen";
	}
}
