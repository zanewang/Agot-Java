package got.ui;

import got.core.Node;
import got.utility.Utility;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;

public class RoundDialog extends JDialog {

    private Image roundImage = Utility.loadImage("got/resource/round.png");

    public RoundDialog(Node node) {
        super(node.gameFrame);
        int width = 480;
        int height = 236;
        this.setSize(480, 236);
        this.setLocationRelativeTo(node.gameFrame);
        this.add(createPanel());
        this.setUndecorated(true);
        this.setVisible(true);
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RoundDialog.this.dispose();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel() {
            public void update(Graphics g) {
                g.drawImage(roundImage, 0, 0, null);
                g.dispose();
            }

            public void paint(Graphics g) {
                update(g);
            }
        };
        panel.repaint();
        return panel;
    }
}
