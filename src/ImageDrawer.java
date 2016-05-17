import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageDrawer extends JComponent {
    public static BufferedImage background; //array of images
    private int image_index; //index of image in array

    public ImageDrawer() {
        try {
            //get all images from resources folder and resize to screen size, add them to array
            background = ImageIO.read(new File("src/background.png"));
        } catch (IOException e) {
            System.out.println("SDGSDGSDG");
        }
    }

    public void paintComponent(Graphics g) //paint component
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, this); //draw the image across the entire screen
    }
}
