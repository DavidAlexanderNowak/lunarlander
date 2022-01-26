import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Utilities {
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Utilities.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public static Font createFont(JLabel label) {
		int stringWidth = label.getFontMetrics(label.getFont())//
				.stringWidth(label.getText());
		int componentWidth = label.getWidth();
		double widthRatio = (double) componentWidth / (double) stringWidth;
		int fontSizeToUse = Math.min(//
				(int) (label.getFont().getSize() * widthRatio), label.getHeight());
		return new Font(label.getFont().getName(), Font.PLAIN, fontSizeToUse);
	}
}