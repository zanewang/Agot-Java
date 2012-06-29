package got.ui.update;

import got.client.Client;
import got.ui.MainPanel;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class LargeMapPanel extends ImageScrollerLargeView {

    private Client client;
    private DrawManager drawManager;

    public LargeMapPanel(Dimension dimension, ImageScrollModel model, Client client) {
        super(dimension, model);
        this.client = client;
        drawManager = new DrawManager();
        // TODO Auto-generated constructor stub
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LargeMapPanel.this.repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();

    }

    @Override
    public void paintComponent(final Graphics g) {
        Image image = Utility.loadImage("got/resource/agot-scale.png");
        Image screen = Utility.createImage(image.getWidth(null), image.getHeight(null), true);
        Graphics2D graphics = (Graphics2D) screen.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        drawManager.draw(graphics, client);
        g.drawImage(screen, 0, 0, m_model.getBoxHeight(), m_model.getBoxWidth(), m_model.getY(), m_model.getX(),
                m_model.getY() + m_model.getBoxHeight(), m_model.getX() + m_model.getBoxWidth(), null);

    }

}
