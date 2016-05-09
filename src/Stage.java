import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sid on 5/7/16.
 */
public class Stage extends JComponent {

	protected Point startLoc;
	protected Rectangle2D.Double landingPad;
	protected Area collisionArea = new Area();
	private ArrayList<JetPack> powerups = new ArrayList<>();
	private ArrayList<Point2D.Double> points = new ArrayList<>();
	private ArrayList<Polygon> ground = new ArrayList<>();
	private ArrayList<Polygon> sky = new ArrayList<>();
	private Color color = null;
	private int countJetPack = 0;
	private boolean goingUpJetPack = true;
	private int countLandingX = 0;
	private int countLandingY = 0;
	private boolean goingUpLanding = true;
	private boolean goingRightLanding = true;
	private int difficulty;
	private double finalX, finalY;

	public Stage(int width, int height, int difficulty) {
		setBounds(getX(), getY(), width, height);

		this.difficulty = difficulty;
		int NUMBER_OF_LINES = ((difficulty - 1) / 3) * 2;

		Random r = new Random();
		int centerY = (height - 200) / 2;
		int tunnelSize = (500 - 4 * difficulty);
		for (int i = 0; i <= width; i += width / NUMBER_OF_LINES) {
			this.points.add(new Point2D.Double(i, height - r.nextInt((int) tunnelSize)));
		}
		for (int i = 0; i < this.points.size() - 1; i++) {
			int[] xPoints = {(int) this.points.get(i).getX(), (int) this.points.get(i + 1).getX(), (width / NUMBER_OF_LINES) * (i + 1), (width / NUMBER_OF_LINES) * i};
			int[] yPoints = {(int) (this.points.get(i).getY() + 0.5 * tunnelSize - centerY), (int) (this.points.get(i + 1).getY() + 0.5 * tunnelSize - centerY), height, height};
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
		startLoc = new Point(15, (int) ((this.points.get(0).getY() + 0.5 * tunnelSize - centerY) + (this.points.get(0).getY() - 0.5 * tunnelSize - centerY)) / 2);
		Point endLoc = new Point((int) this.points.get(points.size() - 2).getX(), (int) ((this.points.get(points.size() - 2).getY() - centerY) + 30));
		landingPad = new Rectangle2D.Double(endLoc.x - 10, endLoc.y + tunnelSize / 8, 60, 5);
		finalX = landingPad.getX();
		finalY = landingPad.getY();
	}

	public void addJetPacks() {
		powerups.add(new JetPack(getWidth() / 2, (int) (this.points.get(points.size() / 2).getY() - getHeight() / 2)));
	}

	public JetPack struckJetPack(Area rect) {
		for (JetPack j : powerups) {
			if (rect.intersects(new Rectangle2D.Double(j.xLoc, j.yLoc, j.jetPackImage.getWidth(), j.jetPackImage.getHeight()))) {
				powerups.remove(j);
				return j;
			}
		}
		return null;
	}

	public void setColor(Color c) {
		color = c;
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
		} else {
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
		if (difficulty > 25) {
			if ((countLandingY % 30) == 0) {
				goingUpLanding = !goingUpLanding;
			}

			if (goingUpLanding) {
				countLandingY++;
			} else {
				countLandingY--;
			}

			landingPad.setRect(landingPad.getX(), finalY + countLandingY, landingPad.getWidth(), landingPad.getHeight());
		}

		if (difficulty > 30) {
			if ((countLandingX % 11) == 0) {
				goingRightLanding = !goingRightLanding;
			}

			if (goingRightLanding) {
				countLandingX++;
			} else {
				countLandingX--;
			}

			landingPad.setRect(finalX + countLandingX, landingPad.getY(), landingPad.getWidth(), landingPad.getHeight());
		}

		g2.fill(landingPad);

		if ((countJetPack % 30) == 0) {
			goingUpJetPack = !goingUpJetPack;
		}

		if (goingUpJetPack) {
			countJetPack++;
		} else {
			countJetPack--;
		}

		for (JetPack j : powerups) {

			g2.drawImage(j.jetPackImage, j.xLoc, j.yLoc - countJetPack, this);
		}
	}
}
