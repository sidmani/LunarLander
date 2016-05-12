import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sid on 5/7/16.
 */
public class Lander extends JComponent {
	private static Vector<Double> baseAcceleration = new Vector<Double>();
	protected Rectangle2D.Double collisionRect;
	protected Area collisionArea = new Area();
	protected Point location = new Point();
	protected double angle = Math.PI / 2;
	protected Vector<Double> velocity;
	protected double fuelPercent = 100;
	private double scaleFactor = 0.25;
	private BufferedImage mainImage;
	private BufferedImage base_image;
	private BufferedImage flame_bottom;
	private BufferedImage flame_left;
	private BufferedImage flame_bottom_left;
	private BufferedImage flame_bottom_right;
	private BufferedImage flame_right;
	private Vector<Double> acceleration;
	private double rotationalVelocity = 0;
	private double rotationalAcc = 0;

	public Lander(int frameWidth, int frameHeight) {
		try {
			base_image = ImageIO.read(new File("src/lander.png"));
			flame_bottom = ImageIO.read(new File("src/lander_flame_bottom.png"));
			flame_left = ImageIO.read(new File("src/lander_flame_left.png"));
			flame_bottom_left = ImageIO.read(new File("src/lander_flame_bottom_left.png"));
			flame_bottom_right = ImageIO.read(new File("src/lander_flame_bottom_right.png"));
			flame_right = ImageIO.read(new File("src/lander_flame_right.png"));


		} catch (IOException e) {
			System.out.println("failure");
		}
		mainImage = base_image;
		collisionRect = new Rectangle2D.Double(getX(), getY(), mainImage.getWidth(), mainImage.getHeight());
		this.setBounds(this.getX(), getY(), frameWidth, frameHeight);
		velocity = new Vector<Double>();
		velocity.add(0.0);
		velocity.add(0.0);
		acceleration = new Vector<Double>();
		acceleration.add(0.0);
		acceleration.add(0.0);
	}

	public static void setGravity(double xComp, double yComp) {
		baseAcceleration.add(xComp);
		baseAcceleration.add(yComp);
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
		angle = Math.PI / 2;
	}

	public void setScaleFactor(double factor) {
		scaleFactor = factor;
	}

	public void setLoc(int x, int y) {
		location = new Point(x, y);
		collisionRect = new Rectangle2D.Double(getX(), getY(), mainImage.getWidth(), mainImage.getHeight());
	}

	public void addPowerup(JetPack j) {
		fuelPercent = Math.min(100, fuelPercent + j.fuelRegenPercent);
	}

	public void tick(Double elapsed) {
		if (fuelPercent > 0) {
			if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
				fuelPercent -= 5 * elapsed;
			}
			if (rotationalAcc != 0) {
				fuelPercent -= 2 * elapsed;
			}
		} else {
			acceleration.set(1, 0.0);
			acceleration.set(0, 0.0);
			rotationalAcc = 0;
            mainImage = base_image;
            Main.c.stop();


		}
		rotationalVelocity += rotationalAcc * elapsed;
		angle += rotationalVelocity * elapsed;

		double angleSin = Math.sin(angle);
		double angleCos = Math.cos(angle);

		double newVelX = velocity.firstElement() + angleCos * acceleration.firstElement() * elapsed + baseAcceleration.firstElement() * elapsed + angleSin * rotationalAcc * elapsed * 50;
		double newVelY = velocity.lastElement() + angleSin * acceleration.lastElement() * elapsed + baseAcceleration.lastElement() * elapsed + angleCos * rotationalAcc * elapsed * 50;
		velocity.set(0, newVelX);
		velocity.set(1, newVelY);

		double newX = location.getX() + velocity.firstElement() * elapsed;
		double newY = location.getY() + velocity.lastElement() * elapsed;
		location.setLocation(newX, newY);
	}

	public void applyThrustDown(boolean enable, double magnitude) {
		if (enable && fuelPercent > 0) {
			acceleration.set(1, magnitude);
			acceleration.set(0, magnitude);
			if (rotationalAcc > 0) {
				mainImage = flame_bottom_left;
			} else if (rotationalAcc < 0) {
				mainImage = flame_bottom_right;
			} else {
				mainImage = flame_bottom;
			}
			//TODO: display flames
		} else {
			acceleration.set(1, 0.0);
			acceleration.set(0, 0.0);
			if (rotationalAcc > 0) {
				mainImage = flame_left;
			} else if (rotationalAcc < 0) {
				mainImage = flame_right;
			} else {
				mainImage = base_image;
			}
		}
	}

	public void applyThrustLeft(boolean enable, double magnitude) {
		if (enable && fuelPercent > 0) {
			rotationalAcc = 1;

            //TODO: display flames
			if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
				mainImage = flame_bottom_left;
			} else {
				mainImage = flame_left;
			}
		} else {
			if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
				mainImage = flame_bottom;
			} else {
				mainImage = base_image;
			}
			rotationalAcc = 0;
		}
	}

	public void applyThrustRight(boolean enable, double magnitude) {
        if (enable && fuelPercent > 0) {
			rotationalAcc = -1;

            //TODO: display flames
			if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
				mainImage = flame_bottom_right;
			} else {
				mainImage = flame_right;
			}
		} else {
			if (acceleration.get(0) != 0 || acceleration.get(1) != 0) {
				mainImage = flame_bottom;
			} else {
				mainImage = base_image;
			}
			rotationalAcc = 0;
		}

	}

	public void refuel() {
		fuelPercent = 100;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		//collisionRect.x = location.x;
		//collisionRect.y = location.y;

		AffineTransform t = new AffineTransform();
		t.translate(location.getX(), location.getY());
		t.rotate(angle - Math.PI / 2);
		if (scaleFactor != 1) {
			t.scale(scaleFactor, scaleFactor);
		}
		collisionArea = new Area(t.createTransformedShape(collisionRect));

		g2.drawImage(mainImage, t, this);
		g2.setColor(Color.GREEN);
		g2.draw(collisionArea);
	}
}
