import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nilay on 5/7/16.
 */
public class JetPack {

	protected BufferedImage jetPackImage;
	protected double fuelRegenPercent = 40;
	protected int xLoc, yLoc;
	private boolean goingUp = true;
	public JetPack(int x, int y) {
		try {
			jetPackImage = ImageIO.read(new File("src/jetPack.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xLoc = x;
		yLoc = y;
	}

}
