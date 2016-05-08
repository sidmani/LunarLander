import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sid on 5/7/16.
 */
public class Main {
    private static String[] fuelBars = {
            "|", "||", "|||", "||||", "|||||", "||||||", "|||||||", "||||||||", "|||||||||", "||||||||||", "|||||||||||"
    };
    private static Lander lander;
    private static Stage currStage;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(null);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.getContentPane().setBackground(Color.BLACK);
        lander = new Lander(frame.getWidth(), frame.getHeight());
        FadeIn f = null;
        try {
            BufferedImage b;
            b = ImageIO.read(new File("src/logo.png"));
            f = new FadeIn(b, frame.getWidth()/2, frame.getHeight()/2, frame.getWidth(), frame.getHeight());
            frame.add(f);
            f.startIncrease();
        } catch (IOException e) {
            System.out.println("failure to load logo");
        }
        frame.setVisible(true);
        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if (f != null) {
            f.startDecrease();
        }
        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        frame.getContentPane().removeAll();

        frame.add(lander);
        frame.setVisible(true);

        frame.add(new Stars(100, frame.getWidth(), frame.getHeight()));
        frame.setVisible(true);

        JLabel fuelPercent = new JLabel();
        fuelPercent.setBounds(10,10,frame.getWidth(),80);
        fuelPercent.setFont(fuelPercent.getFont().deriveFont(64f));
        fuelPercent.setForeground(Color.WHITE);

        frame.add(fuelPercent);

        Lander.setGravity(0, 100);  //xComponent Gravity is always 0, yComponent should be changeable

        class wasdListener implements KeyListener { //if the user wants to use W,A,S,D keys
            public void keyTyped(KeyEvent e) {
            }

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    // A pressed
                    lander.applyThrustRight(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    // A pressed
                    lander.applyThrustLeft(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    // A pressed
                    lander.applyThrustDown(true, -200);
                }
            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    // A pressed
                    lander.applyThrustRight(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    // A pressed
                    lander.applyThrustLeft(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    // A pressed
                    lander.applyThrustDown(false, -200);
                }
            }
        }
        frame.addKeyListener(new wasdListener());

        class arrowListener implements KeyListener { //if the user wants to use the arrow keys
            public void keyTyped(KeyEvent e) {
            }

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    // A pressed
                    lander.applyThrustDown(true, -200);
                }
            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    // A pressed
                    lander.applyThrustDown(false, -200);
                }
            }
        }
        frame.addKeyListener(new arrowListener());
	    frame.setVisible(true);
        //////////////start levels//////////////////////////////
        for (int i = 0; i<10; i++) {
            currStage = new Stage(frame.getWidth(), frame.getHeight(), i * 3 + 10);
            currStage.setColor(Color.BLUE);
            lander.refuel();
            lander.setLoc(currStage.startLoc.x, currStage.startLoc.y);
            frame.add(currStage);
            frame.setVisible(true);
            while(!currStage.completed) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lander.tick(0.03);

                frame.repaint();
                fuelPercent.setText((int)lander.fuelPercent + "% " + fuelBars[(int)(lander.fuelPercent/10)]);
                if (currStage != null) {
                    lander.collisionArea.intersect(currStage.collisionArea);
                    if (!lander.collisionArea.isEmpty() || lander.location.x < 0 || lander.location.x > frame.getWidth()) {
                        System.out.println("collision");
                        lander.setLoc(currStage.startLoc.x, currStage.startLoc.y);
                        currStage.resetStage();
                        lander.refuel();
                        lander.resetMotion();
                    }
                }
            }
            frame.remove(currStage);
        }

    }
}
