package lunarlander.utilities;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Utilities {

	private Utilities() {

	}

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Utilities.class.getResource(path));
		} catch (IOException e) {
			System.err.println("Exception: Loading of image failed. /Cause: " + e.getLocalizedMessage());
			e.printStackTrace();
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
		return new Font(label.getFont().getName(), Font.PLAIN, fontSizeToUse - 2);
		//-2 font size bc it was always too big
	}

	public static BufferedImage bufferedImageSkalieren(BufferedImage before, double scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		// Create a new image of the proper size
		int w2 = (int) (w * scale);
		int h2 = (int) (h * scale);
		BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
		AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);

		scaleOp.filter(before, after);
		return after;
	}
}