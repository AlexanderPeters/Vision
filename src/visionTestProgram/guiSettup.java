package visionTestProgram;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;

public class guiSettup extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void setFilePath(String path) throws UnknownHostException, IOException {

		readAndWriteCSV.setFileLocation(path);
		String defaultDir = readAndWriteCSV.readFromSettingsFile();
		File tempDefaultDirChecker = new File(defaultDir);
		if (tempDefaultDirChecker.exists() && tempDefaultDirChecker.length() != 0) {
			readAndWriteCSV.setFileLocation(defaultDir);
			readInValues();
		}

	}

	private static void readInValues() {
		// Improve by using Iterator and list
		readAndWriteCSV.readFromFile();
		if (readAndWriteCSV.getValue() != null) {
			guiMain.setHueMinValue(Integer.parseInt(readAndWriteCSV.getValue()));
			guiMain.setHueMaxValue(Integer.parseInt(readAndWriteCSV.readInNext()));
			guiMain.setSaturationMinValue(Integer.parseInt(readAndWriteCSV.readInNext()));
			guiMain.setSaturationMaxValue(Integer.parseInt(readAndWriteCSV.readInNext()));
			guiMain.setValueMinValue(Integer.parseInt(readAndWriteCSV.readInNext()));
			guiMain.setValueMaxValue(Integer.parseInt(readAndWriteCSV.readInNext()));
			readAndWriteCSV.resetCount();
		}

	}
}
