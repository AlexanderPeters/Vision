package visionTestProgram;

import java.io.FileWriter;
import java.io.IOException;

public class createNewSettingsFile {
	static String fileName = new String("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\DefaultDirOfSettingsFile.txt");
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
		readAndWriteCSV.setFileLocation(fileName);
		try {
			readAndWriteCSV.writeStringToFile(dirPath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}