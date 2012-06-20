package got.test;

import got.client.Client;
import got.ui.update.ImageScrollModel;
import got.ui.update.ImageScrollerLargeView;
import got.ui.update.ImageScrollerSmallView;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JApplet;
import javax.swing.JSplitPane;

public class TestApplet extends JApplet {
    private Client client;

    @Override
    public void init() {

        this.setSize(960 + 100, 768);
        ImageScrollModel model = new ImageScrollModel();
        model.setBoxDimensions(480, 384);
        model.setScrollX(false);
        Image image = Utility.loadImage("got/resource/agotsmall.png");
        ImageScrollerSmallView small = new ImageScrollerSmallView(image, model);
        LargePanel large = new LargePanel(new Dimension(960, 1768), model);
        JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, small, large);
        mainPanel.setSize(960 + 277, 768);
        mainPanel.setDividerSize(0);
        mainPanel.setDividerLocation(100);
        this.add(mainPanel);
    }

    class LargePanel extends ImageScrollerLargeView {
        public LargePanel(Dimension dimension, ImageScrollModel model) {
            super(dimension, model);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void paintComponent(final Graphics g) {
            System.out.println(String.format("X:%d y:%d", m_model.getX(), m_model.getY()));
            Image image = Utility.loadImage("got/resource/agot.png");
            g.drawImage(image, 0, 0, 480, 384, m_model.getX(), m_model.getY(), m_model.getX() + 480,
                    m_model.getY() + 384, null);
        }
    }
}
