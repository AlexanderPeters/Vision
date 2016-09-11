package visionTestProgram;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class defaultDirButtonWindow extends JFrame{
	
	

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		newButtonWindow fr = new newButtonWindow();
		centerFrame(fr);
		fr.setVisible(true);

	}
	
	public defaultDirButtonWindow(){
		
	}
	
	private static void centerFrame(JFrame fr) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = fr.getSize().width;
		int h = fr.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		fr.setLocation(x, y);
	}
	

}