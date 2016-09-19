package visionTestProgram;

import java.io.FileWriter;
import java.io.IOException;

public class createNewSettingsFile {
	static String fileName = new String();
	static String dirPath = new String();
	
	public static void setDirPath(String path){
		dirPath = path;
		
	}
	
	@SuppressWarnings("resource")
	public static void createNewFile() {
		try {
			new FileWriter(fileName);
			createBaseConfig();
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void createBaseConfig() {
		if (OSValidator.isWindows()){
			fileName = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\DefaultDirOfSettingsFile.txt";
		} else if (OSValidator.isUnix()){			
			fileName = "/home/debian/Desktop/DefaultDirOfSettingsFile.txt";		
		}
		readAndWriteCSV.setFileLocation(fileName);
		try {
			readAndWriteCSV.writeStringToFile(dirPath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}