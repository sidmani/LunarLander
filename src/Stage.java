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
    protected Point startLoc = new Point();
    protected Point endLoc = new Point();
    private ArrayList<Point2D.Double> points = new ArrayList<>();

    public Stage(int width, int height)  {
        Random r = new Random();

        for (int i = 0; i < width; i++) {
            points.add(new Point2D.Double(height - r.nextInt(height),i));
        }
    }

/*
public Stage(String filename) {
try {
BufferedReader in = new BufferedReader(new FileReader(filename));
String str;
str = in.readLine();
while ((str = in.readLine()) != null) {
String[] pts = str.split(",");
lines.add(new Line2D.Double(Integer.parseInt(pts[0]), Integer.parseInt(pts[1]),Integer.parseInt(pts[2]),Integer.parseInt(pts[3])));
}
in.close();
} catch (IOException e) {
System.out.println("File Read Error");
System.out.println("This should never happen");
}
}
*/

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        while (this.points.size() > 1)  {
            g2.draw(new Line2D.Double(this.points.remove(0), this.points.remove(0)));
        }

    }
}
