import org.opencv.core.*;
import org.opencv.imgcodecs.*;

public class Main {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	public static void main(String[] args) {
		
		Mat img  = Imgcodecs.imread("img/elephant-crop.jpg");
		Mat templ = Imgcodecs.imread("img/pattern.jpg");
		
//		Squar3 result = Squar3Processor.findSquar3(img, templ); 	// Faster
		Squar3 result = Squar3Processor.findHomology(img, templ); 	// Slower, handles rotated
		Mat cropped = result.crop();
		
		Utils.show(img, "Before");
		Utils.show(result, "After");
		Utils.show(cropped, "Cropped");
	}	
}