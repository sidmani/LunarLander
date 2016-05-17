import javafx.embed.swing.JFXPanel;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sid on 5/7/16.
 */
public class Main {
    static Clip c = null;
    static BufferedImage background;
    static ImageIcon play;
    static ImageIcon highScores; //high scores
    static JFrame frame;
    static JLayeredPane menuPanel;

    static JPanel contentPanel;

    static ImageIcon scrollLeft;
    static ImageIcon scrollUp;

    static ImageIcon exit;
    static ImageIcon start;
    static ImageIcon back;

    static JFrame contentFrame;

    private static final double ANGLE_ACCURACY = Math.PI / 12;
    private static final int MAX_SAFE_VELOCITY = 100;

    private static String[] fuelBars = {
            "|", "||", "|||", "||||", "|||||", "||||||", "|||||||", "||||||||", "|||||||||", "||||||||||", "|||||||||||"
    };
    private static Lander lander;
    private static Stage currStage;

    public static void main(String[] args) {

        JFXPanel jfx = new JFXPanel();
        Music.fetchTracks();
        Music.playIntro();

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(3840, 2160);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        menuPanel = new JLayeredPane();
        menuPanel.setBounds(0, 0, 3840, 2160);

        ImageDrawer background = new ImageDrawer();
        background.setBounds(0, 0, 3840, 2160);

        try {
            play = new ImageIcon(ImageIO.read(new File("src/play.png")));
            highScores = new ImageIcon(ImageIO.read(new File("src/highScores.png")));
            scrollLeft = new ImageIcon(ImageIO.read(new File("src/arrowLeft.png")));
            scrollUp = new ImageIcon(ImageIO.read(new File("src/arrowUp.png")));
            exit = new ImageIcon(ImageIO.read(new File("src/exit.png")));
            start = new ImageIcon(ImageIO.read(new File("src/start.png")));
            back = new ImageIcon(ImageIO.read(new File("src/back.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton playButton = new JButton(start);
        playButton.setBounds(1920 + (1920 / 2) - start.getIconWidth() / 2, (1080 / 5) - start.getIconHeight(), start.getIconWidth(), start.getIconHeight());

        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);

        JButton openPlayArea = new JButton(play);
        openPlayArea.setBounds(1150, 500, play.getIconWidth(), play.getIconHeight());

        JButton openHighScoresArea = new JButton(highScores);
        openHighScoresArea.setBounds(1250, 600, highScores.getIconWidth(), highScores.getIconHeight());

        JButton goLeft = new JButton(scrollLeft);
        goLeft.setBounds(1920, (1080 / 2) - scrollLeft.getIconHeight() / 2, scrollLeft.getIconWidth(), scrollLeft.getIconHeight());

        JButton goUp = new JButton(scrollUp);
        goUp.setBounds((1980 / 2) - scrollUp.getIconWidth(), 1080, scrollUp.getIconWidth(), scrollUp.getIconHeight());

        JButton quit = new JButton(exit);
        quit.setBounds(1770, 0, exit.getIconWidth(), exit.getIconHeight());

        quit.setContentAreaFilled(false);
        quit.setBorderPainted(false);

        openPlayArea.setContentAreaFilled(false);
        openHighScoresArea.setContentAreaFilled(false);

        openPlayArea.setBorderPainted(false);
        openHighScoresArea.setBorderPainted(false);

        goLeft.setContentAreaFilled(false);
        goUp.setContentAreaFilled(false);

        goLeft.setBorderPainted(false);
        goUp.setBorderPainted(false);

        openPlayArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 1920; i++) {
                    frame.setLocation(0 - i, 0); //SET A TIMER FOR THIS
                }
            }
        });

        openHighScoresArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 1080; i++) {
                    frame.setLocation(0, 0 - i); //SET A TIMER FOR THIS
                }
            }
        });

        goLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = -1920; i < 1; i++) {
                    frame.setLocation(i, 0); //SET A TIMER FOR THIS
                }
            }
        });

        goUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = -1080; i < 1; i++) {
                    frame.setLocation(0, i); //SET A TIMER FOR THIS
                }
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        contentPanel = new JPanel();
                        contentPanel.add(Game.getGameFrame());
                        menuPanel.add(contentPanel, new Integer(3));
                        Game.isClosed = false;
                    }
                }.start();
            }
        });


        menuPanel.add(background, new Integer(0));


        menuPanel.add(openPlayArea, new Integer(1));
        menuPanel.add(openHighScoresArea, new Integer(1));

        menuPanel.add(goLeft, new Integer(1));
        menuPanel.add(goUp, new Integer(1));
        menuPanel.add(quit, new Integer(1));

        menuPanel.add(playButton, new Integer(1));

        frame.add(menuPanel);


        frame.setVisible(true);

    }


}
