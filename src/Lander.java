import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sid on 5/7/16.
 */
public class Lander extends JComponent{
    private static Vector<Double> baseAcceleration = new Vector<Double>();
    protected Rectangle2D.Double collisionRect;
    private double scaleFactor = 0.25;
    private BufferedImage mainImage;
    protected Point location = new Point();

    private double angle = Math.PI/2;

    private Vector<Double> velocity;
    private Vector<Double> acceleration;

    private double rotationalVelocity = 0;
    private double rotationalAcc = 0;

    protected double fuelPercent=100;

    public static void setGravity(double xComp, double yComp) {
        baseAcceleration.add(xComp);
        baseAcceleration.add(yComp);
    }

    public Lander(int frameWidth, int frameHeight) {
        try {
            mainImage = ImageIO.read(new File("src/lander.png"));
        } catch (IOException e) {
            System.out.println("failure");
        }
        collisionRect = new Rectangle2D.Double(getX(), getY(), mainImage.getWidth(), mainImage.getHeight());
        this.setBounds(this.getX(), getY(), frameWidth, frameHeight);
        velocity = new Vector<Double>();
        velocity.add(0.0);
        velocity.add(0.0);
        acceleration = new Vector<Double>();
        acceleration.add(0.0);
        acceleration.add(0.0);
    }
    public void resetMotion() {
        velocity = new Vector<Double>();
        velocity.add(0.0);
        velocity.add(0.0);
        acceleration = new Vector<Double>();
        acceleration.add(0.0);
        acceleration.add(0.0);
        rotationalVelocity = 0;
        rotationalAcc = 0;
        angle = Math.PI/2;
    }
    public void setScaleFactor(double factor) {
        scaleFactor = factor;
    }
    public void setLoc(int x, int y) {
        location = new Point(x,y);
        collisionRect = new Rectangle2D.Double(getX(), getY(), mainImage.getWidth(), mainImage.getHeight());
    }

    public void tick(Double elapsed) {
        if (fuelPercent > 0) {
            if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
                fuelPercent -= 9*elapsed;
            }
            if (rotationalAcc != 0) {
                fuelPercent -= 3*elapsed;
            }
        }
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
        if (enable && fuelPercent > 0) {
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
        if (enable && fuelPercent > 0) {
            rotationalAcc = 1;
            //TODO: display flames
        }
        else {
            rotationalAcc = 0;
        }
    }
    public void applyThrustRight(boolean enable) {
        if (enable && fuelPercent > 0) {
            rotationalAcc = -1;
            //TODO: display flames
        }
        else {
            rotationalAcc = 0;
        }
    }
    public void refuel() {
        fuelPercent = 100;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        collisionRect.x = location.x;
        collisionRect.y = location.y;

        AffineTransform t = new AffineTransform();
        t.translate(location.getX(), location.getY());
        t.rotate(angle-Math.PI/2);
        if (scaleFactor != 1) {
            t.scale(scaleFactor, scaleFactor);
        }

        g2.drawImage(mainImage, t, this);
        g2.setColor(Color.GREEN);
        g2.draw(collisionRect);
    }
}
