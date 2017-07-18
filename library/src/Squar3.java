import org.opencv.core.Mat;
import org.opencv.core.Rect;

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
		
		// TODO: Handle rotated calculations
		brX = blX + Math.abs(trX - tlX);
		brY = trY - Math.abs(tlY - blY); 
	}
	
	public Mat crop() {
		// TODO: Handle rotated crops
		Rect rectCrop = new Rect((int)tlX, (int)tlY , (int)Math.abs(brX - tlX + 1), (int)Math.abs(brY - tlY + 1));
		return submat(rectCrop);
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