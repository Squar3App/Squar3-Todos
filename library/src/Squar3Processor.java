import java.util.*;


import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.*;

public class Squar3Processor {

	static class Squar3Point implements Comparable<Squar3Point> {
		double x, y;
		@Override public int compareTo(Squar3Point o2) { if((int)(x - o2.x) != 0) return (int) (x - o2.x); return (int) (y - o2.y); }
		@Override public String toString() { return "[" + x + ", " + y + "]"; }
	}

	public static Squar3 findSquar3(Mat img, Mat templ) {
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

	// TODO: Fix SURF and SIFT - See https://stackoverflow.com/questions/30657774/surf-and-sift-algorithms-doesnt-work-in-opencv-3-0-java
	// TODO: Cite Lungesoft/computer-vision	
	// ANALYZE: ORB finds three areas near fiducials, BRISK finds one really really well. Bruteforce_Hamming vs. Bruteforce_Hamminglut?
	// TODO: Improve speed!
	public static Squar3 findHomology(Mat imgScene, Mat imgObject) {
		Mat img = new Mat();
		imgScene.copyTo(img);
		
		Squar3Point[] pts = new Squar3Point[3];
		
		for(int ct = 0; ct < 3; ct++) {
			FeatureDetector detector = FeatureDetector.create(FeatureDetector.BRISK);

			MatOfKeyPoint keypointsObject = new MatOfKeyPoint();
			MatOfKeyPoint keypointsScene = new MatOfKeyPoint();

			detector.detect(imgObject, keypointsObject);
			detector.detect(img, keypointsScene);

			DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);

			Mat descriptorObject = new Mat();
			Mat descriptorScene = new Mat();

			extractor.compute(imgObject, keypointsObject, descriptorObject);
			extractor.compute(img, keypointsScene, descriptorScene);

			DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
			MatOfDMatch matches = new MatOfDMatch();

			matcher.match(descriptorObject, descriptorScene, matches);
			List<DMatch> matchesList = matches.toList();

			Double maxDist = 0.0;
			Double minDist = 100.0;

			for (int i = 0; i < descriptorObject.rows(); i++) {
				Double dist = (double) matchesList.get(i).distance;
				if (dist < minDist) minDist = dist;
				if (dist > maxDist) maxDist = dist;
			}

			LinkedList<DMatch> goodMatches = new LinkedList<>();
			MatOfDMatch gm = new MatOfDMatch();

			for (int i = 0; i < descriptorObject.rows(); i++) {
				if (matchesList.get(i).distance < minDist * 4) {
					goodMatches.addLast(matchesList.get(i));
				}
			}

			gm.fromList(goodMatches);

			Mat imgMatches = new Mat();
			Features2d.drawMatches(
					imgObject,
					keypointsObject,
					img,
					keypointsScene,
					gm,
					imgMatches,
					new Scalar(255, 0, 0),
					new Scalar(0, 0, 255),
					new MatOfByte(),
					2);

			LinkedList<Point> objList = new LinkedList<>();
			LinkedList<Point> sceneList = new LinkedList<>();

			List<KeyPoint> keypointsObjectList = keypointsObject.toList();
			List<KeyPoint> keypointsSceneList = keypointsScene.toList();

			for (DMatch goodMatcher : goodMatches) {
				objList.addLast(keypointsObjectList.get(goodMatcher.queryIdx).pt);
				sceneList.addLast(keypointsSceneList.get(goodMatcher.trainIdx).pt);
			}

			MatOfPoint2f obj = new MatOfPoint2f();
			obj.fromList(objList);

			MatOfPoint2f scene = new MatOfPoint2f();
			scene.fromList(sceneList);

			Mat h = Calib3d.findHomography(obj, scene, 8, 10);

			Mat objCorners = new Mat(4, 1, CvType.CV_32FC2);
			Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);

			objCorners.put(0, 0, new double[] {0,0});
			objCorners.put(1, 0, new double[] {imgObject.cols(),0});
			objCorners.put(2, 0, new double[] {imgObject.cols(),imgObject.rows()});
			objCorners.put(3, 0, new double[] {0,imgObject.rows()});

			Core.perspectiveTransform(objCorners, sceneCorners, h);

			Squar3Point pt = new Squar3Point();
			pt.x = (new Point(sceneCorners.get(0, 0)).x + new Point(sceneCorners.get(1, 0)).x + new Point(sceneCorners.get(2, 0)).x + new Point(sceneCorners.get(3, 0)).x) / 4;
			pt.y = (new Point(sceneCorners.get(0, 0)).y + new Point(sceneCorners.get(1, 0)).y + new Point(sceneCorners.get(2, 0)).y + new Point(sceneCorners.get(3, 0)).y) / 4;
			pts[ct] = pt;	
			
			ArrayList<MatOfPoint> arr = new ArrayList<MatOfPoint>() {{ 
				add(new MatOfPoint(new Point(sceneCorners.get(0,0)), new Point(sceneCorners.get(1,0)), new Point(sceneCorners.get(2,0)), new Point(sceneCorners.get(3,0))));
			}};
			Imgproc.fillPoly(img, arr, new Scalar(0, 225, 0));;
		}
		Arrays.sort(pts);
		return new Squar3(img, pts[0].x, pts[0].y, pts[1].x, pts[1].y, pts[2].x, pts[2].y);
	}

}