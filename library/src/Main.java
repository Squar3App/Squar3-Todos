import java.io.*;
import javax.imageio.*;
import javax.swing.*;


public class Main {

	public static void main(String[] args) {
		Image input = load("img/elephant-crop.jpg");
		display(input);
		
		PositionMarker pm = input.findPositionMarker();
		input.transform(pm);
		
	}
	
	public static Image load(String filepath) {
		return load(new File(filepath));
	}
	public static Image load(File file) {
		try {
			return new Image(ImageIO.read(file));
		} 
		catch (IOException e) { e.printStackTrace(); }
		return null;
	}

	public static void display(Image img) {
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(img)));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}