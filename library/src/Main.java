import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;

public class Main {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	public static void main(String[] args) {

		Mat img  = Imgcodecs.imread("img/elephant-crop.jpg");
		Mat templ = Imgcodecs.imread("img/pattern.jpg");

		int result_cols =  img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		int match_method = Imgproc.TM_CCOEFF;


		Mat result = new Mat(result_cols, result_rows, CvType.CV_32FC1);
		Imgproc.matchTemplate(img, templ, result, match_method);
		//Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

		Utils.show(img, "Before");
		
		MinMaxLocResult mmr;
		Point loc;
		
		for(int i = 0; i < 3; i++){
			mmr = Core.minMaxLoc(result);
			loc = mmr.maxLoc;
			
			Point bL = new Point(loc.x - templ.cols(), loc.y +templ.rows());
			Point bR = new Point(loc.x + templ.cols(), loc.y + templ.rows());
			Point tL = new Point(loc.x - templ.cols(), loc.y - templ.rows());
			Point tR = new Point(loc.x + templ.cols(), loc.y - templ.rows());
			
			Imgproc.rectangle(img, loc, bR, new Scalar(0, 0, 255));
			ArrayList<MatOfPoint> arr = new ArrayList<MatOfPoint>() {{ add(new MatOfPoint(bL, bR, tR, tL)); }};
			Imgproc.fillPoly(result, arr, new Scalar(0));;
			
			System.out.println(mmr.maxVal);
		}
		
		Utils.show(img, "After");
	}	
}