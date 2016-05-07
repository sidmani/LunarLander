import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sid on 5/7/16.
 */
public class Main {
    private static Lander lander = new Lander();
    public static void main(String[] args) {
        Lander.setGravity(0, 100);
        JFrame main = new JFrame();
        main.setExtendedState(JFrame.MAXIMIZED_BOTH);
        main.add(lander);
        main.setVisible(true);
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                lander.tick(0.03);
                main.repaint();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
        class wasdListener implements KeyListener {
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
    }
    public static void loadStage(Stage s) {
        lander.setLoc(s.startLoc);

    }
}
