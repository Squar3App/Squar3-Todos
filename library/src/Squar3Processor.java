import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;

public class Squar3Processor {

	static class Squar3Point implements Comparable {
		double x, y;

		@Override public int compareTo(Object o) {
			Squar3Point o2 = (Squar3Point) o;
			if((int)(x - o2.x) != 0) return (int) (x - o2.x);
			return (int) (y - o2.y);
		}
		@Override public String toString() {
			return "[" + x + ", " + y + "]";
		}
		
	}
	
	public static Squar3 findFiducial(Mat img, Mat templ) {
		Mat img2 = new Mat();
		img.copyTo(img2);
		
		int result_cols =  img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		int match_method = Imgproc.TM_CCOEFF;

		Mat result = new Mat(result_cols, result_rows, CvType.CV_32FC1);
		Imgproc.matchTemplate(img2, templ, result, match_method);
		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
		
		MinMaxLocResult mmr;
		Point loc;
		
		Squar3Point[] pts = new Squar3Point[3];
		
		for(int i = 0; i < 3; i++){
			mmr = Core.minMaxLoc(result);
			loc = mmr.maxLoc;
			
			Squar3Point pt = new Squar3Point();
			pt.x = loc.x + templ.cols() / 2.;
			pt.y = loc.y + templ.rows() / 2.;
			pts[i] = pt;
						
			Point bL = new Point(loc.x - templ.cols(), loc.y +templ.rows());
			Point bR = new Point(loc.x + templ.cols(), loc.y + templ.rows());
			Point tL = new Point(loc.x - templ.cols(), loc.y - templ.rows());
			Point tR = new Point(loc.x + templ.cols(), loc.y - templ.rows());
			
			Imgproc.rectangle(img2, loc, bR, new Scalar(0, 0, 255));
			ArrayList<MatOfPoint> arr = new ArrayList<MatOfPoint>() {{ add(new MatOfPoint(bL, bR, tR, tL)); }};
			Imgproc.fillPoly(result, arr, new Scalar(0));;
		}
		Arrays.sort(pts);
		return new Squar3(img2, pts[0].x, pts[0].y, pts[1].x, pts[1].y, pts[2].x, pts[2].y);
	}
}