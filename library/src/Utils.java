import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

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
		JFrame frame = new JFrame();
		frame.setLocation(x += dx, y += dy);
		frame.getContentPane().add(new JLabel(new ImageIcon(toBufferedImage(img))));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void show(Mat img, String title) {
		JFrame frame = new JFrame(title);
		frame.getContentPane().add(new JLabel(new ImageIcon(toBufferedImage(img))));
		frame.setLocation(x += dx, y += dy);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	private static HashMap<String, JFrame> frames = new HashMap<String, JFrame>();
	public static void frame(Mat img, String title) {
		if(!frames.containsKey(title)) {
			JFrame tmp = new JFrame(title);
			tmp.setLocation(x += dx, y += dy);
			tmp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frames.put(title, tmp);
		}


		JFrame frame = frames.get(title);
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new JLabel(new ImageIcon(toBufferedImage(img))));
		frame.pack();
		frame.setVisible(true);
	}
	public static BufferedImage toBufferedImage(Mat img){
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;

		try {
			bufImage = ImageIO.read(new ByteArrayInputStream(byteArray));
		} catch (Exception e) { e.printStackTrace(); }

		return bufImage;

	}
}
