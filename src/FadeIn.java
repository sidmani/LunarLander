//Note: This is from stackoverflow and is not a key part of the game.
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class FadeIn extends JComponent implements ActionListener {

    private BufferedImage imagem;
    private Timer timer;
    private float alpha = 0f;
    private Point loc;
    private boolean increase = true;
    public FadeIn(BufferedImage b, int x, int y) {
        imagem = b;
        loc = new Point(x,y);
    }
    public void startIncrease() {
        timer = new Timer(100, this);
        increase = true;
        timer.start();
    }
    public void startDecrease() {
        timer = new Timer(100, this);
        increase = false;
        timer.start();
    }
    // here you define alpha 0f to 1f
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                alpha));
        g2d.drawImage(imagem, loc.x - imagem.getWidth()/2, loc.y - imagem.getHeight()/2, null);
      //  g2d.drawImage(imagem, 0, 0, null);
        System.out.println("repainted. alpha: " + alpha);

    }

    public void actionPerformed(ActionEvent e) {
        if (increase) {
            alpha += 0.05f;
            if (alpha > 0.95) {
                alpha = 1;
                timer.stop();
            }
        }
        else {
            alpha -= 0.05f;
            if (alpha < 0.05) {
                alpha = 0;
                timer.stop();
            }
        }
        revalidate();
        repaint();
    }
}