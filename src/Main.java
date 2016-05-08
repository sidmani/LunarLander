import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private static Lander lander = new Lander();
	
    public static void main(String[] args) {
        JFrame main = new JFrame();
        main.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        main.getContentPane().setBackground(Color.BLACK);
        FadeIn f = null;
        try {
            BufferedImage b;
            b = ImageIO.read(new File("src/logo.jpg"));
            f = new FadeIn(b, main.getWidth()/2, main.getHeight()/2);
            main.add(f);
            f.startIncrease();
        } catch (IOException e) {
            System.out.println("failure to load logo");
        }
        main.setVisible(true);
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
        main.getContentPane().removeAll();
        main.add(new Stars(100, main.getWidth(), main.getHeight()));
        main.setVisible(true);


        Lander.setGravity(0, 100);  //xComponent Gravity is always 0, yComponent should be changeable
        main.add(lander);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                lander.tick(0.03);
                main.repaint();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);

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
        main.addKeyListener(new wasdListener());

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
        main.addKeyListener(new arrowListener());
	    main.setVisible(true);
	    main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public static void loadStage(Stage s) {
        lander.setLoc(s.startLoc);
    }
}
