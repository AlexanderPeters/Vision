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
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.net.*;

//Class Facepanel extends the functionality of JPanel by allowing you to display Mat objects
class FacePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
    
	//Constructor to initialize the JPanel class to allow it to be extended
	public FacePanel() {
		super();
	}
	
	//Convert an OpenCV mat object to a Buffered Image for display
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

	//Draw the Buffered Image to the JPanel
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image == null) {
			return;
		}
		g.drawImage(this.image, 10, 10, this.image.getWidth(), this.image.getHeight(), null);
	}

}

//Vision happens here
class VisionProcessing {

	private static ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private static int initGui = 0;
	private static int x, y, distanceToCam, distance;
	
	//Open and read video stream then load image and find the goal
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


		if (recbr != null && rectl != null && recAreaLargest >= 4000 ){//&& 
			//recheight < recwidth * 0.75 && recwidth <= 1.75 * recheight){
			
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

	//Return the point that is at the center of two points
	private static Point goalCenter(double x1, double x2, double y1, double y2) {
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

	//Determine angular offsets and distances to the goal from the camera and implements comms
	private static void math(Point targetCenter, int imageWidth, int imageHeight, double goalWidth, double goalHeight) {
		final double REALGOALWIDTH = 20;// goal width in inches not pixels
		final double REALGOALHEIGHT = 14;// goal height in inches not pixels
		double d1, d2, distanceToCam, distance;// distance to goal in ft
		final double CAMFIELDOFVIEWHORIZANTLE = 60;//Horizantle FOV of a microsoft lifecam 3000 in degrees
		final double CAMFIELDOFVIEWVERTICAL = 34;//Vertical FOV of a microsoft lifecam 3000 int degrees
		final double GOALHEIGHT = 8.0833; //Hiehgt to center of goal from floor in ft
		double xOffsetFt;// width offset in width
		double xPixOffsetFromCenter = 0;//ft to left or right of robot times scalar (- value if offset left of center line + if offset to the right)
		double yOffsetFt;// height offset in ft
		double xScalar, yScalar;
		//final double CAMOFFSET = -11.75; //Offset from center of robot in inches.
		//double pixelCamOffset;
		
		//table = NetworkTable.getTable("datatable");
		
		if (targetCenter.x != -1 && targetCenter.y != -1 && goalWidth != -1 && goalHeight != -1) {
			//pixelCamOffset = CAMOFFSET*(goalWidth / REALGOALWIDTH);
			
			d1 = (REALGOALWIDTH / 12 * imageWidth) / (2 * goalWidth * Math.tan(CAMFIELDOFVIEWHORIZANTLE));
			System.out.println("d1 " + d1);
			d2 = (REALGOALHEIGHT / 12 * imageHeight) / (2 * goalHeight * Math.sin(CAMFIELDOFVIEWVERTICAL));
			System.out.println("d2 " + d2);
			
			distanceToCam = (d1 + d2) / 2;
			distance = Math.asin(GOALHEIGHT/distanceToCam);
			
			xScalar = (2*distanceToCam*Math.tan(CAMFIELDOFVIEWHORIZANTLE))/imageWidth;
			yScalar = (2*distanceToCam*Math.tan(CAMFIELDOFVIEWVERTICAL))/imageHeight;
			
			System.out.println(distanceToCam);
			VisionProcessing.distanceToCam = (int) Math.round(distanceToCam);
			
			xOffsetFt = (targetCenter.x - (imageWidth / 2 - xPixOffsetFromCenter)) * xScalar;
			yOffsetFt = (targetCenter.y - imageHeight / 2) * yScalar;
			
			xOffset(distanceToCam, xOffsetFt);
			yOffset(distanceToCam, yOffsetFt);
			try {
				comms(VisionProcessing.x, VisionProcessing.y, VisionProcessing.distanceToCam);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Calculate x angular offset
	private static void xOffset(double distanceToCam, double xOffsetFt) {
		// System.out.print("x " + xOffsetFt + " ");
		
		// negative angles to account for positive offsets
		double x = Math.toDegrees(Math.atan(xOffsetFt / distanceToCam));
		//System.out.print("X " + x);
		VisionProcessing.x = (int) Math.round(x);
		System.out.println("X " + Math.round(x));
		
	}

	//Calculate y angular offset                                
	private static void yOffset(double distanceToCam, double yOffsetFt) {
		// System.out.println("y " + yOffsetFt);// + " " + "dist " + distance);
		int yOffset = 28;//angle offset of cam to  line of fire ( + if cam above center line of fire) (- if below center line)
		
		// negative angles to account for positive offsets
		double y = Math.toDegrees(Math.atan(yOffsetFt / distanceToCam));
		//System.out.println(" Y " + y);
		VisionProcessing.y = (int) Math.round(y) + yOffset;
		System.out.println("Y " + accountForG(VisionProcessing.y));
		
	}
	private static double accountForG(int visionAngle){
		final double SHOOTERSPEED = 29.78;//Magnitude of velocity in ft/s
		double accountForGravityAngle = Math.atan((SHOOTERSPEED * Math.sin(visionAngle) - 16) / VisionProcessing.distance);
		return visionAngle + accountForGravityAngle;
	}
	
	
	//https://systembash.com/a-simple-java-udp-server-and-udp-client/
	//Create a udp datastream and send the data to the Rio
	private static void comms(int x,int y,int distance) throws IOException {
		String str = new String();
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("10.31.40.24");
		byte[] sendData = new byte[1024];
		
		String sentence = str.concat(String.valueOf(x) + "," 
				+ String.valueOf(y) + "," + String.valueOf(distance) + ",Last");
		sendData = sentence.getBytes();
		//System.out.println(sendData);
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 12347);
		clientSocket.send(sendPacket);
		clientSocket.close();
	}
}

//Main class for vision program
public class Main implements ActionListener {
	JButton optionsButton = new JButton("Options");
	static boolean mycontinue = false;
	
	//Main class constructor to set everything up
	public static void main(String[] args) throws InterruptedException, IOException {
		@SuppressWarnings("unused")
		Main main = new Main();
	}

	//Main method that loads the OpenCV libraries and runs the program
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
						Thread.sleep(10);
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
	//Run guimain when the options button is pressed
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == optionsButton) {
			guiMain.main(null);
		}

	}

}
