package visionTestProgram;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class openButtonWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	static String filePath;
	static String shortFileName;

	JFileChooser fc = new JFileChooser();

	public openButtonWindow() {
		String defaultPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop";

		JFileChooser fc = new JFileChooser();

		fc.setCurrentDirectory(new File(defaultPath));

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();

			try {
				filePath = selectedFile.getCanonicalPath();
				shortFileName = selectedFile.getName();
				readAndWriteCSV.setFileLocation(getPath());
				readInValues();

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public static String getPath() {
		return filePath;
	}

	public static String getShortName() {
		return shortFileName;
	}

	private void readInValues() {
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