package visionTestProgram;

import java.io.FileWriter;
import java.io.IOException;

public class createNewCSVFile {
	static String fileName;
	static String defaultLineOne = "HueMin, HueMax, SaturationMin, SaturationMax, ValueMin, ValueMax";

	public static void setFileName(String name) {
		fileName = name;

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
			readAndWriteCSV.writeStringToFile(defaultLineOne);
			readAndWriteCSV.writeIntValuesToFile(000, 000, 000, 000, 000, 000);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}