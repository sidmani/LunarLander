import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Nilay on 5/7/16.
 */
public class JetPack {

    protected BufferedImage jetPackImage;
    protected double fuelRegenPercent = 40;
    protected int xLoc, yLoc;

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
