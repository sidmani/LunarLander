import javafx.embed.swing.JFXPanel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Sid on 5/7/16.
 */
public class Main {


    private static final double ANGLE_ACCURACY = Math.PI / 12;
    private static final int MAX_SAFE_VELOCITY = 100;
    static Clip c = null;

    private static String[] fuelBars = {
            "|", "||", "|||", "||||", "|||||", "||||||", "|||||||", "||||||||", "|||||||||", "||||||||||", "|||||||||||"
    };
    private static Lander lander;
    private static Stage currStage;

    public static void main(String[] args) {
        JFXPanel jfx = new JFXPanel();
        Music.fetchTracks();
        Music.playIntro();

        JFrame frame = new JFrame();
        frame.add(Game.getGameFrame()); //this is the part that will be executed by the "Play" button in the menu
        frame.setVisible(true);

    }


}
