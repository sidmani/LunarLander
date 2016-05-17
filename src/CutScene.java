import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 
 */

/**
 * @author Nilay
 *
 */
public class CutScene {

	private File cutSceneImageFile;
	private BufferedImage backgroundImage, messageImage;
	private JFrame frame;
	private Clip soundEffect;

	/**
	 * 
	 */
	public CutScene(JFrame frame, File cutSceneImageFile, Clip soundEffect, BufferedImage messageImage) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		this.cutSceneImageFile = cutSceneImageFile;
		this.soundEffect = soundEffect;
		this.messageImage = messageImage;

		try {
			BufferedImage bufferedImage = ImageIO.read(new File("src/blackScreen.jpg"));
			Image image = bufferedImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
			BufferedImage newImage = Utility.toBufferedImage(image);
			Color[][] colors = Utility.getColorArrayFromImage(newImage);

			BufferedImage cutSceneImage = ImageIO.read(cutSceneImageFile);

			for (int i = (colors.length - cutSceneImage.getHeight())
					/ 2; i < ((colors.length + cutSceneImage.getHeight()) / 2); i++) {
				for (int j = (colors[0].length - cutSceneImage.getWidth())
						/ 2; j < ((colors[0].length + cutSceneImage.getWidth()) / 2); j++) {
					colors[i][j] = Color.GRAY;
				}
			}

			messageImage = Utility.toBufferedImage(messageImage);
			Color[][] otherColors = Utility.getColorArrayFromImage(messageImage);

			for (int i = (colors.length * 7 / 4 - messageImage.getHeight())
					/ 2; i < ((colors.length * 7 / 4 + messageImage.getHeight()) / 2); i++) {
				for (int j = (colors[0].length - messageImage.getWidth())
						/ 2; j < ((colors[0].length + messageImage.getWidth()) / 2); j++) {
					if (!(otherColors[i - (colors.length * 7 / 4 - messageImage.getHeight()) / 2][j
							- (colors[0].length - messageImage.getWidth()) / 2].equals(otherColors[0][0])))
						colors[i][j] = Color.GRAY;
				}
			}

			// Initialize BufferedImage, assuming Color[][] is already properly
			// populated.
			BufferedImage img = new BufferedImage(colors[0].length, colors.length, BufferedImage.TYPE_INT_RGB);

			// Set each pixel of the BufferedImage to the color from the
			// Color[][].
			for (int x = 0; x < colors.length; x++) {
				for (int y = 0; y < colors[x].length; y++) {
					img.setRGB(y, x, colors[x][y].getRGB());
				}
			}

			img = Utility.toBufferedImage(Utility.makeColorTransparent(img, Color.GRAY));

			this.backgroundImage = img;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {

		boolean toDecorate = false;

		if (!frame.isUndecorated()) {
			toDecorate = true;
			frame.setUndecorated(true);
		}

		FadeIn f = null;
		f = new FadeIn(backgroundImage, frame.getWidth() / 2, frame.getHeight() / 2, frame.getWidth(),
				frame.getHeight());
		frame.add(f);
		f.startIncrease();

		FadeIn g = null;
		try {
			BufferedImage b = ImageIO.read(cutSceneImageFile);
			g = new FadeIn(b, frame.getWidth() / 2, frame.getHeight() / 2, frame.getWidth(), frame.getHeight());
			frame.add(g);
			g.startIncrease();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FadeIn h = null;
		h = new FadeIn(messageImage, frame.getWidth() / 2, frame.getHeight() * 7 / 8, frame.getWidth(),
				frame.getHeight());
		frame.add(h);
		h.startIncrease();

		frame.setVisible(true);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		JLabel gif = new JLabel();
		ImageIcon image = new ImageIcon(cutSceneImageFile.getAbsolutePath());
		gif.setBounds((frame.getWidth() - image.getIconWidth()) / 2, (frame.getHeight() - image.getIconHeight()) / 2,
				image.getIconWidth(), image.getIconHeight());
		gif.setIcon(image);
		frame.add(gif);
		frame.setVisible(true);
		g.setVisible(false);

		frame.setVisible(true);

		soundEffect.start();

		try {
			Thread.sleep(TimeUnit.MICROSECONDS.toMillis(soundEffect.getMicrosecondLength()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frame.remove(gif);
		g.setVisible(true);
		frame.setVisible(true);

		if (h != null)
			h.startDecrease();

		if (g != null)
			g.startDecrease();

		if (f != null)
			f.startDecrease();

		if (toDecorate)
			frame.setUndecorated(false);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
