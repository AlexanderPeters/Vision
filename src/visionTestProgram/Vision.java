package visionTestProgram;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import visionTestProgram.FacePanel;

public class Vision {
	private static double time = 0.0;
	private static double timeAvg = 0.0;
	private static double fps = 0.0;
	private static double frameCount;
	private static double sysProcessStartTime;
	private static boolean resize;
	public static void vision(Mat m, FacePanel facePanel, JFrame frame, Boolean withFPS) throws InterruptedException {
		DecimalFormat format = new DecimalFormat("0.##");	
		double sysProcessEndTime;
		
		
		//System.out.println("FPS " + webCam.get(CV_CAP_PROP_FPS));
		Mat displayable = new Mat();
		Mat frameHSV = new Mat(640, 480, CvType.CV_8UC3);
		Mat frame_threshed = new Mat(640, 480, CvType.CV_8UC1);
		Mat imageHSV_threshed = new Mat();
		Mat imageRGB_threshed = new Mat();
		Mat imageGray = new Mat();
		Mat processed_image = new Mat();
		
		frameCount = Main.getFrameCount();
		sysProcessStartTime = Main.getSysProcessStartTime();

		processed_image = VisionProcessing.findHighGoal(m);
		
		Scalar low = new Scalar(guiMain.getHueMinValue(), guiMain.getValueMinValue(),
				guiMain.getSaturationMinValue());
		Scalar high = new Scalar(guiMain.getHueMaxValue(), guiMain.getValueMaxValue(),
				guiMain.getSaturationMaxValue());

		Imgproc.cvtColor(m, frameHSV, Imgproc.COLOR_RGB2HSV);

		Core.inRange(frameHSV, low, high, frame_threshed);
		Core.bitwise_and(frameHSV, frameHSV, imageHSV_threshed, frame_threshed);

		Imgproc.cvtColor(imageHSV_threshed, imageRGB_threshed, Imgproc.COLOR_HSV2RGB);
		Imgproc.cvtColor(imageRGB_threshed, imageGray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(imageGray, m, Imgproc.COLOR_GRAY2RGB);
		
		if(withFPS)//Disable FPS display when in debug mode because, it is inaccurate
			Core.putText(processed_image, "FPS: " + format.format(fps), new Point (processed_image.rows()/16,
					processed_image.cols()/14), Core.FONT_HERSHEY_DUPLEX, new Double (1), new Scalar(255));
	
		List<Mat> src = Arrays.asList(m, processed_image);
		Core.hconcat(src, displayable);
							
		if (resize) // so we don't have to resize the frame every time through (performance gain).
			frame.setSize(displayable.width() + 80, displayable.height() + 120);						
		
		if(!displayable.empty()){
			facePanel.matToBufferedImage(displayable);
			facePanel.repaint();
			//Thread.sleep(10);
		}
		sysProcessEndTime = System.currentTimeMillis();
		time += sysProcessEndTime - sysProcessStartTime;
		
		if(frameCount %5 == 0) {
			timeAvg = time / 5;
			time = 0; //Reset time count after averaging 5 frames
		}
		else
			timeAvg = time / (frameCount %5);
		
		fps = 1000 / timeAvg;
		//Note that FPS counter is a little stringy but, I did it this way because, I might need a frame
		//counter later so this is probably better in the long run instead of just using a 1-5 counter
		
	}
	public static void setResize(boolean var) {
		resize = var;
	}
}
