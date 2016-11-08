package visionTestProgram;

import java.io.FileWriter;
import java.io.IOException;

//Publicly available class whose can be called in order to create a new CSV data file
public class createNewCSVFile {
	static String fileName;
	static String defaultLineOne = "HueMin, HueMax, SaturationMin, SaturationMax, ValueMin, ValueMax";

	//Method to set the name of the CSV file
	public static void setFileName(String name) {
		fileName = name;

	}

	@SuppressWarnings("resource")
	//Method to create the file
	public static void createNewFile() {
		try {
			new FileWriter(fileName);
			createBaseConfig();
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}

	//Method to initialize the file with instructional data and place holders for future data to be added 
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