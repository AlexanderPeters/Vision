package visionTestProgram;

//API Info
//Group Layout API
//https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
//Slider API
//https://docs.oracle.com/javase/tutorial/uiswing/components/slider.html
//Button API
//https://docs.oracle.com/javase/tutorial/uiswing/components/button.html
//Label API
//https://docs.oracle.com/javase/tutorial/uiswing/components/label.html

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
//import java.io.IOException;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.*;

public class guiMain extends JFrame implements ActionListener, ChangeListener, WindowListener {
	private static final long serialVersionUID = 1L;
	
	static String settingsPath = new String();
	
	JButton buttonNew = new JButton("New");
	JButton buttonOpen = new JButton("Open");
	JButton buttonSaveAs = new JButton("Save As");
	JButton buttonExit = new JButton("Exit");
	JButton buttonDefaultDir = new JButton("Default Dir");
	
	static JTextField txtHueMin = new JTextField(3);
	static JTextField txtSaturationMin = new JTextField(3);
	static JTextField txtValueMin = new JTextField(3);
	static JTextField txtHueMax = new JTextField(3);
	static JTextField txtSaturationMax = new JTextField(3);
	static JTextField txtValueMax = new JTextField(3);

	JLabel labelHueMin = new JLabel("Hue Min Filter Value");
	JLabel labelHueMax = new JLabel("Hue Max Filter Value");
	JLabel labelSaturationMin = new JLabel("Saturation Min Filter Value");
	JLabel labelSaturationMax = new JLabel("Saturation Max Filter Value");
	JLabel labelValueMin = new JLabel("Value Min Filter Value");
	JLabel labelValueMax = new JLabel("Value Max Filter Value");

	JLabel sldLabelHueMin = new JLabel("Hue Min Slider");
	JLabel sldLabelHueMax = new JLabel("Hue Max Slider");
	JLabel sldLabelSaturationMin = new JLabel("Saturation Min Slider");
	JLabel sldLabelSaturationMax = new JLabel("Saturation Max Slider");
	JLabel sldLabelValueMin = new JLabel("Value Min Slider");
	JLabel sldLabelValueMax = new JLabel("Value Max Slider");

	static JSlider sldHueMin = new JSlider(0, 255, 0);
	static JSlider sldHueMax = new JSlider(0, 255, 0);
	static JSlider sldSaturationMin = new JSlider(0, 255, 0);
	static JSlider sldSaturationMax = new JSlider(0, 255, 0);
	static JSlider sldValueMin = new JSlider(0, 255, 0);
	static JSlider sldValueMax = new JSlider(0, 255, 0);

	static String HueMinVal = "0";
	static String HueMaxVal = "0";
	static String SaturationMinVal = "0";
	static String SaturationMaxVal = "0";
	static String ValueMinVal = "0";
	static String ValueMaxVal = "0";

	JPanel panel = new JPanel();
	int timesLooped = 0;

	public static void main(String[] args) {
		guiMain fr = new guiMain();
		centerFrame(fr);
		fr.setVisible(true);

	}

	public guiMain() {

		buttonNew.addActionListener(this);
		buttonOpen.addActionListener(this);
		buttonSaveAs.addActionListener(this);
		buttonExit.addActionListener(this);
		buttonDefaultDir.addActionListener(this);

		addtxtBox(txtHueMin);
		addtxtBox(txtHueMax);
		addtxtBox(txtSaturationMin);
		addtxtBox(txtSaturationMax);
		addtxtBox(txtValueMin);
		addtxtBox(txtValueMax);

		addSlider(sldHueMin);
		addSlider(sldHueMax);
		addSlider(sldSaturationMin);
		addSlider(sldSaturationMax);
		addSlider(sldValueMin);
		addSlider(sldValueMax);

		sldHueMax.setValue(255);
		sldSaturationMax.setValue(255);
		sldValueMax.setValue(255);

		addWindowListener(this);

		GroupLayout layout = new GroupLayout(panel);

		panel.setLayout(layout);

		setSize(750, 325);
		setTitle("HSV Values");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelHueMin)
						.addComponent(txtHueMin).addComponent(labelSaturationMin).addComponent(txtSaturationMin)
						.addComponent(labelValueMin).addComponent(txtValueMin)

				)

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(sldLabelHueMin)
						.addComponent(sldHueMin).addComponent(sldLabelSaturationMin).addComponent(sldSaturationMin)
						.addComponent(sldLabelValueMin).addComponent(sldValueMin))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelHueMax)
						.addComponent(txtHueMax).addComponent(labelSaturationMax).addComponent(txtSaturationMax)
						.addComponent(labelValueMax).addComponent(txtValueMax))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)

						.addComponent(sldLabelHueMax).addComponent(sldHueMax).addComponent(sldLabelSaturationMax)
						.addComponent(sldSaturationMax).addComponent(sldLabelValueMax).addComponent(sldValueMax))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(buttonNew)
						.addComponent(buttonOpen).addComponent(buttonSaveAs).addComponent(buttonExit)
						.addComponent(buttonDefaultDir)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelHueMin)
						.addComponent(sldLabelHueMin).addComponent(labelHueMax).addComponent(sldLabelHueMax)
						.addComponent(buttonNew))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(txtHueMin)
						.addComponent(sldHueMin).addComponent(txtHueMax).addComponent(sldHueMax)
						.addComponent(buttonOpen))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelSaturationMin)
						.addComponent(sldLabelSaturationMin).addComponent(labelSaturationMax)
						.addComponent(sldLabelSaturationMax).addComponent(buttonSaveAs))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(txtSaturationMin)
						.addComponent(sldSaturationMin).addComponent(txtSaturationMax).addComponent(sldSaturationMax)
						.addComponent(buttonExit))// .addComponent(setDefaultDirectoryLabel))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelValueMin)
						.addComponent(sldLabelValueMin).addComponent(labelValueMax)
						.addComponent(sldLabelValueMax).addComponent(buttonDefaultDir))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(txtValueMin)
						.addComponent(sldValueMin).addComponent(txtValueMax).addComponent(sldValueMax))

		);

		add("Center", panel);
		
		if (OSValidator.isWindows()){
			settingsPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\DefaultDirOfSettingsFile.txt";
		} else if (OSValidator.isUnix()){			
			settingsPath = "/home/debian/Desktop/DefaultDirOfSettingsFile.txt";		
		}
		
		if (timesLooped == 0){
			timesLooped++;
			File tempDirChecker = new File(settingsPath);
			if (tempDirChecker.exists() && tempDirChecker.length() != 0){
				try {
					guiSettup.setFilePath(settingsPath);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private static void centerFrame(JFrame fr) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = fr.getSize().width;
		int h = fr.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		fr.setLocation(x, y);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonNew) {
			dispose();
			newButtonWindow.main(null);

		} else if (e.getSource() == buttonOpen) {
			new openButtonWindow();

		} else if (e.getSource() == buttonSaveAs) {
			new saveAsButtonWindow();

		} else if (e.getSource() == buttonExit) {
			dispose();
		} else if (e.getSource() == buttonDefaultDir){
			dispose();
			defaultDirButtonWindow.main(null);
		}

		else {

			// } else if (e.getSource() == txtHueMin) {
			sldHueMin.setValue(parseInt(txtHueMin.getText()));
			HueMinVal = txtHueMin.getText();

			// } else if (e.getSource() == txtHueMax) {
			sldHueMax.setValue(parseInt(txtHueMax.getText()));
			HueMaxVal = txtHueMax.getText();

			// } else if (e.getSource() == txtSaturationMin) {
			sldSaturationMin.setValue(parseInt(txtSaturationMin.getText()));
			SaturationMinVal = txtSaturationMin.getText();

			// } else if (e.getSource() == txtSaturationMax) {
			sldSaturationMax.setValue(parseInt(txtSaturationMax.getText()));
			SaturationMaxVal = txtSaturationMax.getText();

			// } else if (e.getSource() == txtValueMin) {
			sldValueMin.setValue(parseInt(txtValueMin.getText()));
			ValueMinVal = txtValueMin.getText();

			// } else if (e.getSource() == txtValueMax) {
			sldValueMax.setValue(parseInt(txtValueMax.getText()));
			ValueMaxVal = txtValueMax.getText();

		}

	}

	public void stateChanged(ChangeEvent e) {
		txtHueMin.setText(String.valueOf(sldHueMin.getValue()));
		txtHueMax.setText(String.valueOf(sldHueMax.getValue()));
		txtSaturationMin.setText(String.valueOf(sldSaturationMin.getValue()));
		txtSaturationMax.setText(String.valueOf(sldSaturationMax.getValue()));
		txtValueMin.setText(String.valueOf(sldValueMin.getValue()));
		txtValueMax.setText(String.valueOf(sldValueMax.getValue()));
		HueMinVal = String.valueOf(sldHueMin.getValue());
		HueMaxVal = String.valueOf(sldHueMax.getValue());
		SaturationMinVal = String.valueOf(sldSaturationMin.getValue());
		SaturationMaxVal = String.valueOf(sldSaturationMax.getValue());
		ValueMinVal = String.valueOf(sldValueMin.getValue());
		ValueMaxVal = String.valueOf(sldValueMax.getValue());

	}

	public static int parseInt(String s) {
		return Integer.parseInt(s);

	}

	public static int getHueMinValue() {
		return parseInt(HueMinVal);
	}

	public static void setHueMinValue(int x) {
		sldHueMin.setValue(x);
	}

	public static int getHueMaxValue() {
		return parseInt(HueMaxVal);
	}

	public static void setHueMaxValue(int x) {
		sldHueMax.setValue(x);
	}

	public static int getSaturationMinValue() {
		return parseInt(SaturationMinVal);
	}

	public static void setSaturationMinValue(int x) {
		sldSaturationMin.setValue(x);
	}

	public static int getSaturationMaxValue() {
		return parseInt(SaturationMaxVal);
	}

	public static void setSaturationMaxValue(int x) {
		sldSaturationMax.setValue(x);
	}

	public static int getValueMinValue() {
		return parseInt(ValueMinVal);
	}

	public static void setValueMinValue(int x) {
		sldValueMin.setValue(x);
	}

	public static int getValueMaxValue() {
		// System.out.print(ValueMaxVal);
		return parseInt(ValueMaxVal);

	}

	public static void setValueMaxValue(int x) {
		sldValueMax.setValue(x);
	}

	public void addSlider(JSlider s) {

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("0"));
		labelTable.put(new Integer(255), new JLabel("255"));

		s.setMajorTickSpacing(10);
		s.setPaintTicks(true);
		s.setLabelTable(labelTable);
		s.setPaintLabels(true);
		s.addChangeListener(this);

	}

	public void addtxtBox(JTextField s) {
		s.setText("0");
		s.addActionListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
