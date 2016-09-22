package visionTestProgram;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class openButtonWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	static String filePath;
	static String shortFileName;

	JFileChooser fc = new JFileChooser();

	public openButtonWindow() {
		String defaultPath = new String();

		if (OSValidator.isWindows()){
			defaultPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop";}
		else if (OSValidator.isUnix()){
			defaultPath = "/home/debian/Desktop";}

		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Text files", "txt");

		fc.setCurrentDirectory(new File(defaultPath));
		fc.addChoosableFileFilter(textFilter);
		fc.setAcceptAllFileFilterUsed(false);

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();

			try {
				filePath = selectedFile.getCanonicalPath();
				shortFileName = selectedFile.getName();
				
				readInValues();

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public static String getPath() {
		String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		String filePlusExt = filePath + ".txt";

		if (!extension.equals("txt")) {
			return filePlusExt;

		} else {
			return filePath;
		}
	}

	public static String getShortName() {
		return shortFileName;
	}

	private void readInValues() {
		// Improve by using Iterator and list
		readAndWriteCSV.setFileLocation(getPath());
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