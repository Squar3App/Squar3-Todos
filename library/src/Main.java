import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class Main {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	public static void main(String[] args) {

		Mat img  = Imgcodecs.imread("img/elephant-crop.jpg");
		Mat templ = Imgcodecs.imread("img/pattern.jpg");

		Squar3 result = Squar3Processor.findFiducial(img, templ);
		System.out.println(result);
		Mat x = result.crop();
		
		Utils.show(img, "Before");
		Utils.show(result, "After");
		Utils.show(x, "Cropped");
	}	
}