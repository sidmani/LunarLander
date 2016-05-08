import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sid on 5/7/16.
 */
public class Stage extends JComponent {
    protected boolean completed = false;
    protected Point startLoc;
    protected Rectangle2D.Double landingPad;

	private final int NUMBER_OF_LINES = 9;
    private ArrayList<Point2D.Double> points = new ArrayList<>();
	private ArrayList<Polygon> ground = new ArrayList<>();
	private ArrayList<Polygon> sky = new ArrayList<>();
    private Color color = null;
    protected Area collisionArea = new Area();
    public Stage(int width, int height, int difficulty)  {
	    setBounds(getX(), getY(), width, height);

        Random r = new Random();
        int centerY = height/2;
		int tunnelSize = (500-4*difficulty);
        for (int i = 0; i <= width; i+= width / this.NUMBER_OF_LINES) {
            this.points.add(new Point2D.Double(i,height - r.nextInt((int) tunnelSize)));
        }
	    for (int i = 0; i < this.points.size() - 1; i++) {
		    int[] xPoints = {(int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i };
		    int[] yPoints = {(int) (this.points.get(i).getY() + 0.5*tunnelSize - centerY), (int) (this.points.get(i + 1).getY() + 0.5*tunnelSize - centerY), height, height};
            Polygon p = new Polygon(xPoints, yPoints, 4);
		    this.ground.add(p);
            collisionArea.add(new Area(p));
	    }

	    for (int i = 0; i < this.points.size() - 1; i++) {
            int[] xPoints = {(int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i};
            int[] yPoints = {(int) (this.points.get(i).getY() - 0.5 * tunnelSize - centerY), (int) (this.points.get(i + 1).getY() - 0.5 * tunnelSize - centerY), 0, 0};
            Polygon p = new Polygon(xPoints, yPoints, 4);
            this.sky.add(p);
            collisionArea.add(new Area(p));
        }
        startLoc = new Point(15,  (int)((this.points.get(0).getY() + 0.5*tunnelSize - centerY)+(this.points.get(0).getY() - 0.5 * tunnelSize - centerY))/2);
        Point endLoc = new Point((int)this.points.get(points.size()-2).getX(),  (int)((this.points.get(points.size()-2).getY() + 0.5*tunnelSize - centerY)+(this.points.get(points.size()-2).getY() - 0.5 * tunnelSize - centerY))/2 + 30);
        landingPad = new Rectangle2D.Double(endLoc.x - 10, endLoc.y, 60, 5);
    }
    public void setColor(Color c) {
        color = c;
    }
    public void resetStage() {
        completed = false;
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
	    if (color == null) {
            Random r = new Random();

            for (Polygon p : ground) {
                g2.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                g2.fillPolygon(p);
            }

            for (Polygon p : sky) {
                g2.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                g2.fillPolygon(p);
            }
        }
        else {
            g2.setColor(color);
            for (Polygon p : ground) {
                g2.fillPolygon(p);
            }
            for (Polygon p : sky) {
                g2.fillPolygon(p);
            }
        }
        g2.setColor(Color.RED);
        g2.draw(collisionArea);
        g2.setColor(Color.CYAN);
        g2.fill(landingPad);
    }
}
