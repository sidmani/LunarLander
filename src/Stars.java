import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Sid on 5/7/16.
 */
public class Stars extends JComponent {
	ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();

	public Stars(int num, int width, int height) {
		this.setBounds(this.getX(), this.getY(), width, height);
		for (int i = 0; i < num; i++) {
			points.add(new Point2D.Double(Math.random() * width, Math.random() * height));
		}
	}

	public void paintComponent(Graphics g) {
		Graphics g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		for (Point2D.Double p : points) {
			g2.fillOval((int) p.x, (int) p.y, 4, 4);
		}
	}
}
