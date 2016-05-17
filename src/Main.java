import javafx.embed.swing.JFXPanel;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

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

        JLayeredPane playPanel = new JLayeredPane();
        playPanel.setBounds(1920, 0, 1920, 1080);

        JLayeredPane highScoresPanel = new JLayeredPane();
        highScoresPanel.setBounds(0, 1080, 1920, 1080);

        try {
            play = new ImageIcon(ImageIO.read(new File("src/play.png")));
            highScores = new ImageIcon(ImageIO.read(new File("src/highScores.png")));
            scrollLeft = new ImageIcon(ImageIO.read(new File("src/arrowLeft.png")));
            scrollUp = new ImageIcon(ImageIO.read(new File("src/arrowUp.png")));
            exit = new ImageIcon(ImageIO.read(new File("src/exit.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            }
        });

        JButton launchGame = new JButton("SDGS");
        launchGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.add(Game.getGameFrame().getContentPane());
            }
        });

        launchGame.setBounds(0, 0, 400, 400);

        menuPanel.add(background, new Integer(0));


        menuPanel.add(openPlayArea, new Integer(1));
        menuPanel.add(openHighScoresArea, new Integer(1));

        menuPanel.add(goLeft, new Integer(1));
        menuPanel.add(goUp, new Integer(1));
        menuPanel.add(quit, new Integer(1));

        //menuPanel.add(launchGame, new Integer(2));
        frame.add(menuPanel);

        contentPanel = new JPanel();
        JFrame contentFrame = Game.getGameFrame();
        contentFrame.setBounds(0, 0, 1920, 1080);
        contentFrame.setVisible(true);
        contentPanel.add(contentFrame);
        menuPanel.add(contentPanel, new Integer(3));
        //tester();

        //contentPanel = new JPanel();
        //contentPanel.add(Game.getGameFrame());
        //frame.add(contentPanel);

        frame.setVisible(true);

    }

//    public static void tester() {
//        frame.remove(menuPanel);
//        playGame();
//    }

    public static void playGame() {
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(null);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.getContentPane().setBackground(Color.BLACK);
        lander = new Lander(frame.getWidth(), frame.getHeight());
//        FadeIn f = null;
//        try {
//            BufferedImage b;
//            b = ImageIO.read(new File("src/logo.png"));
//            f = new FadeIn(b, frame.getWidth() / 2, frame.getHeight() / 2, frame.getWidth(), frame.getHeight());
//            frame.add(f);
//            f.startIncrease();
//        } catch (IOException e) {
//            System.out.println("failure to load logo");
//        }
//        frame.setVisible(true);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
//        if (f != null) {
//            f.startDecrease();
//        }
//        try {
//            Thread.sleep(3000);                 //1000 milliseconds is one second.
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
//        frame.getContentPane().removeAll();

        frame.add(lander);
        frame.setVisible(true);

        frame.add(new Stars(100, frame.getWidth(), frame.getHeight()));
        frame.setVisible(true);

        JLabel fuelPercent = new JLabel();
        fuelPercent.setBounds(10, 10, frame.getWidth() - 10, 80);
        fuelPercent.setFont(fuelPercent.getFont().deriveFont(64f));
        fuelPercent.setForeground(Color.WHITE);

        JLabel attempts = new JLabel("0 ATTEMPTS", SwingConstants.RIGHT);
        attempts.setBounds(10, 10, frame.getWidth() - 10, 80);
        attempts.setFont(fuelPercent.getFont().deriveFont(64f));
        attempts.setForeground(Color.WHITE);

        JLabel velocityLabel = new JLabel("");
        velocityLabel.setBounds(10, frame.getHeight() - 90, frame.getWidth() - 10, 80);
        velocityLabel.setFont(fuelPercent.getFont().deriveFont(64f));
        velocityLabel.setForeground(Color.GREEN);

        JLabel angleLabel = new JLabel("", SwingConstants.RIGHT);
        angleLabel.setBounds(10, frame.getHeight() - 90, frame.getWidth() - 10, 80);
        angleLabel.setFont(fuelPercent.getFont().deriveFont(64f));
        angleLabel.setForeground(Color.GREEN);

        frame.add(fuelPercent);
        frame.add(attempts);
        frame.add(velocityLabel);
        frame.add(angleLabel);
        Lander.setGravity(0, 100);  //xComponent Gravity is always 0, yComponent should be changeable

        class wasdListener implements KeyListener { //if the user wants to use W,A,S,D keys

            public void keyTyped(KeyEvent e) {

            }

            /**
             * Handle the key-pressed event from the text field.
             */
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(true, 50);
                    if ((c == null || !c.isRunning()) && lander.fuelPercent > 0) {
                        try {
                            c = playSound("src/rocketSound.wav");
                        } catch (LineUnavailableException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedAudioFileException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(true, 50);
                    if ((c == null || !c.isRunning()) && lander.fuelPercent > 0) {
                        try {
                            c = playSound("src/rocketSound.wav");
                        } catch (LineUnavailableException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedAudioFileException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_UP) {
                    // A pressed
                    lander.applyThrustDown(true, -200);
                    if ((c == null || !c.isRunning()) && lander.fuelPercent > 0) {
                        try {
                            c = playSound("src/rocketSound.wav");
                        } catch (LineUnavailableException e1) {
                            e1.printStackTrace();
                        } catch (UnsupportedAudioFileException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    reset();
                }
            }

            /**
             * Handle the key-released event from the text field.
             */
            public void keyReleased(KeyEvent e) {
                if (c != null) {
                    c.stop();
                }
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(false, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(false, 0);
                } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_UP) {
                    // A pressed
                    lander.applyThrustDown(false, -200);
                }
            }
        }
        frame.addKeyListener(new wasdListener());
        frame.setVisible(true);
        //////////////start levels//////////////////////////////
        int attemptCount = 0;
        boolean complete;
        int velocity = 0;
        for (int i = 0; i < 10; i++) {
            attempts.setText(attemptCount + (attemptCount == 1 ? " ATTEMPT" : " ATTEMPTS"));
            currStage = new Stage(frame.getWidth(), frame.getHeight(), 40);
            currStage.setColor(Color.BLUE);
            reset();
            frame.add(currStage);
            frame.setVisible(true);
            complete = false;
            while (!complete) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lander.tick(0.03);
                frame.repaint();
                fuelPercent.setText((int) lander.fuelPercent + "% " + fuelBars[(int) (lander.fuelPercent / 10)]);
                velocity = (int) Math.sqrt(lander.velocity.get(0) * lander.velocity.get(0) + lander.velocity.get(1) * lander.velocity.get(1));
                velocityLabel.setText(velocity + " METERS/SEC");
                angleLabel.setText(String.valueOf((int) Math.toDegrees(lander.angle) - 90) + " DEG");
                if (velocity > MAX_SAFE_VELOCITY) {
                    velocityLabel.setForeground(Color.RED);
                } else {
                    velocityLabel.setForeground(Color.GREEN);
                }
                if (Math.abs(lander.angle - Math.PI / 2) > ANGLE_ACCURACY) {
                    angleLabel.setForeground(Color.RED);
                } else {
                    angleLabel.setForeground(Color.GREEN);
                }
                if (currStage != null) {
                    JetPack j = currStage.struckJetPack(lander.collisionArea);
                    if (j != null) {
                        lander.addPowerup(j);
                        System.out.println("Struck Jet Pack");
                    }
                    if (lander.collisionArea.intersects(currStage.landingPad)) {
                        System.out.println("Landing Attempt");
                        if (Math.abs(lander.angle - Math.PI / 2) < ANGLE_ACCURACY && velocity < MAX_SAFE_VELOCITY && lander.location.y < currStage.landingPad.y + 5) {
                            //victory
                            complete = true;
                            lander.refuel();
                            lander.resetMotion();
                        } else {
                            reset();
                            attemptCount++;
                            attempts.setText(attemptCount + (attemptCount == 1 ? " ATTEMPT" : " ATTEMPTS"));
                        }
                    }
                    lander.collisionArea.intersect(currStage.collisionArea);
                    if (!lander.collisionArea.isEmpty() || lander.location.x < 0 || lander.location.x > frame.getWidth()) {
                        Clip explosion;
                        try {
                            explosion = playSound("src/rocketExplosion.wav");

                            Thread.sleep(3000);
                        } catch (LineUnavailableException e) {
                            e.printStackTrace();
                        } catch (UnsupportedAudioFileException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        reset();
                        attemptCount++;
                        attempts.setText(attemptCount + (attemptCount == 1 ? " ATTEMPT" : " ATTEMPTS"));
                    }

                }
            }
            frame.remove(currStage);
            attemptCount = 0;
        }
    }

    public static void reset() {
        lander.refuel();
        lander.resetMotion();
        if (currStage != null) {
            lander.setLoc(currStage.startLoc.x, currStage.startLoc.y);
            currStage.addJetPacks();
        }
    }

    public static Clip playSound(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        File url = new File(fileName);
        Clip clip = AudioSystem.getClip();

        AudioInputStream ais = AudioSystem.
                getAudioInputStream(url);
        clip.open(ais);
        clip.start();
        return clip;
    }

}
