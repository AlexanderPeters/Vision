package webcam;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;



class FacePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	public FacePanel() {
		super();
	}

	public boolean matToBufferedImage(Mat matrix) {
		MatOfByte mb = new MatOfByte();
		Highgui.imencode(".jpg", matrix, mb);
		try {
			this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image == null)
			return;
		g.drawImage(this.image, 10, 10, this.image.getWidth(), this.image.getHeight(), null);
	}

}

class VisionProcessing {

	public static ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	public static Point targetcenter = null, imagecenter = null;
	public static int yoffcenter, xoffcenter;
	public static int recwidth;

	public static Mat findHighGoal(Mat m) throws InterruptedException {

		Mat hierarchy = new Mat();
		Mat mask = new Mat();
		Mat image = new Mat();
		Mat image2 = new Mat();
		Mat image3 = new Mat();

		Core.inRange(m, new Scalar(Integer.parseInt(Main.huetxt), Integer.parseInt(Main.luminancetxt), Integer.parseInt(Main.saturationtxt)), new Scalar(255, 255, 255), mask);
		Core.bitwise_and(m, m, image, mask);
		Imgproc.cvtColor(image, image2, Imgproc.COLOR_RGB2GRAY);
		Imgproc.findContours(image2, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		for (Iterator<org.opencv.core.MatOfPoint> iterator = contours.iterator(); iterator.hasNext();) {
			org.opencv.core.MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
			Rect rec = Imgproc.boundingRect(matOfPoint);
			if (rec.height < 35 || rec.width < 35) {
				iterator.remove();
				continue;
			}
			float aspect = (float) rec.width / (float) rec.height;
			if (aspect < 1.0)
				iterator.remove();
		}

		if (contours.size() == 1) {
			Rect rec = Imgproc.boundingRect(contours.get(0));
			Core.rectangle(image2, rec.br(), rec.tl(), new Scalar(255, 255, 255));
			targetcenter = new Point(rec.x + rec.width / 2, rec.y + rec.height / 2);
			imagecenter = new Point(image2.width() / 2, image2.height() / 2);
			recwidth = rec.width;

		}

		Imgproc.cvtColor(image2, image3, Imgproc.COLOR_GRAY2RGB);
		return image3;

	}
}

public class Main implements ActionListener {
	
	public static String huetxt = "0";
	public static String saturationtxt = "0";
	public static String luminancetxt = "0";
	static JPanel HSLValues = new JPanel();
	JTextField txtHue;
	JTextField txtSaturation;
	JTextField txtLuminance;
	
	Main() {
	
	txtHue = new JTextField(5);
	txtSaturation = new JTextField(5);
	txtLuminance = new JTextField(5);
	
	
    //setLayout(new FlowLayout());
    //addWindowListener(this);
    //b = new Button("Click me");
    //add(b);
    //add(text);
    //b.addActionListener(this);
	
    

	HSLValues.add("North", new Label("Hue Value"));
	
	

	HSLValues.add("South", txtHue);
	txtHue.setText("0");
	txtHue.addActionListener(this);
	//frame.add("North", HSLValues);
	
	HSLValues.add("North", new Label("Saturation Value"));
	HSLValues.add("South", txtSaturation);
	txtSaturation.setText("0");
	txtSaturation.addActionListener(this);
	//

	HSLValues.add("North", new Label("Luminance Value"));
	HSLValues.add("South", txtLuminance);
	txtLuminance.setText("0");
	txtLuminance.addActionListener(this);
	
	}

    
	public static void main(String[] args) throws InterruptedException{
		JFrame frame = new JFrame("WebCam Capture - Face detection");
		
		FacePanel facePanel = new FacePanel();
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 400);
		frame.setBackground(Color.BLUE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.add("Center", HSLValues);
		//frame.add("South", HSLValues);
		frame.setVisible(true);
	    
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

		//JFrame frame = new JFrame("WebCam Capture - Face detection");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		

		//frame.setSize(400, 400);
		
		frame.add(facePanel, BorderLayout.CENTER);
		frame.add(HSLValues, BorderLayout.SOUTH);
		frame.setVisible(true);

		Mat webcam_image = new Mat();
		VideoCapture webCam = new VideoCapture(0);
		Mat processed_image = new Mat();

		Mat displayable = new Mat();

		if (webCam.isOpened()) {
			Thread.sleep(500);
			while (true) {
				webCam.read(webcam_image);
				if (!webcam_image.empty()) {
					Thread.sleep(5);
					processed_image = VisionProcessing.findHighGoal(webcam_image);
					List<Mat> src = Arrays.asList(webcam_image, processed_image);
					Core.hconcat(src, displayable);

					frame.setSize(displayable.width() + 40, displayable.height() + 60);

					facePanel.matToBufferedImage(displayable);
					facePanel.repaint();
				} else {
					System.out.println(" --(!) No captured frame from webcam !");

					break;
				}
			}
		}
		webCam.release();
		frame.dispose();
		Thread.sleep(50);
		main(args);
	}
	

	


	@Override
	public void actionPerformed(ActionEvent arg0) {
		huetxt = txtHue.getText();
		saturationtxt = txtSaturation.getText();
		luminancetxt = txtLuminance.getText();
		// TODO Auto-generated method stub
		
	}

}