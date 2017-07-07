import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Utils {
	private static int x = 0, y = 0, dx = 25, dy = 25;
	
	public static void show(Mat img) {
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		
		try {
			bufImage = ImageIO.read(new ByteArrayInputStream(byteArray));
			JFrame frame = new JFrame();
			frame.setLocation(x += dx, y += dy);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
	}
	public static void show(Mat img, String title) {
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		
		try {
			bufImage = ImageIO.read(new ByteArrayInputStream(byteArray));
			JFrame frame = new JFrame(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.setLocation(x += dx, y += dy);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) { e.printStackTrace(); }
	}
}
