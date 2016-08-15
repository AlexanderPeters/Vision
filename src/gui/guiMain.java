package gui;

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
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.*;

public class guiMain extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;

	JButton buttonNew = new JButton("New");
	JButton buttonOpen = new JButton("Open");
	JButton buttonSaveAs = new JButton("Save As");
	JButton buttonExit = new JButton("Exit");

	JTextField txtHueMin = new JTextField(3);
	JTextField txtSaturationMin = new JTextField(3);
	JTextField txtLuminanceMin = new JTextField(3);
	JTextField txtHueMax = new JTextField(3);
	JTextField txtSaturationMax = new JTextField(3);
	JTextField txtLuminanceMax = new JTextField(3);

	JLabel labelHueMin = new JLabel("Hue Min Filter Value");
	JLabel labelHueMax = new JLabel("Hue Max Filter Value");
	JLabel labelSaturationMin = new JLabel("Saturation Min Filter Value");
	JLabel labelSaturationMax = new JLabel("Saturation Max Filter Value");
	JLabel labelLuminanceMin = new JLabel("Luminance Min Filter Value");
	JLabel labelLuminanceMax = new JLabel("Luminance Max Filter Value");

	JLabel sldLabelHueMin = new JLabel("Hue Min Slider");
	JLabel sldLabelHueMax = new JLabel("Hue Max Slider");
	JLabel sldLabelSaturationMin = new JLabel("Saturation Min Slider");
	JLabel sldLabelSaturationMax = new JLabel("Saturation Max Slider");
	JLabel sldLabelLuminanceMin = new JLabel("Luminance Min Slider");
	JLabel sldLabelLuminanceMax = new JLabel("Luminance Max Slider");

	static JSlider sldHueMin = new JSlider(0, 255, 0);
	static JSlider sldHueMax = new JSlider(0, 255, 0);
	static JSlider sldSaturationMin = new JSlider(0, 255, 0);
	static JSlider sldSaturationMax = new JSlider(0, 255, 0);
	static JSlider sldLuminanceMin = new JSlider(0, 255, 0);
	static JSlider sldLuminanceMax = new JSlider(0, 255, 0);

	JPanel panel = new JPanel();

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

		addtxtBox(txtHueMin);
		addtxtBox(txtHueMax);
		addtxtBox(txtSaturationMin);
		addtxtBox(txtSaturationMax);
		addtxtBox(txtLuminanceMin);
		addtxtBox(txtLuminanceMax);

		addSlider(sldHueMin);
		addSlider(sldHueMax);
		addSlider(sldSaturationMin);
		addSlider(sldSaturationMax);
		addSlider(sldLuminanceMin);
		addSlider(sldLuminanceMax);

		GroupLayout layout = new GroupLayout(panel);

		panel.setLayout(layout);

		setSize(750, 325);
		setTitle("HSL Values");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelHueMin)
						.addComponent(txtHueMin).addComponent(labelSaturationMin).addComponent(txtSaturationMin)
						.addComponent(labelLuminanceMin).addComponent(txtLuminanceMin)

				)

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(sldLabelHueMin)
						.addComponent(sldHueMin).addComponent(sldLabelSaturationMin).addComponent(sldSaturationMin)
						.addComponent(sldLabelLuminanceMin).addComponent(sldLuminanceMin))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelHueMax)
						.addComponent(txtHueMax).addComponent(labelSaturationMax).addComponent(txtSaturationMax)
						.addComponent(labelLuminanceMax).addComponent(txtLuminanceMax))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)

						.addComponent(sldLabelHueMax).addComponent(sldHueMax).addComponent(sldLabelSaturationMax)
						.addComponent(sldSaturationMax).addComponent(sldLabelLuminanceMax)
						.addComponent(sldLuminanceMax))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(buttonNew)
						.addComponent(buttonOpen).addComponent(buttonSaveAs).addComponent(buttonExit)));

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

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(labelLuminanceMin)
						.addComponent(sldLabelLuminanceMin).addComponent(labelLuminanceMax)
						.addComponent(sldLabelLuminanceMax))

				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(txtLuminanceMin)
						.addComponent(sldLuminanceMin).addComponent(txtLuminanceMax).addComponent(sldLuminanceMax))

		);

		add("Center", panel);
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

		} else if (e.getSource() == txtHueMin) {
			sldHueMin.setValue(Integer.parseInt(txtHueMin.getText()));

		} else if (e.getSource() == txtHueMax) {
			sldHueMax.setValue(Integer.parseInt(txtHueMax.getText()));

		} else if (e.getSource() == txtSaturationMin) {
			sldSaturationMin.setValue(Integer.parseInt(txtSaturationMin.getText()));

		} else if (e.getSource() == txtSaturationMax) {
			sldSaturationMax.setValue(Integer.parseInt(txtSaturationMax.getText()));

		} else if (e.getSource() == txtLuminanceMin) {
			sldLuminanceMin.setValue(Integer.parseInt(txtLuminanceMin.getText()));

		} else if (e.getSource() == txtLuminanceMax) {
			sldLuminanceMax.setValue(Integer.parseInt(txtLuminanceMax.getText()));

		}

	}

	public void stateChanged(ChangeEvent e) {
		txtHueMin.setText(String.valueOf(sldHueMin.getValue()));
		txtHueMax.setText(String.valueOf(sldHueMax.getValue()));
		txtSaturationMin.setText(String.valueOf(sldSaturationMin.getValue()));
		txtSaturationMax.setText(String.valueOf(sldSaturationMax.getValue()));
		txtLuminanceMin.setText(String.valueOf(sldLuminanceMin.getValue()));
		txtLuminanceMax.setText(String.valueOf(sldLuminanceMax.getValue()));
	}

	public static int getHueMinValue() {
		return sldHueMin.getValue();
	}

	public static void setHueMinValue(int x) {
		sldHueMin.setValue(x);
	}

	public static int getHueMaxValue() {
		return sldHueMax.getValue();
	}

	public static void setHueMaxValue(int x) {
		sldHueMax.setValue(x);
	}

	public static int getSaturationMinValue() {
		return sldSaturationMin.getValue();
	}

	public static void setSaturationMinValue(int x) {
		sldSaturationMin.setValue(x);
	}

	public static int getSaturationMaxValue() {
		return sldHueMax.getValue();
	}

	public static void setSaturationMaxValue(int x) {
		sldSaturationMax.setValue(x);
	}

	public static int getLuminanceMinValue() {
		return sldLuminanceMin.getValue();
	}

	public static void setLuminanceMinValue(int x) {
		sldLuminanceMin.setValue(x);
	}

	public static int getLuminanceMaxValue() {
		return sldLuminanceMax.getValue();
	}

	public static void setLuminanceMaxValue(int x) {
		sldLuminanceMax.setValue(x);
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
}
