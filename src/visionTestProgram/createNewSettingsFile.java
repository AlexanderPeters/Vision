package visionTestProgram;

import java.io.FileWriter;
import java.io.IOException;

//Publicly available class whose methods can be called inorder to create a new settings file
public class createNewSettingsFile {
	static String fileName = new String();
	static String dirPath = new String();

	//Method to set the directory of the CSV file
	public static void setDirPath(String path) {
		dirPath = path;

	}

	@SuppressWarnings("resource")
	//Method to create the new settings file that defines the location of the CSV file
	public static void createNewFile() {
		try {
			if (OSValidator.isWindows()) {
				fileName = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\DefaultDirOfSettingsFile.txt";
			} else if (OSValidator.isUnix()) {
				fileName = "/home/debian/Desktop/DefaultDirOfSettingsFile.txt";
			}
			new FileWriter(fileName);
			createBaseConfig();
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}

	//Method that adds the data to the newly created file
	public static void createBaseConfig() {

		readAndWriteCSV.setFileLocation(fileName);
		try {
			readAndWriteCSV.writeStringToFile(dirPath);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}