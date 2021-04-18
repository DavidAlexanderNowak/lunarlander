import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utilities {
	// lädt ein Bild -- creds an Moritz (ab hier dunkle Magie)
    public static BufferedImage ladeBild(String path) {
    	try {
    		return ImageIO.read(Utilities.class.getResource(path));
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	return null;
    }
}