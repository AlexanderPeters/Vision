package visionTestProgram;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Public class that defines the functionality of the Default Dir button under the options menu
public class defaultDirButtonWindow extends JFrame implements ActionListener, WindowListener {
	static JTextField userInput = new JTextField(25);
	JButton saveButton = new JButton("Save");

	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel userInputLabel = new JLabel("Enter your Dir Path here");
	String userInputTxt;

	private static final long serialVersionUID = 1L;

	//Main method to set everything up
	public static void main() {
		defaultDirButtonWindow fr = new defaultDirButtonWindow();
		centerFrame(fr);
		fr.setVisible(true);

	}

	//Class constructor to defines the default Dir window
	public defaultDirButtonWindow() {
		setTitle("Set Dir Path (Do Not Include Extensions or Directory)");
		setSize(500, 120);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		userInput.addActionListener(this);
		saveButton.addActionListener(this);
		panel.add(userInputLabel);
		panel.add(userInput);
		panel.add(saveButton);
		add("Center", panel);

	}

	//Method to center the default Dir button window on the screen
	private static void centerFrame(JFrame fr) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = fr.getSize().width;
		int h = fr.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		fr.setLocation(x, y);
	}

	//Format the user input so that all inputs will be uniform as far as extension (corrects incorrect extensions to .txt)
	public static String formatPath() {
		String extension = userInput.getText().substring(userInput.getText().lastIndexOf(".") + 1,
				userInput.getText().length());
		String fileMinusExt = userInput.getText().replaceAll(".txt", "");

		if (extension.equals("txt")) {
			return fileMinusExt;
		} else {
			return userInput.getText();
		}
	}

	@Override
	//If the save button is clicked this will happen
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton && !userInput.getText().isEmpty()) {
			String userInputTxt = new String();

			if (OSValidator.isWindows()) {
				userInputTxt = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + formatPath() + ".txt";
			} else if (OSValidator.isUnix()) {
				userInputTxt = "/home/debian/Desktop/" + formatPath() + ".txt";
			}

			createNewSettingsFile.setDirPath(userInputTxt);
			createNewSettingsFile.createNewFile();
			userInput.setText(null);
			dispose();

		}

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	//When the window is closed reopen the main gui
	public void windowClosed(WindowEvent arg0) {
		guiMain.main(null);

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}