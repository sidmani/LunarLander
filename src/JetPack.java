import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nilay on 5/7/16.
 */
public class JetPack extends JComponent {

	protected BufferedImage jetPackImage;
	private ArrayList<Point2D.Double> values;
	private int count = 0;
	private boolean goingUp = true;

	public JetPack(ArrayList<Point2D.Double> values) {
		Random r = new Random();
		for (int i = 0; i < values.size(); i++) {
			if (r.nextBoolean()) {
				values.remove(i);
			}
		}

		for (int i = 0; i < values.size() - 1; i++) {
			if (Point2D.distance(values.get(i).getX(), values.get(i).getY(), values.get(i + 1).getX(), values.get(i + 1).getY()) > 300) {
				values.add(i + 1, new Point2D.Double((values.get(i).getX() + values.get(i + 1).getX()) / 2, (values.get(i).getY() + values.get(i + 1).getY()) / 2));
			}
		}

		this.values = values;

		try {
			jetPackImage = ImageIO.read(new File("src/jetPack.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if ((count %= 5) == 0)  {
			goingUp = !goingUp;
		}

		for (int i = 0; i < values.size(); i++) {
			AffineTransform t = new AffineTransform();
			t.translate(values.get(i).getX(), values.get(i).getY() + count * ((goingUp) ? 1 : -1));

			g2.drawImage(jetPackImage, t, this);
		}
	}
}
