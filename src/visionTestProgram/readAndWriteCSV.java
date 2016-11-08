package visionTestProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Public class which defines functions for reading and writing to CSV files
public class readAndWriteCSV {

	static String value = null;
	static int i = 0;
	public static String fileLocation;
	public static List<String> objects = new ArrayList<>();

	// Method which reads all CSV file data into a list and each line into a
	// String Array
	public static void readFromFile() {
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader(fileLocation));
			List<String> lines = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			String chosenLine = lines.get(1);
			String[] values = chosenLine.split(",");
			int loop = 0;

			while (!values[loop].equals("Last")) {
				objects.add(values[loop]);
				loop++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Method which reads the defaultDirOfSettingFile and returns the directory
	// of the settings file
	public static String readFromSettingsFile() {
		String chosenLine = new String();
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader(fileLocation));
			List<String> lines = new ArrayList<>();
			String line = null;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			chosenLine = lines.get(0);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return chosenLine;

	}

	// Parses through a line one item at a time upon being called and returns
	// its value
	public static String getValue() {

		if (i < 0) {
			i = 0;
		}

		if (i <= objects.size()) {
			String element = objects.get(i);
			if (element == null || element == "Last") {
				value = null;
			} else if (element != null && element != "Last") {
				value = element;
			}
		}

		return value;
	}

	// Method which allows for multiple filter values to be assembled into one
	// string which is returned
	public static String assembleInput(int a, int b, int c, int d, int e, int f) {
		String input = (String.valueOf(a) + "," + String.valueOf(b) + "," + String.valueOf(c) + "," + String.valueOf(d)
				+ "," + String.valueOf(e) + "," + String.valueOf(f) + "," + "Last");

		return input;

	}

	// Writes multiple filter values to one line within a CSV file
	public static void writeIntValuesToFile(int a, int b, int c, int d, int e, int f) throws IOException {
		File file = new File(fileLocation);

		BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
		try {
			out.append(assembleInput(a, b, c, d, e, f));
			out.newLine();
		} finally {
			out.close();
		}
	}

	// Allows for a string to be written to a text file
	public static void writeStringToFile(String input) throws IOException {
		File file = new File(fileLocation);

		BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
		try {
			out.append(input);
			out.newLine();
		} finally {
			out.close();
		}

	}

	// Method which increments the position that the getValue method reads
	public static String readInNext() {
		i++;
		return getValue();

	}

	// Resets class level variables so that the class is ready to read another
	// file
	public static void resetCount() {
		i = 0;
		objects = new ArrayList<>();
	}

	// Method which decrements the position that the getValue method reads
	// (Currently unused)
	public static String readInPrevious() {
		i--;
		return getValue();

	}

	// Returns the current file location that has been set (Currently unused)
	public static String getFileLocation() {
		return (fileLocation);
	}
	
	//Method which sets the file location to be used
	public static void setFileLocation(String e) {
		fileLocation = e;

	}

}
