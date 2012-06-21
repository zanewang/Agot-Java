package got.ui.update;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
        this.setSize(image.getWidth(null), image.getHeight(null));
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
