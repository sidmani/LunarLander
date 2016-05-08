import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sid on 5/7/16.
 */
public class Lander extends JComponent{
    private static Vector<Double> baseAcceleration = new Vector<Double>();
    protected Ellipse2D.Double collisionRing = new Ellipse2D.Double(0,0,10,10);

    private BufferedImage mainImage;
    private Point location = new Point();

    private double angle = Math.PI/2;

    private Vector<Double> velocity;
    private Vector<Double> acceleration;

    private double rotationalVelocity = 0;
    private double rotationalAcc = 0;

    private double fuelPercent;

    public static void setGravity(double xComp, double yComp) {
        baseAcceleration.add(xComp);
        baseAcceleration.add(yComp);
    }

    public Lander() {
        try {
            mainImage = ImageIO.read(new File("src/lander.jpg"));
        } catch (IOException e) {
            System.out.println("failure");
        }
        velocity = new Vector<Double>();
        velocity.add(0.0);
        velocity.add(0.0);
        acceleration = new Vector<Double>();
        acceleration.add(0.0);
        acceleration.add(0.0);
    }

    public void tick(Double elapsed) {
        rotationalVelocity += rotationalAcc * elapsed;
        angle += rotationalVelocity*elapsed;

        double angleSin = Math.sin(angle);
        double angleCos = Math.cos(angle);

        double newVelX = velocity.firstElement() + angleCos * acceleration.firstElement()*elapsed + baseAcceleration.firstElement()*elapsed;
        double newVelY = velocity.lastElement() + angleSin * acceleration.lastElement()*elapsed + baseAcceleration.lastElement()*elapsed;
        velocity.set(0, newVelX);
        velocity.set(1, newVelY);

        double newX = location.getX() + velocity.firstElement()*elapsed;
        double newY = location.getY() + velocity.lastElement()*elapsed;
        location.setLocation(newX, newY);
    }

    public void applyThrustDown(boolean enable, double magnitude) {
        if (enable) {
            System.out.println("Thrust enabled with magnitude" + magnitude);

            acceleration.set(1, magnitude);
            acceleration.set(0, magnitude);

            //TODO: display flames
        }
        else {
            acceleration.set(1, 0.0);
            acceleration.set(0, 0.0);
        }
    }

    public void applyThrustLeft(boolean enable) {
        if (enable) {
            rotationalAcc = 1;
            //TODO: display flames
        }
        else {
            rotationalAcc = 0;
        }
    }
    public void applyThrustRight(boolean enable) {
        if (enable) {
            rotationalAcc = -1;
            //TODO: display flames
        }
        else {
            rotationalAcc = 0;
        }
    }
    public void setLoc(Point p) {
        location = p;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform t = new AffineTransform();
        t.translate(location.getX(), location.getY());
        t.rotate(angle-Math.PI/2);
        g2.drawImage(mainImage, t, this);

    }
}
