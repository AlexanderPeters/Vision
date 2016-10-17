package visionTestProgram;

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
import java.net.*;


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
		if (this.image == null) {
			return;
		}
		g.drawImage(this.image, 10, 10, this.image.getWidth(), this.image.getHeight(), null);
	}

}

class VisionProcessing {

	public static ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	public static int initGui = 0;
	static int x, y, distance;
	
	
	//static NetworkTable table;

	public static Mat findHighGoal(Mat m) throws InterruptedException {

		Mat hierarchy = new Mat();
		Mat imageHSV_threshed = new Mat();
		Mat imageRGB_threshed = new Mat();
		Mat imageGray = new Mat();
		Mat output = new Mat();
		Mat frameHSV = new Mat(640, 480, CvType.CV_8UC3);
		Mat frame_threshed = new Mat(640, 480, CvType.CV_8UC1);

		Scalar low = new Scalar(guiMain.getHueMinValue(), guiMain.getValueMinValue(), guiMain.getSaturationMinValue());
		Scalar high = new Scalar(guiMain.getHueMaxValue(), guiMain.getValueMaxValue(), guiMain.getSaturationMaxValue());

		double recAreaLargest = 0;
		Point recbr = null;
		Point rectl = null;

		int recwidth = 0, recheight = 0;
		int imagewidth, imageheight;

		if (initGui == 0) {
			guiMain.main(null);// call gui once at start to get initialization values
			initGui++;
		}

		Imgproc.cvtColor(m, frameHSV, Imgproc.COLOR_RGB2HSV);
		imagewidth = m.cols();
		imageheight = m.rows();

		Core.inRange(frameHSV, low, high, frame_threshed);
		Core.bitwise_and(frameHSV, frameHSV, imageHSV_threshed, frame_threshed);

		Imgproc.cvtColor(imageHSV_threshed, imageRGB_threshed, Imgproc.COLOR_HSV2RGB);
		Imgproc.cvtColor(imageRGB_threshed, imageGray, Imgproc.COLOR_RGB2GRAY);

		Imgproc.findContours(imageGray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

		for (Iterator<org.opencv.core.MatOfPoint> iterator = contours.iterator(); iterator.hasNext();) {

			org.opencv.core.MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
			Rect rec = Imgproc.boundingRect(matOfPoint);

			if (rec.area() >= recAreaLargest) {
				recAreaLargest = rec.area();
				recbr = rec.br();
				rectl = rec.tl();
				recwidth = rec.width;
				recheight = rec.height;

			}
			iterator.remove();

		}

		if (recbr != null && rectl != null && recAreaLargest >= 4000 && 
			recheight < recwidth * 0.75 && recwidth <= 1.75 * recheight){
			
			//System.out.println(recAreaLargest);
			Core.rectangle(imageGray, recbr, rectl, new Scalar(255, 255, 255));
			math(goalCenter(rectl.x, recbr.x, rectl.y, recbr.y), imagewidth, imageheight, recwidth, recheight);
			recAreaLargest = 0;
			recbr = null;
			rectl = null;
			recwidth = 0;
			recheight = 0;

		} else {
			math(goalCenter(-1, -1, -1, -1), imagewidth, imageheight, -1, -1);
		}

		Imgproc.cvtColor(imageGray, output, Imgproc.COLOR_GRAY2RGB);

		return output;

	}

	public static Point goalCenter(double x1, double x2, double y1, double y2) {
		Point centerPoint = new Point();
		if (x1 != -1 && x2 != -1 && y1 != -1 && y1 != -1) {

			centerPoint.x = (x2 + x1) / 2;
			centerPoint.y = (y2 + y1) / 2;

		} else {
			centerPoint.x = -1;
			centerPoint.y = -1;

		}
		return centerPoint;

	}

	public static void math(Point targetCenter, int imageWidth, int imageHeight, double goalWidth, double goalHeight) {
		double realGoalWidth = 20;// goal width in inches not pixels
		double realGoalHeight = 14;// goal height in inches not pixels
		double distance;// distance to goal in ft
		double camFieldOfView = 60;// FOV of a microsoft lifecam 3000 in degrees
		double xOffsetFt;// width offset in width
		double yOffsetFt;// height offset in ft
		
		//table = NetworkTable.getTable("datatable");
		
		if (targetCenter.x != -1 && targetCenter.y != -1 && goalWidth != -1 && goalHeight != -1) {
			
			distance = realGoalWidth / 12 * imageWidth / (2 * goalWidth * Math.tan(camFieldOfView));
			VisionProcessing.distance = (int) Math.round(distance);
			
			xOffsetFt = (targetCenter.x - imageWidth / 2) / (goalWidth / realGoalWidth);
			yOffsetFt = (imageHeight / 2 - targetCenter.y) / (goalHeight / realGoalHeight);
			
			xOffset(distance, xOffsetFt);
			yOffset(distance, yOffsetFt);
			try {
				comms(VisionProcessing.x, VisionProcessing.y, VisionProcessing.distance);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void xOffset(double distance, double xOffsetFt) {
		// System.out.print("x " + xOffsetFt + " ");
		
		// negative angles to account for positive offsets
		double x = -Math.toDegrees(Math.atan(xOffsetFt / distance));
		//System.out.print("X " + x);
		VisionProcessing.x = (int) Math.round(x);
		
	}

	public static void yOffset(double distance, double yOffsetFt) {
		// System.out.println("y " + yOffsetFt);// + " " + "dist " + distance);
		
		// negative angles to account for positive offsets
		double y = -Math.toDegrees(Math.atan(yOffsetFt / distance));
		//System.out.println(" Y " + y);
		VisionProcessing.y = (int) Math.round(y);
		
	}
	//https://systembash.com/a-simple-java-udp-server-and-udp-client/
	public static void comms(int x,int y,int distance) throws IOException {
		String str = new String();
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("192.168.2.8");
		byte[] sendData = new byte[1024];
		
		String sentence = str.concat(String.valueOf(x) + "," 
				+ String.valueOf(y) + "," + String.valueOf(distance) + ",Last");
		sendData = sentence.getBytes();
		//System.out.println(sendData);
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 12346);
		clientSocket.send(sendPacket);
		clientSocket.close();
	}
}

public class Main implements ActionListener {
	JButton optionsButton = new JButton("Options");
	static boolean mycontinue = false;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		@SuppressWarnings("unused")
		Main main = new Main();
	}

	public Main() throws InterruptedException, IOException {
		//System.load(Core.NATIVE_LIBRARY_NAME);
		//System.load("opencv_java2413");
		System.out.println(OSValidator.getOS());
		if(OSValidator.isWindows()){
			System.out.println("Is windows!!");
			Runtime.getRuntime().loadLibrary("opencv_java2413");
			//Runtime.getRuntime().loadLibrary("NetworkTables");
		
		}
		else if(OSValidator.isUnix()){
			System.out.println("Is Linux!!");
			Runtime.getRuntime().loadLibrary("libopencv_core");
			
		}
				
		JFrame frame = new JFrame("WebCam Capture - FRC Vision");
		JPanel mainPanel = new JPanel();

		optionsButton.addActionListener(this);

		FacePanel facePanel = new FacePanel();
		frame.setLayout(new BorderLayout());
		frame.setSize(400, 400);
		frame.setBackground(Color.BLUE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel.add(optionsButton);

		frame.add(facePanel, BorderLayout.CENTER);
		frame.add(mainPanel, BorderLayout.EAST);
		frame.setVisible(true);

		Mat webcam_image = new Mat();
		Mat imageGray = new Mat();
		Mat processed_image = new Mat();

		VideoCapture webCam = new VideoCapture(0);

		int i = 0;
		
		if (webCam.isOpened()) {
			//Half a second works just fine for init on more powerful 
			//computers 700ms necessary for kangaroo computer
			Thread.sleep(700);
			while (true) {
				Mat displayable = new Mat();
				Mat frameHSV = new Mat(640, 480, CvType.CV_8UC3);
				Mat frame_threshed = new Mat(640, 480, CvType.CV_8UC1);
				Mat imageHSV_threshed = new Mat();
				Mat imageRGB_threshed = new Mat();			

				webCam.read(webcam_image);
				if (!webcam_image.empty()) {
					//Thread.sleep(5);

					processed_image = VisionProcessing.findHighGoal(webcam_image);

					Scalar low = new Scalar(guiMain.getHueMinValue(), guiMain.getValueMinValue(),
							guiMain.getSaturationMinValue());
					Scalar high = new Scalar(guiMain.getHueMaxValue(), guiMain.getValueMaxValue(),
							guiMain.getSaturationMaxValue());

					Imgproc.cvtColor(webcam_image, frameHSV, Imgproc.COLOR_RGB2HSV);

					Core.inRange(frameHSV, low, high, frame_threshed);
					Core.bitwise_and(frameHSV, frameHSV, imageHSV_threshed, frame_threshed);

					Imgproc.cvtColor(imageHSV_threshed, imageRGB_threshed, Imgproc.COLOR_HSV2RGB);
					Imgproc.cvtColor(imageRGB_threshed, imageGray, Imgproc.COLOR_RGB2GRAY);
					Imgproc.cvtColor(imageGray, webcam_image, Imgproc.COLOR_GRAY2RGB);

					List<Mat> src = Arrays.asList(webcam_image, processed_image);
					Core.hconcat(src, displayable);

					if (i == 0) {// so we don't have to resize the frame every
								 // time through (performance gain).
						frame.setSize(displayable.width() + 80, displayable.height() + 120);
						i++;
					}
					if(!displayable.empty()){
						facePanel.matToBufferedImage(displayable);
						facePanel.repaint();
					}
					
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

		if (e.getSource() == optionsButton) {
			guiMain.main(null);
		}

	}

}
