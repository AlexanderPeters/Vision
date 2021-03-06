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

//Public class which defines the functionality of the new button in the user GUI
public class newButtonWindow extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;

	String userInputTxt;
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();

	JLabel userInputLabel = new JLabel("New File Name (Do Not Include Extensions or Directory)");
	static JTextField userInput = new JTextField(25);
	JButton saveButton = new JButton("Save");

	//Main method to set everything up
	public static void main() {
		newButtonWindow fr = new newButtonWindow();
		centerFrame(fr);
		fr.setVisible(true);

	}

	//Class constructor which defines the functionality of the new button
	public newButtonWindow() {

		setSize(500, 120);
		setTitle("Create a new file.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		userInput.addActionListener(this);
		saveButton.addActionListener(this);
		panel.add(userInputLabel);
		panel.add(userInput);
		panel.add(saveButton);
		add("Center", panel);

	}

	//Method to allow the window created by pressing the new button to be centered on the screen
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
	//If the new button is clicked this will happen
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			String userInputTxt = new String();

			if (OSValidator.isWindows()) {
				userInputTxt = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + formatPath() + ".txt";
			} else if (OSValidator.isUnix()) {
				userInputTxt = "/home/debian/Desktop/" + formatPath() + ".txt";
			}

			createNewCSVFile.setFileName(userInputTxt);
			createNewCSVFile.createNewFile();
			userInput.setText(null);
			dispose();

		}

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		guiMain.main(null);

	}

	@Override
	public void windowClosing(WindowEvent arg0) {

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