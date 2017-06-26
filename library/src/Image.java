import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class Image extends BufferedImage {

	/*** Fields ***/
	
	
	/*** Implicit Constructors ***/
	public Image(int width, int height, int imageType) {
		super(width, height, imageType);
	}
	public Image(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
	}	
	public Image(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
	}

	
	/*** Copy Constructors ***/
	public Image(BufferedImage bi) {
		super(bi.getColorModel(), bi.copyData(bi.getRaster().createCompatibleWritableRaster()), bi.isAlphaPremultiplied(), null);
	}
	
	
	/*** Methods ***/
	public PositionMarker findPositionMarker() {
		// TODO
		return null;
	}
	public void transform(PositionMarker pm) {
		// TODO
	}
	
}
