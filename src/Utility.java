import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 
 */

/**
 * @author Nilay
 *
 */
public class Utility {
	public static Clip getSound(String fileName)
			throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		return clip;
	}

	public static BufferedImage toBufferedImage(Image img) {

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public static Image makeColorTransparent(Image im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static Color[][] getColorArrayFromImage(BufferedImage newImage) {
		int[] data = ((DataBufferInt) newImage.getRaster().getDataBuffer()).getData();
		Color[][] colors = new Color[newImage.getHeight()][newImage.getWidth()];
		for (int i = 0; i < data.length; i++) {
			Color c = new Color(data[i]);
			colors[i / newImage.getWidth()][i % newImage.getWidth()] = c;
		}
		return colors;
	}
}
