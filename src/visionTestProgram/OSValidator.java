package visionTestProgram;

//Public class that allows the program to determine under which operating system it is running
public class OSValidator {

	private static String OS = System.getProperty("os.name").toLowerCase();

	//Method which return true if the OS is of a Windows Architecture
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	//Method which return true if the OS is of a Mac OSX Architecture
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	//Method which return true if the OS is of a Unix Architecture
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	//Method which return true if the OS is of a Solaris Architecture
	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	//Method which returns the string identifier of the OS type
	public static String getOS() {
		if (isWindows()) {
			return "win";
		} else if (isMac()) {
			return "osx";
		} else if (isUnix()) {
			return "unix";
		} else if (isSolaris()) {
			return "sol";
		} else {
			return "err";
		}
	}
}