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

public class readAndWriteCSV {

	static String value = null;
	static int i = 0;
	public static String fileLocation;
	public static List<String> objects = new ArrayList<>();

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
	public static String readFromSettingsFile(){
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
			

		} 
		catch (FileNotFoundException e) {
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

	public static String assembleInput(int a, int b, int c, int d, int e, int f) {
		String input = (String.valueOf(a) + "," + String.valueOf(b) + "," + String.valueOf(c) + "," + String.valueOf(d)
				+ "," + String.valueOf(e) + "," + String.valueOf(f) + "," + "Last");

		return input;

	}

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

	public static String readInNext() {
		i++;
		return getValue();

	}

	public static void resetCount() {
		i = 0;
	}

	public static String readInPrevious() {
		i--;
		return getValue();

	}

	public static String getFileLocation() {
		return (fileLocation);
	}

	public static void setFileLocation(String e) {
		fileLocation = e;

	}

}
