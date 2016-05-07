import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sid on 5/7/16.
 */
public class Stage extends JComponent {
    protected Point startLoc = new Point();
    protected Point endLoc = new Point();
    private ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();

    public Stage()  {

    }

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

}
