import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Squar3 extends Mat {
	double tlX, tlY;
	double blX, blY;
	double trX, trY;
	double brX, brY;

	public Squar3(Mat img, double tlX, double tlY, double blX, double blY, double trX, double trY) {
		img.copyTo(this);
		this.tlX = tlX;
		this.tlY = tlY;
		this.blX = blX;
		this.blY = blY;
		this.trX = trX;
		this.trY = trY;

		// TODO: Adapt for rotated rectangles? Or will parallels work
		brX = blX + Math.abs(trX - tlX);
		brY = trY + Math.abs(blY - tlY); 
	}
	public Mat crop() {
		MatOfPoint2f points = new MatOfPoint2f(new Point(blX, blY), new Point(tlX, tlY), new Point(trX, trY), new Point(brX, brY));
		RotatedRect rrect = Imgproc.minAreaRect(points);

		double angle = rrect.angle;
		Size rect_size = rrect.size;
		if (rrect.angle < -45.) {
			angle += 90.0;
			double tmp = rect_size.height;
			rect_size.height = (rect_size.width);
			rect_size.width = tmp;
		}
		Mat m = Imgproc.getRotationMatrix2D(rrect.center, angle, 1);
		Mat rotated = new Mat();
		Imgproc.warpAffine(this, rotated, m, size(), Imgproc.INTER_CUBIC);
		Mat cropped = new Mat();
		Imgproc.getRectSubPix(rotated, rect_size, rrect.center, cropped);
		
		return cropped;
	}
	public String toString() {
		return "Squar3 - {\n" 
				+ "\tTL: (" + tlX + ", " + tlY + ")\n" 
				+ "\tTR: (" + trX + ", " + trY + ")\n" 
				+ "\tBL: (" + blX + ", " + blY + ")\n" 
				+ "\tBR: (" + brX + ", " + brY + ") [calc]\n" 
				+ "}";
	}
}