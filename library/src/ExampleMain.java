import org.opencv.core.*;
import org.opencv.imgcodecs.*;

public class ExampleMain {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	public static void main(String[] args) {

//		for(int i = 0; i < 5; i++) {
			int i = 0;	// i -> [0, 99]
			//Mat img  = Imgcodecs.imread("img/img/elephant" + i + ".jpg"); 
			Mat img = Imgcodecs.imread("img/elephant-crop.jpg");
			Mat templ = Imgcodecs.imread("img/pattern.jpg");

			// Squar3 result = Squar3Processor.findSquar3(img, templ); 								// Fastest, least accurate
			// Squar3 result = Squar3Processor.findHomology(img, templ, Squar3Processor.ORB); 		// Medium, medium accuracy
			Squar3 result = Squar3Processor.findHomology(img, templ); 								// Slowest, good accuracy
			Mat cropped = result.crop();

			Utils.show(img, "Before");
			Utils.show(result, "After");
			Utils.show(cropped, "Cropped");
		}
	
//	}

}