import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sid on 5/7/16.
 */
public class Stage extends JComponent {

	private int width, height;
	private final int NUMBER_OF_LINES = 3;
    private ArrayList<Point2D.Double> points = new ArrayList<>();

    public Stage(int width, int height)  {
        Random r = new Random();
	    this.width = width;
	    this.height = height;

        for (int i = 0; i <= width; i+= width / NUMBER_OF_LINES) {
            points.add(new Point2D.Double(i,height - r.nextInt((int) height/3)));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        for (int i = 0; i < points.size() - 1; i++) {
	        int[] xPoints = { (int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i };
	        int[] yPoints = {(int) this.points.get(i).getY(), (int) this.points.get(i + 1).getY(), height, height};
            g2.fillPolygon(new Polygon(xPoints, yPoints, 4));
        }

    }
}
