package visionTestProgram;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

//Public class which defines the functionality of the save as button in the user GUI
public class saveAsButtonWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	static String defaultLineOne = "HueMin, HueMax, SaturationMin, SaturationMax, LuminanceMin, LuminanceMax";
	static String filePath;
	static String shortFileName;

	JFileChooser fc = new JFileChooser();

	//Constructor to set up a JFileChooser as part of the GUI
	public saveAsButtonWindow() {
		String defaultPath = new String();

		if (OSValidator.isWindows()) {
			defaultPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop";
		} else if (OSValidator.isUnix()) {
			defaultPath = "/home/debian/Desktop";
		}

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
				writeToCSV();

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	//Getter method for selected file path
	public static String getPath() {
		String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		String filePlusExt = filePath + ".txt";

		if (!extension.equals("txt")) {
			return filePlusExt;

		} else {
			return filePath;
		}

	}

	//Method which server as a Getter for the short filename (Currently unused)
	public static String getShortName() {
		return shortFileName;
	}

	//Writes the currently set filter values within the GUI to the CSV specified
	public static void writeToCSV() throws IOException {
		readAndWriteCSV.setFileLocation(getPath());
		readAndWriteCSV.writeStringToFile(defaultLineOne);
		readAndWriteCSV.writeIntValuesToFile(guiMain.getHueMinValue(), guiMain.getHueMaxValue(),
				guiMain.getSaturationMinValue(), guiMain.getSaturationMaxValue(), guiMain.getValueMinValue(),
				guiMain.getValueMaxValue());

	}

}