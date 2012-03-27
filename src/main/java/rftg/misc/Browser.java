package rftg.misc;

import rftg.bundle.images.ImageBundle;
import rftg.bundle.images.ImageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Browser {
    public static void main(String[] args) {
        final JFrame test = new JFrame("Test");
        final ImageBundle imageBundle = new ImageBundle("images.data");
        imageBundle.load();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Image image = new Image(imageBundle.getCard(0));
        test.add(image);
        test.pack();
        test.setVisible(true);
        test.addKeyListener(new KeyAdapter() {
            int i = 0;

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Browser.keyPressed:36 - i = " + i);
                BufferedImage card = null;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    do {
                        inc();
                        card = imageBundle.getCard(i);
                    } while (card == null);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    do {
                        dec();
                        card = imageBundle.getCard(i);
                    } while (card == null);
                }

                image.setImage(card);
                test.repaint();
            }

            private void dec() {
                i--;
                if (i < 0) i = imageBundle.count(ImageType.CARDS) - 1;
            }

            private void inc() {
                i++;
                if (i > imageBundle.count(ImageType.CARDS)) i = 0;
            }
        });
    }


    public static class Image extends Component {

        BufferedImage img;

        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }

        public Image(BufferedImage img) {
            this.img = img;
        }

        public Dimension getPreferredSize() {
            if (img == null) {
                return new Dimension(100, 100);
            } else {
                return new Dimension(img.getWidth(null), img.getHeight(null));
            }
        }

        public void setImage(BufferedImage card) {
            this.img = card;
            this.repaint();
        }
    }
}
