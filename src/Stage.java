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

	private final int NUMBER_OF_LINES = 9;
    private ArrayList<Point2D.Double> points = new ArrayList<>();
	private ArrayList<Polygon> ground = new ArrayList<>();
	private ArrayList<Polygon> sky = new ArrayList<>();

    public Stage(int width, int height, int difficulty)  {
	    setBounds(getX(), getY(), width, height);

	    int newCenter = height/2;

        Random r = new Random();

		int tunnelSize = (int) (newCenter * (1 - difficulty/100));

        for (int i = 0; i <= width; i+= width / this.NUMBER_OF_LINES) {
            this.points.add(new Point2D.Double(i,height - r.nextInt((int) tunnelSize)));
        }

	    for (int i = 0; i < this.points.size() - 1; i++) {
		    int[] xPoints = { (int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i };
		    int[] yPoints = {(int) this.points.get(i).getY() - newCenter/2, (int) this.points.get(i + 1).getY() -newCenter/2, height, height};
		    this.ground.add(new Polygon(xPoints, yPoints, 4));
	    }

	    for (int i = 0; i < this.points.size() - 1; i++)    {
		    int[] xPoints = { (int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i };
		    int[] yPoints = {(int) (-tunnelSize - newCenter/2 + this.points.get(i).getY()), (int) (-tunnelSize -newCenter/2 + this.points.get(i + 1).getY()), 0, 0};
		    this.sky.add(new Polygon(xPoints, yPoints, 4));
	    }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
	    Random r = new Random();

        for (Polygon p: ground) {
	        g2.setColor(new Color(r.nextInt(256), r.nextInt(256),r.nextInt(256)));
            g2.fillPolygon(p);
        }

	    for (Polygon p: sky)    {
		    g2.setColor(new Color(r.nextInt(256), r.nextInt(256),r.nextInt(256)));
		    g2.fillPolygon(p);
	    }

    }
}
