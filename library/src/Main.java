import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class Main {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	public static void main(String[] args) throws InterruptedException {
		VideoCapture camera = new VideoCapture(0);
		Mat templ = Imgcodecs.imread("img/pattern.jpg");


		Mat frame = new Mat();
		while(camera.grab()) {
			camera.retrieve(frame);

			// Edited method to be realtime-efficient
			Squar3 result = Squar3Processor.findSquar3(frame, templ);
			Mat cropped = result.crop();


			Utils.frame(result, "Frame");
			Thread.sleep(5); // Prevent webcam overuse
		}
	}
}
