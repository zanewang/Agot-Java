package got.ui;

import got.core.ClickManager;
import got.core.Node;
import got.utility.Utility;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainPanel extends JPanel {

    private JFrame owner;
    private Image screen;
    private Graphics graphics;
    private int width = 960;
    private int height = 1768;

    private Image backgroundImage = Utility.loadImage("got/resource/agot.png");
    private final Node node;

    private ClickManager clickManager;
    private DrawManager drawManager;

    public MainPanel(Node node) {
        this.node = node;
        this.screen = Utility.createImage(width, height, true);
        this.setPreferredSize(new Dimension(width, height));
        this.graphics = (Graphics2D) screen.getGraphics();

        this.owner = node.gameFrame;
        this.clickManager = new ClickManager(node);
        this.drawManager = new DrawManager(graphics, node);

        this.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                clickManager.handler(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                System.out.println(e.getPoint().x);
                System.out.println(e.getPoint().y);
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                drawManager.setMousePoint(e.getPoint());
            }

        });
        graphics.drawImage(backgroundImage, 0, 0, null);
        this.repaint();
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainPanel.this.repaint();
                while (MainPanel.this.node.gameEvents.peek() != null) {
                    MainPanel.this.node.gameEvents.poll().handler(MainPanel.this.node);
                }
            }
        });
        timer.setRepeats(true);
        timer.start();

    }

    public void draw() {
        graphics.drawImage(backgroundImage, 0, 0, null);
        drawManager.draw();
    }

    public void update(Graphics g) {
        draw();
        g.drawImage(screen, 0, 0, null);
        g.dispose();
    }

    public void paint(Graphics g) {
        update(g);
    }

}
