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
    private static final double ANGLE_ACCURACY = Math.PI/12;
    private static final int MAX_SAFE_VELOCITY = 100;

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
            Thread.sleep(3000);
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
        fuelPercent.setBounds(10,10,frame.getWidth()-10,80);
        fuelPercent.setFont(fuelPercent.getFont().deriveFont(64f));
        fuelPercent.setForeground(Color.WHITE);

        JLabel attempts = new JLabel("0 ATTEMPTS", SwingConstants.RIGHT);
        attempts.setBounds(10,10,frame.getWidth()-10,80);
        attempts.setFont(fuelPercent.getFont().deriveFont(64f));
        attempts.setForeground(Color.WHITE);

        JLabel velocityLabel = new JLabel("");
        velocityLabel.setBounds(10,frame.getHeight()-90,frame.getWidth()-10,80);
        velocityLabel.setFont(fuelPercent.getFont().deriveFont(64f));
        velocityLabel.setForeground(Color.GREEN);

        JLabel angleLabel = new JLabel("", SwingConstants.RIGHT);
        angleLabel.setBounds(10,frame.getHeight()-90,frame.getWidth()-10,80);
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

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_UP) {
                    // A pressed
                    lander.applyThrustDown(true, -200);
                }
                else if (e.getKeyCode() == KeyEvent.VK_R) {
                    reset();
                }
            }

            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // A pressed
                    lander.applyThrustRight(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // A pressed
                    lander.applyThrustLeft(false);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_UP) {
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
        for (int i = 0; i<10; i++) {
            attempts.setText(attemptCount + (attemptCount == 1 ? " ATTEMPT" : " ATTEMPTS"));
            currStage = new Stage(frame.getWidth(), frame.getHeight(), i * 3 + 10);
            currStage.setColor(Color.BLUE);
            reset();
            frame.add(currStage);
            frame.setVisible(true);
            complete = false;
            while(!complete) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lander.tick(0.03);
                frame.repaint();
                fuelPercent.setText((int)lander.fuelPercent + "% " + fuelBars[(int)(lander.fuelPercent/10)]);
                velocity = (int) Math.sqrt(lander.velocity.get(0)*lander.velocity.get(0) + lander.velocity.get(1)*lander.velocity.get(1));
                velocityLabel.setText(velocity + " METERS/SEC");
                angleLabel.setText(String.valueOf((int)Math.toDegrees(lander.angle) - 90) + " DEG");
                if (velocity > MAX_SAFE_VELOCITY) {
                    velocityLabel.setForeground(Color.RED);
                }
                else {
                    velocityLabel.setForeground(Color.GREEN);
                }
                if (Math.abs(lander.angle - Math.PI/2) > ANGLE_ACCURACY) {
                    angleLabel.setForeground(Color.RED);
                }
                else {
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
                        if (Math.abs(lander.angle - Math.PI/2) < ANGLE_ACCURACY && velocity < MAX_SAFE_VELOCITY && lander.location.y < currStage.landingPad.y + 5) {
                            //victory
                            complete = true;
                            lander.refuel();
                            lander.resetMotion();
                        }
                        else {
                            reset();
                            attemptCount++;
                            attempts.setText(attemptCount + (attemptCount == 1 ? " ATTEMPT" : " ATTEMPTS"));
                        }
                    }
                    lander.collisionArea.intersect(currStage.collisionArea);
                    if (!lander.collisionArea.isEmpty() || lander.location.x < 0 || lander.location.x > frame.getWidth()) {
                        System.out.println("collision");
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
}
