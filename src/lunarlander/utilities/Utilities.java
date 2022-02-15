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

	/**
	 * returns a {@link Font} that makes the JLabel's text fit its size
	 * 
	 * @param label
	 * @return fitting {@link Font}
	 */
	public static Font createFittingFont(JLabel label) {
		double widthRatio = (double) label.getWidth() / //
				(double) label.getFontMetrics(label.getFont()).stringWidth(label.getText());
		int fontSizeToUse = Math.min(//
				(int) (label.getFont().getSize() * widthRatio), label.getHeight());
		return new Font(label.getFont().getName(), Font.PLAIN, fontSizeToUse);
	}

	/**
	 * return a scaled {@link BufferedImage}
	 * 
	 * @param bufferedImage {@link BufferedImage} to be scaled
	 * @param scale         scale
	 * @return {@link BufferedImage} scaled correctly
	 */
	public static BufferedImage scaleImage(BufferedImage bufferedImage, double scale) {
		BufferedImage resultingBufferedImage = new BufferedImage((int) (bufferedImage.getWidth() * scale),
				(int) (bufferedImage.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);
		scaleOp.filter(bufferedImage, resultingBufferedImage);
		return resultingBufferedImage;
	}
}