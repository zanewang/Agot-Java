package got.ui.update;

import got.ui.MouseListenerAdapter;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class LargeMapPanel extends ImageScrollerLargeView {

    public LargeMapPanel(Dimension dimension, ImageScrollModel model) {
        super(dimension, model);
        this.addMouseListener(MOUSE_CLICK_LISTENER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void paintComponent(final Graphics g) {
        System.out.println(String.format("X:%d y:%d", m_model.getX(), m_model.getY()));
        Image image = Utility.loadImage("got/resource/agot-scale.png");
        g.drawImage(image, 0, 0, m_model.getBoxHeight(), m_model.getBoxWidth(), m_model.getY(), m_model.getX(), m_model
                .getY()
                + m_model.getBoxHeight(), m_model.getX() + m_model.getBoxWidth(), null);
    }

    private final MouseListenerAdapter MOUSE_CLICK_LISTENER = new MouseListenerAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            new MusteringDialog(e.getPoint());
        }

    };
}
