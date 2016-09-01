package visionTestProgram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Size;

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
	public static int y, x;
	public static int recwidth;

	public static int initGui = 0;
	private final static Point point = new Point(-1, -1);
	private final static Size size = new Size(3, 3);

	public static Mat findHighGoal(Mat m) throws InterruptedException {

		Mat hierarchy = new Mat();

		Mat image2 = new Mat();
		Mat image3 = new Mat();
		Mat image4 = new Mat();
		Mat image6 = new Mat();

		// cvCvtColor();
		if (initGui == 0) {
			guiMain.main(null);// call gui once at start to get initialization
								// values

			initGui++;
		}
		Scalar low = new Scalar(guiMain.getHueMinValue(), guiMain.getValueMinValue(),
				guiMain.getSaturationMinValue());
		Scalar high = new Scalar(guiMain.getHueMaxValue(), guiMain.getValueMaxValue(),
				guiMain.getSaturationMaxValue());

		Mat frameHSV = new Mat(640, 480, CvType.CV_8UC3);
		Imgproc.cvtColor(m, frameHSV, Imgproc.COLOR_RGB2HSV);
		Mat frame_threshed = new Mat(640, 480, CvType.CV_8UC1);
		Core.inRange(frameHSV, low, high, frame_threshed);
		Core.bitwise_and(frameHSV, frameHSV, image2, frame_threshed);
		Imgproc.cvtColor(image2, image3, Imgproc.COLOR_HSV2RGB);
		Imgproc.cvtColor(image3, image4, Imgproc.COLOR_RGB2GRAY);
		//Imgproc.blur(image4, image4, size, point, Imgproc.BORDER_DEFAULT);

		Imgproc.findContours(image4, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		double recAreaLargest = 0;
		Point recbr = null;
		Point rectl = null;
		for (Iterator<org.opencv.core.MatOfPoint> iterator = contours.iterator(); iterator.hasNext();) {

			org.opencv.core.MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
			Rect rec = Imgproc.boundingRect(matOfPoint);

			if (rec.area() >= recAreaLargest) {
				recAreaLargest = rec.area();
				recbr = rec.br();
				rectl = rec.tl();
				// System.out.println("largest");

			}
			iterator.remove();

		}

		if (!(recbr == null) && !(rectl == null)){
		Core.rectangle(image4, recbr, rectl, new Scalar(255, 255, 255));
		recAreaLargest = 0;
		recbr = null;
		rectl = null;
		}
		if (contours.size() == 1) {
			Rect recdisp = Imgproc.boundingRect(contours.get(0));
			//System.out.println("test");
			try{
			Core.rectangle(image4, recdisp.br(), recdisp.tl(), new Scalar(255, 255, 255));
			recAreaLargest = 0;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		Imgproc.cvtColor(image4, image6, Imgproc.COLOR_GRAY2RGB);

		return image6;

	}
}

public class Main implements ActionListener {

	static JButton m = new JButton("Options");

	public static void main(String[] args) throws InterruptedException, IOException {
		Main fr = new Main();
	}

	public Main() throws InterruptedException, IOException {
		JFrame frame = new JFrame("WebCam Capture - FRC Vision");
		m.addActionListener(this);

		FacePanel facePanel = new FacePanel();
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 400);
		frame.setBackground(Color.BLUE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel n = new JPanel();

		n.add(m);

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		frame.add(facePanel, BorderLayout.CENTER);
		frame.add(n, BorderLayout.EAST);
		frame.setVisible(true);

		Mat webcam_image = new Mat();
		Mat imageGray = new Mat();
		VideoCapture webCam = new VideoCapture(0);
		Mat processed_image = new Mat();
		//URL url = new URL("http://192.168.2.7/axis-cgi/jpg/image.cgi");
		//BufferedImage image = ImageIO.read(url);
		//BufferedImage image = myBufferedImage;
		//byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		//Mat webcam_image = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		//webcam_image.put(0, 0, data);

		//webCam.open();
		//IpCam.open("http://192.168.1.30:8080/?dummy=param.mjpg");

		Mat displayable = new Mat();
		int x = 0;

		if (webCam.isOpened()) {
			Thread.sleep(500);
			while (true) {
				
				webCam.read(webcam_image);
				if (!webcam_image.empty()) {
					Thread.sleep(5);
					processed_image = VisionProcessing.findHighGoal(webcam_image);
					Scalar low = new Scalar(guiMain.getHueMinValue(), guiMain.getValueMinValue(),
							guiMain.getSaturationMinValue());
					Scalar high = new Scalar(guiMain.getHueMaxValue(), guiMain.getValueMaxValue(),
							guiMain.getSaturationMaxValue());

					Mat frameHSV = new Mat(640, 480, CvType.CV_8UC3);
					Imgproc.cvtColor(webcam_image, frameHSV, Imgproc.COLOR_RGB2HSV);
					Mat frame_threshed = new Mat(640, 480, CvType.CV_8UC1);
					Core.inRange(frameHSV, low, high, frame_threshed);
					Mat image2 = new Mat();
					Core.bitwise_and(frameHSV, frameHSV, image2, frame_threshed);
					Mat image3 = new Mat();
					Imgproc.cvtColor(image2, image3, Imgproc.COLOR_HSV2RGB);
					Imgproc.cvtColor(image3, imageGray, Imgproc.COLOR_RGB2GRAY);
					Imgproc.cvtColor(imageGray, webcam_image, Imgproc.COLOR_GRAY2RGB);

					List<Mat> src = Arrays.asList(webcam_image, processed_image);
					Core.hconcat(src, displayable);
					if (x == 0) {// so we don't have to resize the frame every
									// time through (performance gain).
						frame.setSize(displayable.width() + 80, displayable.height() + 120);
						x++;
					}

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
		main(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == m) {

			guiMain.main(null);
		}

	}

}
